#!/usr/bin/env python3

import argparse
import json

import svg.svg_map_parser
import svg.math.bbox
import map.map


def parse_arguments():
    """ Parse command line arguments.
        :return: Tuple containing input filename and output filename.
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("-i", "--input", help="SVG input for map generation", required=True, metavar="FILE", dest="input")
    parser.add_argument("-o", "--output", help="Map filename to generate", required=True, metavar="FILE", dest="output")
    parser.add_argument("-n", "--name", help="Map's name", required=True, dest="name")
    args = parser.parse_args()

    return args.input, args.output, args.name


def main():
    """ Main entry point for the map generator.
        :return: None
    """
    (svg_filename, map_filename, name) = parse_arguments()
    region_points = svg.svg_map_parser.SvgMapParser(svg_filename).parse()
    for region, points in region_points.items():
        for p in points:
            pass
    bbox = svg.math.bbox.BBox(region_points)
    with open(map_filename, "w") as map_file:
        json.dump(map.map.Map(name, region_points, bbox).get_json_dict(), map_file, indent=4)


if __name__ == '__main__':
    main()
