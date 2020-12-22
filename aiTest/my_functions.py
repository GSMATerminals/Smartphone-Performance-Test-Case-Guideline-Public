import os, time
from PIL import Image, ImageDraw, ImageFont
from src.detect_benchi import benchi_detect
import json
import numpy as np
import cv2
from face_dlib.face_detection_new import once_detect

def determinant(A, B):
    #print(v1, v2, v3, v4)
    return (A[0]*B[1]-A[1]*B[0])

def point_mul(A, B):
    return A[0]*B[0]+A[1]*B[1]

def lenth_l(A):
    return (A[0]**2+A[1]**2)**(1/2)


# Compute point is inner of targets.
def inner_point(point1):
    filename = "./myguard.json"
    if not os.path.exists(filename):
        print("myguard.json is error!")    
    with open(filename, "r") as f:
        load_dict = json.load(f)
    points2 = np.array(load_dict["base1"])    

    a1 = [0, 0]
    a2 = point1
    num = 0
    result_point = []
    for j in range(len(points2)):
        if j < len(points2)-1:
            b1 = points2[j]
            b2 = points2[j+1]
        elif j == len(points2)-1:
            b1 = points2[j]
            b2 = points2[0]                
        else:
            print("Input points err!")
        result = cup_intersect(a1,a2,b1,b2)
        #print(result)
        if result[0] == 1:
            if result[1] not in result_point:
                result_point.append(result[1])
                num += 1
    if num%2 == 1:
        return True
    else:
        return False



