import typing

class DMXFrameSource:
    """ The base class for all sources of DMX frame data """

    def frames(self) -> typing.Generator[typing.Tuple[int, bytes], None, None]:
        """

        :return: a generator providing a tuple with the form [frame_number, frame_data]
        """
        raise NotImplementedError('The DMXFrameSource is an abstract class')


class DMXOutput:
    """ A DMX output device interface """

    def push_frame(self, frame: bytes):
        raise NotImplementedError('This is an abstract function and must be overridden')


class DummyDMXOutput(DMXOutput):
    """ A dummy DMX device which just discards all frames """

    def push_frame(self, frame: bytes):
        return

