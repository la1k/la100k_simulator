import time

class FrameRateController:
    """ A helper class to keep us at a given frame rate """

    def __init__(self, frame_rate: int):
        self.frame_rate = frame_rate
        self.frame_period = 1.0 / float(frame_rate)
        self.last_frame_time = time.time()

    def reset(self):
        self.last_frame_time = time.time()

    def next_frame(self):
        next_frame_time = self.last_frame_time + self.frame_period
        time_now = time.time()
        if next_frame_time < time_now:
            print(
                "WARNING! Can not keep up frame rate! Lagging behind {:.2f} seconds".format(time_now - next_frame_time))
        else:
            time.sleep(next_frame_time - time_now)
        self.last_frame_time = time.time()