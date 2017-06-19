#!/usr/bin/env python3

import svg.math
import svg.math.point


class Map:
    """ Map container class.
    """

    def __init__(self, name, region_points, bbox):
        """
            Initializer
            :param name: Map name to use.
            :param region_points: Dictionary mapping from region name to vertices.
            :param bbox: AABB around the map
        """
        self.__name = name
        self.__bbox = bbox
        self.__all_points = Map.__compute_points(region_points)
        self.__region_vertices = Map.__compute_region_vertices(region_points, self.__all_points)


    def get_json_dict(self):
        """
            :return:
        """
        return dict(name=self.__name,
                    vertices=self.__get_json_all_points(),
                    regions=self.__get_json_regions())

    def __get_json_regions(self):
        """
        Get JSON regions - list of region object
        :return:
        """
        result = []

        for region_name, vertex_indices in self.__region_vertices.items():
            result.append(dict(name=region_name,
                               vertexIndices=vertex_indices,
                               neighbouringRegions=self.__compute_neighbouring_regions(region_name)))

        return result

    def __compute_neighbouring_regions(self, region_name):

        neighbouring_regions = []

        # vertex indices for the given region
        region_indices = self.__region_vertices[region_name]
        for region, indices in self.__region_vertices.items():
            if region == region_name:
                continue  # same region - no check
            if Map.__is_neighbouring_region(region_indices, indices):
                neighbouring_regions.append(region)

        return neighbouring_regions

    @staticmethod
    def __is_neighbouring_region(region_one, region_two):

        for i in range(0, len(region_one)):
            for j in range(0, len(region_two)):
                if region_one[i] == region_two[j]:
                    # same indices on i and j, check if neighbouring are the same
                    if i > 0 and j > 0 and region_one[i-1] == region_two[j-1]:
                        return True
                    if i > 0 and j + 1 < len(region_two) and region_one[i-1] == region_two[j+1]:
                        return True
                    if i + 1 < len(region_one) and j > 0 and region_one[i+1] == region_two[j-1]:
                        return True
                    if i + 1 < len(region_one) and j + 1 < len(region_two) and region_one[i+1] == region_two[j+1]:
                        return True

        return False

    def __get_json_all_points(self):

        # enlarge bbox points
        min_bbox = self.__bbox.min + svg.math.point.Point(-10.0, -10.0)
        max_bbox = self.__bbox.max + svg.math.point.Point(10.0, 10.0)

        width = max_bbox.x - min_bbox.x
        height = max_bbox.y - min_bbox.y

        # translate the points into (0, 0) direction with some pix offset to the border
        tmatrix = svg.math.translate(-1 * min_bbox.x, -1 * min_bbox.y)
        points = map(lambda p: tmatrix.apply(p), self.__all_points)

        # multiply y-coords with -1 to ensure it's libgdx coordinate system
        return list(map(lambda p: dict(x=(p.x / width), y=((-1 * (p.y - height)) / height)), points))

    @staticmethod
    def __compute_points(region_points):
        """
        Compute region points
        :param region_points:
        :return:
        """
        result = []

        for k, points in region_points.items():
            for point in points:
                if point not in result:
                    result.append(point)


        return result

    @staticmethod
    def __compute_region_vertices(region_points, unique_points):
        """
        Compute vertex indices for the regions.
        :param region_points:
        :param unique_points:
        :return:
        """
        result = {}

        for region_name, points in region_points.items():
            result[region_name] = list(map(lambda p: unique_points.index(p), points))

        return result
