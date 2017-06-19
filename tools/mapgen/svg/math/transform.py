#!/usr/bin/env python3

import svg.math.point

class Transform:
    """ Transform class, used to transform SVG points.
    """

    def __init__(self, matrix):
        """ Initializer.
            :param matrix: Transform matrix (3x2).
        """
        self.__matrix = matrix

    def apply(self, point):
        """ Transform a point according to the transformation matrix.
            :param point: The point to transform.
            :return: Transformed point - which is copy of the original
        """
        x = self.__matrix[0] * point.x + self.__matrix[2] * point.y + self.__matrix[4]
        y = self.__matrix[1] * point.x + self.__matrix[3] * point.y + self.__matrix[5]

        return svg.math.point.Point(x, y)

