import typing
from pathlib import Path

class Playlist:
    """
    A class to read playlist files.

    A playlist file is simply a list of relative paths to the .pgm files to play.
    The default directory can be overwritten as an argument to the constructor.
    """

    def __init__(self,
                 path_or_file: typing.Union[typing.TextIO, str, Path],
                 base_dir: typing.Optional[Path] = None,
                 repeating: bool = True):
        self.directory = Path.cwd()
        self.repeating = repeating
        self.prev_pgm_path = None  # Keep the path of the last played file

        if isinstance(path_or_file, (str, Path)):
            self.file = open(str(path_or_file), 'r')
            # Default to the directory of the playlist if it is a real file
            self.directory = Path(path_or_file).absolute().parent

        else:
            self.file = path_or_file

        if base_dir:
            self.directory = base_dir

    def read_entry(self) -> typing.Optional[typing.Tuple[int, Path]]:
        # Seek to the start of the file (reload if changed)
        self.file.seek(0)
        lines = [line.strip() for line in self.file.readlines()]
        if not lines:
            raise ValueError("Playlist file {} is empty".format(self.file.name))

        # Find the previously playing file by it's name
        # This is done so that we can gracefully handle playlist changes where a new
        # pgm file is inserted earlier than the currently playing one
        # This comes at the cost that a playlist with the same pgm file more than once is not supported.
        prev_file_index = -1
        if self.prev_pgm_path:
            for index, path in enumerate(lines):
                if path == self.prev_pgm_path:
                    prev_file_index = index
                    break

        next_file_index = prev_file_index + 1  # This becomes 0 if the previous file was not found
        if next_file_index >= len(lines):
            # We have reached the end of the playlist. Revert to the first one
            next_file_index = 0

        # Update the
        self.prev_pgm_path = lines[next_file_index]

        path = Path(self.prev_pgm_path)
        if not path.is_absolute():
            path = Path(self.directory, path).resolve()

        return next_file_index + 1, path  # Convert to 1 indexed lines

    def entry_generator(self) -> typing.Generator[typing.Tuple[int, Path], None, None]:
        """
        :return: a generator providing pgm files to play in the form [line_number_in_playlist, path_to_pgm]
        """
        while True:
            entry = self.read_entry()
            if not entry:
                return
            yield entry