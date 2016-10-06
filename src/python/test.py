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
# print time.time()
# localtime = time.asctime( time.localtime(time.time()) )
# print "本地时间为 :", localtime
# print time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()) 
# hor = tranFormer.equatorial_to_horizontal(latitude, longitude, timestamp, right_ascension, declination)
# raInDegrees=37.5
# decIndegrees=89.25
raInDegrees=sys.argv[1]
decIndegrees=sys.argv[2]
# print raInDegrees
# print decIndegrees
# raInDegrees=172.5
# decIndegrees=3.66
# numpy.float(raInDegrees)
# numpy.float(decIndegrees)
raInRadians=math.radians(numpy.float(raInDegrees));
decInRadians=math.radians(numpy.float(decIndegrees));
# print 'Ra In Radians    ' +repr(raInRadians)
# print 'Dec In Radians    '+ repr(decInRadians)
# hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), raInDegrees,decIndegrees) #sun
# hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), raInRadians,decInRadians) #sun
hor = tranFormer.equatorial_to_horizontal(43.4710372, 87.1775888, time.time(), raInRadians,decInRadians) #sun
# hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), 285.6, 18) #moon


print repr(hor[0])+","+repr(hor[1])+","+repr(hor[2])+","+repr(hor[3]);
# print numpy.rad2deg(hor[0])
# print numpy.rad2deg(hor[1]) 
# print angles.radians_to_hours(hor[0])
# print angles.radians_to_hours(hor[1])
# print hor.zenith        #子午线？位置？ 中天
# print hor.azimuth        # 方位角
    
    
