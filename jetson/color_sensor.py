import cv2

def get_color(cap):
  print("getting color")

  val, frame = cap.read()
  print(val)

  cv2.imshow("frame", frame)
