channels = 512
fps = 20
max_value = 255
print "P2"
print channels, channels * fps
print max_value
for i in range(channels):
    values = [0] * channels
    values[i] = max_value
    row = " ".join(str(v) for v in values)
    for f in range(fps):
        print row