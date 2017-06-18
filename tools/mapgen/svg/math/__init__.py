#!/usr/bin/env python3

import svg.math.transform

def matrix(a, b, c, d, e, f):
    """ Create matrix transform.
        :return: Transform.
    """
    return svg.math.transform.Transform([a, b, c, d, e, f])

def translate(x, y=0):
    """ Create translate transform.
        :return: Transform.
    """
    return matrix(1, 0, 0, 1, x, y)
