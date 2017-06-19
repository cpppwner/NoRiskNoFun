#!/usr/bin/env python3

import svg.math.point

class BBox:
    """ Axis aligned bounding box.
    """

    def __init__(self, region_points):
        """ Initializer.
        """
        self.__region_points = region_points
        self.__min = None
        self.__max = None

    @property
    def min(self):
        """ Get min point (in upper left corner).
            :return: Minimum point describing the bbox
        """
        if self.__min is None:
            self.__compute()
        return self.__min

    @property
    def max(self):
        """ Get max point (in lower right corner).
            :return: Maximum point describing the bbox
        """
        if self.__max is None:
            self.__compute()
        return self.__max

    def __compute(self):
        """ Compute min and max points describing aabb
            :return: None
        """
        for k, v in self.__region_points.items():
            for p in v:
                # init points first time (if none)
                if self.__min is None:
                    self.__min = svg.math.point.Point(p.x, p.y)
                if self.__max is None:
                    self.__max = svg.math.point.Point(p.x, p.y)
                self.__min.x = min(self.__min.x, p.x)
                self.__min.y = min(self.__min.y, p.y)
                self.__max.x = max(self.__max.x, p.x)
                self.__max.y = max(self.__max.y, p.y)
