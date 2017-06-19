#!/usr/bin/env python3


class Point:
    """ 2D point.
    """

    def __init__(self, x=None, y=None):
        """ Initializer.
            :param x: x-coordinate
            :param y: y-coordinate
        """
        self.__x = x
        self.__y = y

    @property
    def x(self):
        """ Get x-coordinate of point.
            :return: x-coordinate
        """
        return self.__x

    @x.setter
    def x(self, value):
        """ Set the x-coordinate of this point.
            :param value: New x-coordinate value
            :return: None
        """
        self.__x = value

    @property
    def y(self):
        """ Get y-coordinate of point.
            :return: y-coordinate
        """
        return self.__y

    @y.setter
    def y(self, value):
        """ Set the y-coordinate of this point.
            :param value: New y-coordinate value
            :return: None
        """
        self.__y = value

    def __eq__(self, other):
        """ Checks if this point is equal to other.
            :param other:
            :return: True if equal, False otherwise.
        """
        return (abs(self.x - other.x) < 10E-6) and (abs(self.y - other.y) < 10E-6)

    def __ne__(self, other):
        return not self.__eq__(other)

    def __str__(self):
        return "({}, {})".format(self.x, self.y)

    def __unicode__(self):
        return "({}, {})".format(self.x, self.y)

    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

    def __mul__(self, other):
        return Point(self.x * other, self.y * other)

    def __hash__(self):
        return hash((self.y, self.y))
