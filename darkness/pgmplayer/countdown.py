from datetime import datetime
import typing
import math

from .base import DMXFrameSource

class CountdownGenerator(DMXFrameSource):
    """
    A class for generating the countdown DMX frames automatically.

    The countdown is a 2-digit 7-segment display
    Each digit has the following segments

     A A A A
    F       B
    F       B
    F       B
    F       B
     G G G G
    E       C
    E       C
    E       C
    E       C
     D D D D

    The segment definition is defined as a dict with 1 indexed dmx channels. Example:
    [
    {"A": 1, "B": 2, "C": 3, "D": 4, "E": 5, "F": 6, "G": 7}, // Least significant digit.
    {"A": 8, "B": 9, "C": 10, "D": 11, "E": 12, "F": 13, "G": 14}, // Most significant digit.
    ]
    """
    PHYSICAL_CHANNELS = [
        {"A": 497, "B": 498, "C": 499, "D": 500, "E": 501, "F": 502, "G": 503},  # LSB
        {"A": 488, "B": 489, "C": 490, "D": 491, "E": 492, "F": 493, "G": 494},  # MSB
    ]

    NUMBER_SEGMENTS = {
        '0': 'ABCDEF',
        '1': 'BC',
        '2': 'ABGED',
        '3': 'ABGCD',
        '4': 'FGBC',
        '5': 'AFGCD',
        '6': 'AFEDCG',
        '7': 'ABC',
        '8': 'ABCDEFG',
        '9': 'ABCDGF'
    }

    def __init__(self, target_dt: datetime):
        self.target_dt = target_dt
        self.frame_counter = 0
        self.frame_buffer = bytearray(512)  # Preallocate a buffer for all our DMX data
        self.current_displaying_number = None

        for digit_index, segments in enumerate(self.PHYSICAL_CHANNELS):
            for segment_name in "ABCDEFG":
                segment_channel = int(segments[segment_name])
                segment_channel -= 1  # Convert to 0-indexing
                segments[segment_name] = segment_channel

    def zero_all_segments(self):
        for digit_index, segments in enumerate(self.PHYSICAL_CHANNELS):
            for segment_name, segment_channel in segments.items():
                self.frame_buffer[segment_channel] = 0

    def render_number(self, number: int):
        self.zero_all_segments()
        if number > 99 or number < 0:
            raise ValueError('Can not display values over 99 or below 0')

        self.current_displaying_number = number
        number_string = list(str(number))
        number_string.reverse()  # Lets handle the least significant digits first
        for digit_index, digit in enumerate(number_string):
            for seg in self.NUMBER_SEGMENTS[digit]:
                self.frame_buffer[self.PHYSICAL_CHANNELS[digit_index][seg]] = 255

    def frames(self) -> typing.Generator[typing.Tuple[int, bytes], None, None]:
        while True:
            current_time = datetime.now()
            diff_time = self.target_dt - current_time

            self.frame_counter += 1
            if self.target_dt < current_time:
                # We are done counting down - blank out all the segments and exit the generator
                self.zero_all_segments()
                self.current_displaying_number = None
                yield self.frame_counter, self.frame_buffer
                return

            seconds_remaining = int(math.ceil(diff_time.total_seconds()))
            minutes_remaining = int(seconds_remaining / 60)
            if minutes_remaining > 0:
                if minutes_remaining > 60:
                    self.zero_all_segments()
                else:
                    self.render_number(minutes_remaining)
            else:
                self.render_number(seconds_remaining)

            yield self.frame_counter, self.frame_buffer

    def __str__(self):
        dt = self.target_dt - datetime.now()
        seconds = dt.total_seconds()
        minutes = int(seconds / 60)
        seconds -= minutes * 60

        displaying = '--'
        if self.current_displaying_number:
            displaying = "{:2d}".format(self.current_displaying_number)

        return "CountdownGenerator(displaying: {}; remaining: {:2d} minutes and {:.2f} seconds)".format(
            displaying,
            minutes, seconds)

