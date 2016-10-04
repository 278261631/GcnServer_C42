# -*- coding: utf-8 -*-
'''
Created on 

@author: Administrator
'''
import celestial as tranFormer
import time
import numpy
import angles
import math
import os,sys
import clock
# raInDegrees=sys.argv[1]
# decIndegrees=sys.argv[2]
# print raInDegrees
# print decIndegrees

# raInRadians=math.radians(numpy.float(raInDegrees));
# decInRadians=math.radians(numpy.float(decIndegrees));
# hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), raInRadians,decInRadians) #sun
lst = clock.gps_to_lst(time.time(), 87.5)
altitudeInDeg=90
azimuthIndeg=180
ra,dec = tranFormer.horizontal_to_equatorial(43.8, lst, math.radians(numpy.float(altitudeInDeg)), math.radians(numpy.float(azimuthIndeg)))



# 
# print repr(ra)+","+repr(dec);
# print repr(ra)+","+repr(dec)+","+repr((24*numpy.float(ra))/(2*math.pi))+","+repr(math.degrees(numpy.float(dec)));
print repr((24*numpy.float(ra))/(2*math.pi))+","+repr(math.degrees(numpy.float(dec)));

    
    
