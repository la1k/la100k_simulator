import typing
import termios
import struct
from pathlib import Path


from .base import DMXOutput



class OVDMXOutput(DMXOutput):
    """ The OVDMX device over a USB CDC serial connection

     The format of the packet is as such
     struct {
          uint8_t magic[2];
          uint8_t type;
          uint16_t data_length;
          uint8_t data[DMX_UNIVERSE_SIZE]; // 512
          uint16_t crc;
     } dmx_packet;
    """
    OFFSET_MAGIC = 0
    OFFSET_TYPE = 2
    OFFSET_DATA_LENGTH = 3
    OFFSET_DATA = 5
    DATA_LENGTH = 512
    OFFSET_CRC = OFFSET_DATA + DATA_LENGTH

    def __init__(self, device: typing.Union[str, Path]):
        self.dev = open(device, 'wb')
        if self.dev.isatty():
            # Make the terminal raw. That is disable everything related to automatic echo, \n -> \r\n
            # This does the same as the `cfmakeraw` function in glibc
            attr = termios.tcgetattr(self.dev.fileno())
            c_iflag = 0
            c_oflag = 1
            c_cflag = 2
            c_lflag = 3
            c_ispeed = 4
            c_ospeed = 5

            attr[c_iflag] = attr[c_iflag] & ~(termios.IGNBRK | termios.BRKINT | termios.PARMRK | termios.ISTRIP
                                              | termios.INLCR | termios.IGNCR | termios.ICRNL | termios.IXON)
            attr[c_oflag] = attr[c_oflag] & ~termios.OPOST
            attr[c_lflag] = attr[c_lflag] & ~(termios.ECHO | termios.ECHONL | termios.ICANON | termios.ISIG
                                              | termios.IEXTEN)
            attr[c_cflag] = attr[c_cflag] & ~(termios.CSIZE | termios.PARENB)
            attr[c_cflag] = attr[c_cflag] | termios.CS8

            # Set the speed to something high if we are testing this on some device that is not a real OVDMX device,
            # such as a USB to serial converter
            attr[c_ispeed] = termios.B1152000
            attr[c_ospeed] = termios.B1152000

            # Save the attributes
            termios.tcsetattr(self.dev.fileno(), termios.TCSANOW, attr)

        # Preallocate the packet and set static values
        self.usb_packet = bytearray(2 + 1 + 2 + 512 + 2)
        self.usb_packet[self.OFFSET_MAGIC:self.OFFSET_MAGIC + 2] = b'OV'
        self.usb_packet[self.OFFSET_TYPE] = ord('D')
        self.usb_packet[self.OFFSET_DATA_LENGTH:self.OFFSET_DATA_LENGTH + 2] = struct.pack('!H', 512)
        self.usb_packet[self.OFFSET_CRC:self.OFFSET_CRC + 2] = b'\0\0'

    def push_frame(self, frame: bytes):
        self.usb_packet[self.OFFSET_DATA:self.OFFSET_DATA + self.DATA_LENGTH] = frame[0:self.DATA_LENGTH]
        self.dev.write(self.usb_packet)
        self.dev.flush()