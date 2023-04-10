import typing
from pathlib import Path

from .base import DMXFrameSource
from .channels import ChannelMap

class PGMReader(DMXFrameSource):
    """ A PGM file format parser """

    def __init__(self, path_or_file: typing.Union[typing.TextIO, str, Path], *,
                 channel_map: typing.Optional[ChannelMap] = None):
        if isinstance(path_or_file, (str, Path)):
            self.file = open(str(path_or_file), 'r')
        else:
            self.file = path_or_file

        self.channel_map = channel_map

        file_type = self.file.readline().strip()
        if file_type != 'P2':
            raise IOError('The pgm file {} is not of the PGMv2 format'.format(self.file))

        dimensions = self.file.readline().strip().split(' ')
        if len(dimensions) != 2:
            raise IOError(
                'The second line in the PGM file {} should be [channel_count] [frame_count]'.format(self.file))
        if any((not x.isnumeric() for x in dimensions)):
            raise IOError(
                'The second line in the PGM file {} should be [channel_count] [frame_count] any be numeric'.format(
                    self.file))

        self.channel_count = int(dimensions[0])
        self.frame_count = int(dimensions[1])

        max_channel_value = self.file.readline().strip()
        if not max_channel_value.isnumeric():
            raise IOError('The third line in the PGM file {} should be [max_channel_value]'.format(self.file))
        if int(max_channel_value) != 255:
            raise NotImplementedError('Only a max channel value of 255 is currently supported')

        self.current_frame_index = 0

    def read_frame(self) -> bytes:
        raw_frame_data = self.file.readline()
        if not raw_frame_data:
            raise EOFError('End of PGM file {}'.format(self.file))

        self.current_frame_index += 1

        raw_frame_channels = raw_frame_data.strip().split(' ')
        frame = bytearray(self.channel_count)
        for channel_index, channel_value in enumerate(raw_frame_channels):
            if channel_index >= self.channel_count:
                raise IOError('The PGM file {} has more than {} channels in frame {}'.format(self.file,
                                                                                             self.channel_count,
                                                                                             self.current_frame_index))
            channel_value = int(channel_value)
            if self.channel_map:
                mapped_channel = self.channel_map[channel_index]
                if mapped_channel >= 0:  # Only map valid channels
                    frame[mapped_channel] = channel_value
            else:
                # No channel map. Copy everything raw
                frame[channel_index] = channel_value

        return frame

    def frames(self) -> typing.Generator[typing.Tuple[int, bytes], None, None]:
        while True:
            try:
                frame = self.read_frame()
                yield self.current_frame_index, frame
            except EOFError:
                return

    def __str__(self):
        return "pgm file {}".format(self.file.name)

