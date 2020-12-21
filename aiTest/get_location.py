# -*- coding: utf-8 -*-
import cv2
import json

class Getxy:
    def __init__(self):
        self.track = []
        self.lis = []
        self.sum_lis = []
        self.myset = {}
        self.num = 1

    def click_on(self, event, x, y, flags, param):
        if event == cv2.EVENT_LBUTTONDOWN:
            rg = 30
            if x > 1280-rg:
                x = 1280
            if x < rg:
                x = 0
            if y > 720-rg:
                y = 720


            self.track.append([x, y])
            if len(self.lis) == 0:
                myfind = self.find_same(x, y, self.sum_lis)
                self.lis.append(myfind)
                print(self.lis)
            elif len(self.lis) > 0:
                for i in self.lis:
                    if -10<i[0]-x<10 and -10<i[1]-y<10:
                        if len(self.lis) < 3:
                            print("click error!please exit and restart!")
                        else:
                            self.myset["base{}".format(self.num)] = self.lis
                            self.sum_lis = self.sum_lis + self.lis
                            print(self.myset)
                            print(self.sum_lis)
                            self.lis = []
                            self.num += 1
                            break
                else:
                    myfind = self.find_same(x, y, self.sum_lis)
                    self.lis.append(myfind)
                    print(self.lis)
                         
    def show_xy(self, img, x, y):
        xy = "%d, %d" % (x, y)
        cv2.circle(img, (x, y), 5, (0, 0, 255), thickness=-1)
        cv2.putText(img, xy, (x, y), cv2.FONT_HERSHEY_PLAIN,
                        1.0, (0,0,0), thickness = 1)
        
    def get_xy_video(self, save_file, video_id=0):
        cv2.namedWindow("image")
        cv2.setMouseCallback("image", self.click_on)
        
        #可能需要修改
        cap = cv2.VideoCapture(video_id)
        #cap.set(cv2.CAP_PROP_FPS, 30)
        cap.set(cv2.CAP_PROP_FOURCC, cv2.VideoWriter.fourcc('M', 'J', 'P', 'G'))
        cap.set(3, 1280)
        cap.set(4, 720)
        while True:
            ret, img = cap.read()
            #print("====", img.shape)
            for i in self.track:
                x, y = i[0], i[1]
                self.show_xy(img, x, y)
                
            cv2.imshow("image", img)
            key = cv2.waitKey(33)
            if key == 27:
                break
            if key == ord(" "):
                self.save_xy(self.myset)           
        cv2.destroyAllWindows()
        
    def get_xy_picture(self, save_file, image_loction):
        cv2.namedWindow("image", 0)
        cv2.setMouseCallback("image", self.click_on)
        
        #可能需要修改
        #cap = cv2.VideoCapture(0)
        while True:
            img = cv2.imread(image_loction)
            #print("====", track)
            for i in self.track:
                x, y = i[0], i[1]
                self.show_xy(img, x, y)
                
            cv2.imshow("image", img)
            key = cv2.waitKey(33)
            if key == 27:
                break
            if key == ord(" "):
                self.save_xy(self.myset)           
        cv2.destroyAllWindows()
    
    def find_same(self, x, y, sum_lis):
        for j in sum_lis:
            if -10<j[0]-x<10 and -10<j[1]-y<10: 
                return j
        else:
            return [x, y]
                
    def save_xy(self, myset):
        with open(save_file, "w") as f:
            #indent非负整数表明换行，要是为None则表示存为1行
            json.dump(myset, f, indent=None)
            print("保存成功！")
    
if __name__ == "__main__":
    #======
    #img=cv2.imread('D:/test/image.jpg')
    save_file = "myguard.json"
    image_loction = "1.jpg"
    video_id = 0
    rtsp = "rtsp://admin:ad123456@192.168.199.220/Streaming/Channels/1"
    # rtsp = "rtsp://demo.easydss.com:10554/aidong_demo"
    obj = Getxy()
    obj.get_xy_video(save_file, rtsp)
    # obj.get_xy_video(save_file, video_id)
    # obj.get_xy_picture(save_file, image_loction)
    
    
    
    
    
    
    
