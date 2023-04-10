#!/usr/bin/env python3

##############################################################################
# This script requires python 3.5 or above
#
# Example usage:
# python3 pgmplayer.py \
#  --channel-mapping ../simulator/patterns/UKA-19/logical_to_physical.txt \
#  --playlist ../simulator/sequences/uka19/testlist.txt \
#  --device  /dev/ttyUSB0
#
# usage: pgmplayer.py [-h] [--channel-mapping CHANNEL_MAPPING]
#                     [--playlist PLAYLIST] [--single-cycle] [--pgm PGM]
#                     [--device DEVICE] [--fps FPS] [--verify]
#
# pgmplayer version 2
#
# optional arguments:
#   -h, --help            show this help message and exit
#   --channel-mapping CHANNEL_MAPPING
#                         A channel mapping file
#   --playlist PLAYLIST   A list of pgm files to play
#   --single-cycle        Only play the playlist once and exit
#   --pgm PGM             The PGM file to play
#   --device DEVICE       The OVDMX device to play to. I.e. /dev/ttyAMA0
#   --fps FPS             Override the default frame rate of 20fps
#   --verify              Scan the playlist/pgm file and check for errors
##############################################################################
import sys
from datetime import datetime, timedelta
from pathlib import Path
from io import StringIO
import argparse

from .channels import ChannelMap
from .playlist import Playlist
from .frame_rate import FrameRateController
from .ovdmx import OVDMXOutput
from .base import DummyDMXOutput
from .countdown import CountdownGenerator
from .player import Player

def main():
    parser = argparse.ArgumentParser(description='pgmplayer version 2')
    parser.add_argument('--channel-mapping', type=argparse.FileType(mode='r'), help='A channel mapping file')
    parser.add_argument('--playlist', help='A file that contains a list of pgm files to play')
    parser.add_argument('--playlist-dir', help='Override the folder the playlist files are relative to')
    parser.add_argument('--single-cycle', action='store_true', help='Only play the playlist once and exit')
    parser.add_argument('--pgm', help='The PGM file to play')
    parser.add_argument('--device', help='The OVDMX device to play to. I.e. /dev/ttyAMA0')
    parser.add_argument('--simulator', action="store_true", help="Send to the simulator instead of a real device")
    parser.add_argument('--fps', type=int, default=20, help='Override the default frame rate of 20fps')
    parser.add_argument('--verify', action='store_true', help='Scan the playlist/pgm file and check for errors')
    parser.add_argument('--countdown',
                        help='Before playing the given file, '
                             'start a countdown for the given number of seconds or minutes:seconds')
    parser.add_argument('--countdown-to',
                        help='Before playing the given file, start a countdown to the given datetime in ISO format, '
                             'e.g. 2019-09-27T00:00:00 (only works on Python 3.7 and above)')
    parser.add_argument('--loop-until', help="Run a single PGM file in a loop until a given point in time formatted in ISO format.")
    parser.add_argument('--loop-pgm', help="Run a single PGM file in a loop until a given point in time provided by --loop-until")
    parser.add_argument('--single-step', action='store_true', help='Single step through the PGM file')

    args = parser.parse_args()
    if bool(args.playlist) == bool(args.pgm):
        print('Exactly one of --pgm and --playlist must be specified', file=sys.stderr)
        exit(1)

    if not args.device and not args.simulator and not args.verify:
        print('A device must be specified if not running a format verification', file=sys.stderr)
        exit(1)

    if args.device and args.verify:
        print('A device can not be specified if running a format verification', file=sys.stderr)
        exit(1)

    if args.simulator and args.verify:
        print('A simulator output can not be specified if running a format verification', file=sys.stderr)
        exit(1)


    if args.countdown:
        items = args.countdown.split(":")
        if len(items) == 1:
            minutes = 0
            seconds = int(items[0])
        elif len(items) == 2:
            minutes = int(items[0])
            seconds = int(items[1])
        else:
            print('There can be at most one colon in --countdown')
            exit(1)
        countdown_to = datetime.now() + timedelta(minutes=minutes, seconds=seconds)
    elif args.countdown_to:
        countdown_to = datetime.fromisoformat(args.countdown_to)
    else:
        countdown_to = None

    
    if args.loop_until or args.loop_pgm:
        if countdown_to is not None:
            print('--loop-until and --loop-pgm can not be combined with --countdown flags')
            exit(1)
        if not args.loop_until:
            print('--loop-until arg must be set')
            exit(1)
        if not args.loop_pgm:
            print('--loop-pgm arg must be set')
            exit(1)
        loop_until = datetime.fromisoformat(args.loop_until)
    else:
        loop_until = None

    channel_mapping = None
    if args.channel_mapping:
        channel_mapping = ChannelMap(args.channel_mapping)

    repeating = not args.single_cycle
    if args.pgm:
        # Create a synthetic playlist with a single item
        playlist = Playlist(StringIO(str(Path(args.pgm).absolute().resolve())), repeating=repeating)
    else:
        base_dir = None
        if args.playlist_dir:
            base_dir = args.playlist_dir
        playlist = Playlist(args.playlist, base_dir=base_dir, repeating=repeating)

    frame_rate = args.fps
    if args.verify:
        frame_rate = 200  # Override for verify to something way high
    frame_rate_controller = FrameRateController(frame_rate)

    if args.device:
        dmx_output = OVDMXOutput(args.device)
    elif args.simulator:
        # Do not import in main scope a that would require websocket 
        # dependencies which is not required for the normal mode.
        from .development.ws_output import WSDMXOutput
        dmx_output = WSDMXOutput()
    elif args.verify:
        dmx_output = DummyDMXOutput()
    else:
        raise ValueError('Unknown DMX ouput')

    # If the countdown is activated, run that first
    if countdown_to:
        countdown_gen = CountdownGenerator(countdown_to)
        for frame_index, frame in countdown_gen.frames():
            if frame_index % frame_rate == 0:
                print("Frame #{:3d} in {}".format(frame_index, countdown_gen))
            frame_rate_controller.next_frame()
            dmx_output.push_frame(frame)

        print("Countdown complete! Starting playlist!")

    # Create a Player object    
    player = Player(dmx_output, channel_mapping, frame_rate_controller)

    if loop_until:
        # Create a playlist with a single item
        print("Stopping loop at {}. Current time is {}".format(loop_until, datetime.now()))
        loop_playlist = Playlist(StringIO(str(Path(args.loop_pgm).absolute().resolve())), repeating=repeating)
        player.run_playlist(loop_playlist, stop_at=loop_until)

    player.run_playlist(playlist, single_step=args.single_step)



if __name__ == "__main__":
    main()
