# -*- coding: utf-8 -*-
"""
Created on Wed Mar  4 18:45:51 2020

@author: 13373
"""

import json

# '/opt/intel/computer_vision_sdk/inference_engine/lib/ubuntu_16.04/intel64/libcpu_extension_avx2.so'
# 'location": 'C:\\Program Files (x86)\\IntelSWTools\\openvino\\inference_engine\\bin\\intel64\\Release\\cpu_extension_avx2.dll'

myset = {"location": '/opt/intel/openvino/inference_engine/lib/intel64/libcpu_extension_avx2.so',
          "basefile1": "./model_fp/my_FP32",
          "basefile2": "./model_fp/my_FP16",
          "devices": 'GPU'
        }


filename = "mysetting"


def save_json():
    with open(filename, "w") as f:
        json.dump(myset, f)
        print("保存成功！")

def read_json():
    with open(filename, "r") as f:
        load_dict = json.load(f)
        print("导入成功！")
    return load_dict
    
if __name__ == "__main__":
    save_json()
#    data = read_json()
#    print(data["location"])
    
        
