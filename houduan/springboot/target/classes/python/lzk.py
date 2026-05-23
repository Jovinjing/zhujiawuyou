
# -*- coding: utf-8 -*-
import oss2
import requests
import sys


import cv2
import numpy as np
import tempfile
import os
param1 = sys.argv[1]

def process_image(input_path, output_path):
    response = requests.get(input_path)
    img_array = np.asarray(bytearray(response.content), dtype=np.uint8)
    # 读取原图
    image = cv2.imdecode(img_array, cv2.IMREAD_COLOR)
    # image = cv2.imread(input_path)
    if image is None:
        raise FileNotFoundError(f"无法加载图片：{input_path}")

    # 创建全白图像
    white_image = np.ones_like(image) * 255

    # 转换为HSV颜色空间
    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # 定义红色的HSV范围（覆盖0°和180°附近）
    # lower_red1 = np.array([0, 120, 70])
    # upper_red1 = np.array([10, 255, 255])
    # lower_red2 = np.array([170, 120, 70])
    # upper_red2 = np.array([180, 255, 255])

    lower_red1 = np.array([0, 100, 100])
    upper_red1 = np.array([10, 255, 255])
    lower_red2 = np.array([160, 100, 100])
    upper_red2 = np.array([180, 255, 255])

    # 生成红色区域的掩膜
    mask1 = cv2.inRange(hsv, lower_red1, upper_red1)
    mask2 = cv2.inRange(hsv, lower_red2, upper_red2)
    mask = cv2.bitwise_or(mask1, mask2)

    # 形态学处理优化（增强闭合效果）
    # kernel = np.ones((5,5), np.uint8)
    # mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel, iterations=3)
    kernel = np.ones((7, 7), np.uint8)
    mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel, iterations=5)
    # 查找轮廓并填充最大区域
    contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    if contours:
        # 找到最大轮廓
        largest_contour = max(contours, key=cv2.contourArea)

        # 方案一：精确填充轮廓内部
        filled_mask = np.zeros_like(mask)
        cv2.drawContours(filled_mask, [largest_contour], -1, 255, cv2.FILLED)
        white_image[filled_mask == 255] = 0

        # 方案二：填充外接矩形（任选其一）
        # x, y, w, h = cv2.boundingRect(largest_contour)
        # cv2.rectangle(white_image, (x, y), (x+w, y+h), (0,0,0), thickness=cv2.FILLED)

    # 保存结果
    cv2.imwrite(output_path, white_image)

process_image(param1, "output1.png")


picurl="output1.png"
accessKeyId = 'LTAI5tBqztXsBthND9eYwutX'
accessKeySecret = 'hB0dPoIbglNd6SLpVTyZuYhGiwY13H'
auth = oss2.Auth(accessKeyId, accessKeySecret)
endpoint = 'oss-cn-guangzhou.aliyuncs.com'
objectName="blackwhite/"+picurl
# 填写Bucket名称。# yourBucketName填写存储空间名称。
bucket = oss2.Bucket(auth, endpoint, 'lzkai')
#
# # 填写网络流地址。
# input = requests.get(picurl).content
# 填写Object完整路径。Object完整路径中不能包含Bucket名称。
bucket.put_object_from_file(objectName, picurl)
fileLink = 'http://lzkai.'+endpoint+'/'+objectName
print(fileLink)
os.remove("output1.png")
