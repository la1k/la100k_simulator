import typing
from pathlib import Path

class ChannelMap:
    """ A class that loads and handles channel mapping """

    def __init__(self, path_or_file: typing.Union[typing.TextIO, str, Path]):
        if isinstance(path_or_file, (str, Path)):
            file = open(str(path_or_file), 'r')
        else:
            file = path_or_file

        self._map = {}  # type: dict[int, int]
        for line_number, line in enumerate(file.readlines()):
            line = line.strip()
            parts = line.split(' ')
            if len(parts) == 1:
                parts = line.split('\t')

            if len(parts) != 2 or any(not x.isnumeric() for x in parts):
                raise IOError('The channel mapping file {} is malformed on line {}'.format(file, line_number))

            map_from = int(parts[0]) - 1  # Convert from 1-indexing to 0-indexing
            map_to = int(parts[1]) - 1  # Convert from 1-indexing to 0-indexing

            if map_from in self._map.keys():
                raise IOError('The channel mapping file {} contains duplicate mapping for the channel {}'
                              .format(file, map_from))

            self._map[map_from] = map_to

        if len(set(self._map.values())) != len(self._map.values()):
            raise IOError('The channel mapping contains duplicate mappings to the same channel')

    def __getitem__(self, item):
        if type(item) is not int:
            raise ValueError('The channel mapping must be indexed with ints')
        if item in self._map:
            return self._map[item]
        return -1  # Not found
