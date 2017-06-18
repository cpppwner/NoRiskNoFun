#!/usr/bin/env python3

import re
import sys
import xml.dom.minidom

import svg.math.point

def _parse_point(data):
    if ',' in data:
        # woah, we got two positive numbers
        coords = list(map(float, data.split(',')))
        return svg.math.point.Point(coords[0], coords[1])
    else:
        # x-coord is also negative, search for split index, since second index is negative too
        if data[0] == '-':
            index = data.find('-', 1)
            assert (index > 1)
        else:
            index = data.find('-')

        coords = [float(data[:index]), float(data[index:])]
        return svg.math.point.Point(coords[0], coords[1])

def _move_to(data, points):
    """ Move to point.
    """
    return _parse_point(data)

def _line_to(data, points):
    """ Line to next point
    """
    return _parse_point(data)

def _line_to_rel(data, points):
    """ Line to next point realtive (dx, dy)
    """
    return points[-1] + _parse_point(data)

def _horizontal_line_to(data, points):
    """ horizontal line to given point.
    """
    return svg.math.point.Point(float(data), points[-1].y)

def _horizontal_line_to_rel(data, points):
    """ horizontal line to given point relative (dx, dy).
    """
    return svg.math.point.Point(points[-1].x + float(data), points[-1].y)

def _vertical_line_to(data, points):
    """ vertical line to given point.
    """
    return svg.math.point.Point(points[-1].x, float(data))

def _vertical_line_to_rel(data, points):
    """ vertical line to given point relative (dx, dy).
    """
    return svg.math.point.Point(points[-1].x, points[-1].y + float(data))


def _close_path(data, points):
    if points[0] == points[-1]:
        return None
    return points[0]


_SVG_COMMANDS = {
    'M': _move_to,
    'L': _line_to,
    'l': _line_to_rel,
    'H': _horizontal_line_to,
    'h': _horizontal_line_to_rel,
    'V': _vertical_line_to,
    'v': _vertical_line_to_rel,
    'z': _close_path
}

_SVG_COMMANDS_PATTERN = re.compile(str.join('|', _SVG_COMMANDS.keys()))


class SvgMapParser:
    """ Map parser class.
    """

    def __init__(self, svg_filename):
        """ Initializer.
            :param svg_filename: Filename of the SVG from where to parse the map.
        """
        self.__svg_filename = svg_filename


    def parse(self):
        """ Parse SVG provided in initializer.
            :return:
        """
        regions_points_map = {}
        document = xml.dom.minidom.parse(self.__svg_filename)
        region_element = SvgMapParser.get_region_element(document)
        tmatrix = SvgMapParser.get_transform(region_element)
        for element in filter(lambda n: n.nodeType == xml.dom.Node.ELEMENT_NODE, region_element.childNodes):
            regions_points_map[element.attributes["id"].value] = SvgMapParser.parse_points(element, tmatrix)

        return regions_points_map

    @staticmethod
    def get_region_element(document):
        elements = document.getElementsByTagName("g")
        elements = list(filter(lambda e: "id" in e.attributes and e.attributes["id"].value == "Bezirke", elements))
        if len(elements) != 1:
            sys.stderr.write('Expected element with attribute name "id" and value "Bezirke" not found\n')
            sys.exit(1)
        return elements[0]

    @staticmethod
    def get_transform(element):
        transform_str = "svg.math." + element.attributes["transform"].value
        result = eval(transform_str)
        return result

    @staticmethod
    def parse_points(element, tmatrix):
        if element.tagName == "path":
            return SvgMapParser.parse_path(element, tmatrix)
        elif element.tagName == "polygon":
            return SvgMapParser.parse_polygon(element, tmatrix)
        else:
            sys.stderr.write("Not supported tag {}\n".format(element.tagName))
            sys.exit(1)

    @staticmethod
    def parse_polygon(element, tmatrix):
        values = list(map(lambda s: list(map(lambda x: float(x), s.split(","))), element.attributes["points"].value.strip().split()))
        result = list(map(lambda p: tmatrix.apply(svg.math.point.Point(p[0], p[1])), values))

        return result

    @staticmethod
    def parse_path(element, tmatrix):
        commands = element.attributes["d"].value.strip()
        if commands[0] not in _SVG_COMMANDS.keys():
            sys.stderr.write("SVG does not start with command\n")
            sys.exit(1)

        points = []
        i = 0
        while i < len(commands):
            match = _SVG_COMMANDS_PATTERN.search(commands, i + 1)
            next_index = match.start() if match is not None else len(commands)
            data = commands[i + 1: next_index]
            point = _SVG_COMMANDS[commands[i]](data, points)
            if point is not None:
                points.append(point)
            i = next_index

        return list(map(lambda p: tmatrix.apply(p), points))

