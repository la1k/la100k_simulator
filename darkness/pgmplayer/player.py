from pgmplayer.channels import ChannelMap
from pgmplayer.frame_rate import FrameRateController
from .pgm import PGMReader
from .base import DMXOutput

from typing import Optional
from datetime import datetime


class Player:

    def __init__(self, dmx_output: DMXOutput,  channel_mapping: ChannelMap, frame_rate_controller = FrameRateController):
        self.dmx_output = dmx_output
        self.channel_mapping = channel_mapping
        self.frame_rate_controller = frame_rate_controller

    def run_playlist(self, playlist, *, single_step=False, stop_at: Optional[datetime] = None):
        # Iterate the playlist and play the files
        for playlist_line_number, pgm_path in playlist.entry_generator():
            try:
                print("Starting pgm file from line #{}: {}".format(playlist_line_number, pgm_path))
                pgm_reader = PGMReader(pgm_path, channel_map=self.channel_mapping)
                self.frame_rate_controller.reset()

                for frame_index, frame in pgm_reader.frames():
                    if stop_at and datetime.now() > stop_at:
                        print("Stopping playlist as stop_at was reached!")
                        return
                    
                    if frame_index % self.frame_rate_controller.frame_rate == 0:
                        print("Frame #{:3d} in {}".format(frame_index, pgm_reader), end="")
                        if stop_at:
                            dt = stop_at - datetime.now()
                            seconds = dt.total_seconds()
                            minutes = int(seconds / 60)
                            seconds -= minutes * 60
                            print(" remaining: {:2d} minutes and {:.2f} seconds)".format(minutes, seconds), end="")

                        print("")
                    if single_step:
                        ans = input("Single stepping. Proceed to frame #{} Y/y/n/q [or enter]".format(frame_index)).strip()
                        if ans != '' and ans not in 'Yy':
                            print("Exiting on request!")
                            exit(0)
                    else:
                        self.frame_rate_controller.next_frame()
                    self.dmx_output.push_frame(frame)
            except Exception as e:
                print("Exception when playing pgm file {}: {}".format(pgm_path, e))