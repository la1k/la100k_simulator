# Given a pattern file (which maps each bulb ID to a unique logical DMX channel)
# and a box file (which maps each DMX box and LED to a unique bulb ID), this script
# generates a systematic mapping from each logical DMX channel to a physical DMX channel.
# Given a 1-indexed `box`, a 1-indexed `led` within that box (between 1 and 6),
# and a color that is either red (0), green (1), or blue (2), the physical
# channel will be assigned according to this formula:
# (box - 1) * 18 + (led - 1) * 3 + (color + 2)
# (TODO: Remember why we add 2...)

import argparse
import json


def main():
	parser = argparse.ArgumentParser()
	parser.add_argument("scene", type=argparse.FileType('rb'), help="The json scene file that the simulator uses.")
	parser.add_argument("boxes", type=argparse.FileType('r'), help="Box file containg the which LEDs a given box controls")
	args = parser.parse_args()

	bulbs = load_bulbs(args.scene)
	
	# Example boxes file looks like this
	# 1.1 0
	# 1.2 1
	# 1.3 2
	# 1.4 3
	# 1.5 4
	# 1.6 9
 
	# 2.1 5
	# 2.2 6
	# 2.3 7
	# 2.4 8
	# 
	# 3.1 10
	# 3.2 11
	# 3.3 12
	# 3.4 13
	# 3.5 14

	seenBoxItems = set()
	for line in args.boxes.readlines():
		items = line.split()
		if not items or items[1] == '-':
			continue
		boxItems = items[0].split('.')
		box = int(boxItems[0])
		boxLed = int(boxItems[1])
		boxItem = (box, boxLed)
		if boxItem in seenBoxItems:
			print("Duplicate box:", boxItem)
			exit(1)
		bulb = int(items[1])
		rgb = bulbs.get(bulb)
		if not rgb:
			print("No bulb with id {}".format(bulb))
			exit(1)
		for color in range(len(rgb)):
			physical = (box - 1) * 18 + (boxLed - 1) * 3 + (color + 2)
			print('%d\t%d' % (rgb[color], physical))

	# The two seven-segment displays that make up the counter
	# TODO place these in a different range as we have duplicates for UKA-21
	# for display in range(2):
	# 	for segment in range(7):
	# 		logical = display * 7 + segment + 101
	# 		physical = display * 9 + segment + 488
	# 		print("{:d}\t{:d}".format(logical, physical))
	

def load_bulbs(scene_file):	
	"""
	Returns a dictionary that maps from bulb_id to an array containing [r, g, b] channel numbers
	"""
	bulbs = {}
	scene = json.load(scene_file)
	for group in scene["groups"]:
		for bulb in group.get("bulbs", []):
			# Assume default array based definition.
			# Once we add more bulb types this must be changes
			bulbs[bulb[0]] = [channel for channel in bulb[3:6]]
	return bulbs




if __name__ == "__main__":
	main()

