#!/usr/bin/env bash
# Intended to test our physical boxes. Cycles all bulbs through Red, Green, Blue and White

BASEDIR=$(dirname "$0")
cd $BASEDIR/../ ; python3 -m pgmplayer --channel-mapping box_testing/mapping.txt --pgm box_testing/all_channel_test.pgm --device /dev/ttyACM0 --fps 1
