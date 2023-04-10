import argparse
import re
import logging
import sys
import types
import typing
import json
from collections import OrderedDict
from shapely.geometry import Point, Polygon


logger = logging.getLogger(__name__)

class Bulb:
    def __init__(self, id: int, p: Point, channels: tuple):
        self.id = id
        self.point = p
        self.channels = channels

class Group:
    def __init__(self, polygon: Polygon):
        self.polygon = polygon
        self.bulbs = []  # type: typing.List[Point]
    
    def add(self, bulb: Bulb):
        self.bulbs.append(bulb)

class Background:
    def __init__(self, size: Point, texture: str):
        self.size = size
        self.texture = texture

class Parser:
    PATTERN_BULB = re.compile(r"^([0-9]+)\s+\((-?[0-9]+\.?[0-9]*).\s+(-?[0-9]+\.?[0-9]*)\)\s+R\s+([0-9]+)\s([0-9]+)\s([0-9]+)$")
    PATTERN_ALU_POINT = re.compile(r"^\((-?[0-9]+\.?[0-9]*).\s+(-?[0-9]+\.?[0-9]*)\)$")
    PATTERN_BACKGROUND = re.compile(r"^\((-?[0-9]+\.?[0-9]*).\s+(-?[0-9]+\.?[0-9]*).\s+(-?[0-9]+\.?[0-9]*).\s+(-?[0-9]+\.?[0-9]*)\)$")

    def __init__(self):
        self.polygons = []
        self.bulbs = {}
        self.background = None  # type: Background

    def parse(self, input_file: typing.TextIO) -> bool:
        for line in input_file.readlines():
            line = line.rstrip()
            
            if line == "":
                # skip empty lines
                continue
            
            expression = line.split(" ")[0]

            if expression == "OFFSET":
                if line != "OFFSET 0 0":
                    logger.error("Offset expression not yet supported")
            elif expression == "SCALE":
                if line != "SCALE 1 1":
                    logger.error("Scale expression not yet supported")
            elif expression == "BACKGROUND":
                if not self._parse_background(line):
                    return False
            elif expression == "ALU":
                if not self._parse_alu(line):
                    return False
            else:
                # Assume bulb definition if it starts with a number
                try:
                    int(expression)
                except ValueError:
                    logger.error(f"Unknown expression: \"{expression}\"")
                    return False
                
                if not self._parse_bulb(line):
                    return False

        return True
    
    def _parse_bulb(self, line: str) -> bool:             
        match = Parser.PATTERN_BULB.match(line)
        if not match:
            logger.error(f"Malformed bulb definition \"{line}\"")
            return False
        
        # Group 0 is the complete match
        # Group 1 is the bulb number (already parsed)
        bulb_num = int(match.group(1))
        if bulb_num in self.bulbs:
            logger.warning(f"Redefinition of bulb #{bulb_num}")
        pos_x = float(match.group(2))
        pos_y = -float(match.group(3)) # Invert the axis such that up is positive
        chan_r = int(match.group(4))
        chan_g = int(match.group(5))
        chan_b = int(match.group(6))
        self.bulbs[bulb_num] = Bulb(bulb_num, Point(pos_x, pos_y), (chan_r, chan_g, chan_b))
        return True

    def _parse_alu(self, line: str) -> bool:
        polygon_def = [] # first element will be list of points for the outline, the next ones will be holes in the polygon
        points = []  # list of points containing the current outline or hole being worked on

        point_defs = line.lstrip("ALU ").split(";")
        for point_index, point_def in enumerate(point_defs):
            point_def = point_def.strip()
            if point_def == "":
                # Ignore empty points (likely the last point has a trailing ;)
                continue
            if point_def == "-":
                # Defines the start of a new alu sub region
                if len(points) < 3:
                    logger.error(f"ALU definition contains less than 3 points. \"{line}\"")
                # Append to the point definition and start over
                polygon_def.append(points)
                points = []
                continue               

            match = Parser.PATTERN_ALU_POINT.match(point_def)
            if not match:
                logger.error(f"Malformed ALU definition \"{line}\"\nOffending point definition was #{point_index}: \"{point_def}\"")
                return False
            points.append((float(match.group(1)), -float(match.group(2)))) # Invert the axis such that up is positive

        if len(points) < 3:
            logger.error(f"ALU definition contains less than 3 points. \"{line}\"")
        
        polygon_def.append(points)

        if len(polygon_def) == 1:
            self.polygons.append(Polygon(polygon_def[0]))
        else:
            # Also include the holes
            # Figure out if one polygon contains all the others, this is the outline
            for outline_index in range(len(polygon_def)):
                outline = Polygon(polygon_def[outline_index])
                
                # Check if all the other polygons is inside this one
                inside = True
                holes = []
                for i in range(len(polygon_def)):
                    if i == outline_index:
                        continue # This is the outline itself
                    p = Polygon(polygon_def[i])
                    if not outline.contains(p):
                        inside = False
                        break
                    holes.append(polygon_def[i])
                if not inside:
                    continue
                # all holes are inside this one
                self.polygons.append(Polygon(polygon_def[outline_index], holes))
                break

        return True

    def _parse_background(self, line: str) -> bool:
        pdata, texture = line.lstrip("BACKGROUND ").split(";")[:2]
        texture = texture.strip()
        match = Parser.PATTERN_BACKGROUND.match(pdata)
        if not match:
            logger.error(f"Malformed BACKGROUND definition \"{line}\"")
            return False
        size = Point(float(match.group(3)), float(match.group(4)))
        self.background = Background(size, texture)
        return True



class BulbGrouper:        
    def __init__(self):
        self.groups = []  # type: typing.List[BulbGrouper.Group]
        self.floating_bulbs = []  # type: typing.Dict[Point]
    
    def add_polygons(self, polygons: typing.List[Polygon]):
        for p in polygons:
            self.groups.append(Group(p))
        
        # Readd any floating bulbs to see if they fit in any of the new groups
        floating = self.floating_bulbs
        self.floating_bulbs = []
        self.add_points(floating)
    
    def add_points(self, bulbs: typing.List[Bulb]):
        for bulb in bulbs:
            added = False
            for group in self.groups:
                if group.polygon.contains(bulb.point):
                    group.add(bulb)
                    added = True
                    break
            if not added:
                self.floating_bulbs.append(bulb)
        

class NewFormat:
    def __init__(self, groups: typing.List[Group], floating_bulbs: typing.List[Bulb], background: typing.Optional[Background]):
        self.groups = groups
        self.floating_bulbs = floating_bulbs
        self.background = background

    def generate(self) -> OrderedDict:
        out = OrderedDict()
        out["format_versin"] = "v0.1.0"

        if self.background:
            bg = out["background"] = OrderedDict()
            bg["size"] = FlattenedList(round(self.background.size.x, 4), round(self.background.size.y, 4))
            bg["texture"] = "background.png"  # ignore the actual value and always call it background.png

        out["groups"] = []
        out["groups"].append(self._generate_floating_bulb_group())
        for group in self.groups:
            out["groups"].append(self._generate_group(group))
        
        return out
    
    def _generate_bulb(self, bulb: Bulb, short_format=True) -> typing.Union[typing.List, OrderedDict]:
        if short_format:
            out = FlattenedList(bulb.id, round(bulb.point.x, 4), round(bulb.point.y, 4), *bulb.channels)
            return out

        out = OrderedDict()
        out["id"] = bulb.id
        out["pos"] = FlattenedList(bulb.point.x, bulb.point.y)
        out["channels"] = bulb.channels
        return out

    def _generate_floating_bulb_group(self) -> OrderedDict:
        out = OrderedDict()
        out["pos"] = FlattenedList(0.0, 0.0)

        out["bulbs"] = [self._generate_bulb(b) for b in self.floating_bulbs]
        return out

    def _normalize_path(self, path, base_x, base_y, ndigits=4):
        """
        Subtracts the base position from all the elements,
        rounds them of to a fixed precision, and
        removes duplicates
        """
        output = []
        last_p_rounded = None
        for p in path:
            p_rounded = [round(p[0]-base_x, ndigits), round(p[1]-base_y, ndigits)]
            if last_p_rounded == p_rounded:
                continue
            last_p_rounded = p_rounded
            output.append(p_rounded)
        
        if output[0] == output[-1]:
            output = output[:-1]

        return output

    def _generate_group(self, group: Group) -> OrderedDict:
        out = OrderedDict()
        # Calculate the bounding box for the polygon and use that as reference for the bulbs
        bounds = group.polygon.bounds
        x = bounds[0]  # minX
        y = bounds[1]  # minY
        out["pos"] = FlattenedList(x, y)

        out_alu = out["alu"] = OrderedDict()
        out_alu["outline"] = [FlattenedList(*p) for p in self._normalize_path(group.polygon.exterior.coords, x, y)]
        out_alu_holes = out_alu["holes"] = []
        for hole in group.polygon.interiors:
            out_alu_holes.append([FlattenedList(*p) for p in self._normalize_path(hole.coords, x, y)])

        out_bulbs = out["bulbs"] = []
        for bulb in group.bulbs:
            b =  Bulb(bulb.id, Point(bulb.point.x-x, bulb.point.y-y), bulb.channels)
            out_bulbs.append(self._generate_bulb(b))

        return out

class FlattenedList:
    _list = None
    def __init__(self, *args):
        if len(args) == 1 and isinstance(args[0], types.GeneratorType):
            self._list = [x for x in args[0]]
        else:
            self._list = [x for x in args]

class CustomJSONEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, FlattenedList):
            return "##<{}>##".format(o._list)

def main():
    logging.basicConfig(stream=sys.stderr, level=logging.DEBUG, format='[%(asctime)s] {%(filename)s:%(lineno)d} %(levelname)s - %(message)s')

    parser = argparse.ArgumentParser("convert old pattern files to the new JSON format")
    parser.add_argument("input", type=argparse.FileType('r'))
    parser.add_argument("output", type=argparse.FileType('w'))

    args = parser.parse_args()

    logger.info("Parsing input file")
    format_parser = Parser()
    if not format_parser.parse(args.input):
        logger.error("Failed to parse input file. Exiting...")
        exit(1)
    
    logger.info("Figuring out which bulbs belongs to which letter")
    grouper = BulbGrouper()
    grouper.add_polygons(format_parser.polygons)
    grouper.add_points(format_parser.bulbs.values())

    logger.info("Generating output")
    nf = NewFormat(grouper.groups, grouper.floating_bulbs, format_parser.background)
    nf_def = nf.generate()
    
    # Dirty hack for generating "nice" short arrays on one line
    args.output.write(json.dumps(nf_def, indent="  ", cls=CustomJSONEncoder).replace('"##<', "").replace('>##"', ""))

    logger.info("Done")
    

    
    

if __name__ == "__main__":
    main()
