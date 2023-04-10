from . import ws
from ..base import DMXOutput



class WSDMXOutput(DMXOutput):
    """ A fake DMX device which sends frames to listening simulators """

    def __init__(self):
        ws.start()

    def push_frame(self, frame: bytes):
        ws.broadcast(frame)