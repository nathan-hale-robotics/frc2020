import cv2

import vision_pb2

def get_color(frame):
  print("Getting color")

  blur = cv2.blur(frame, (5, 5))
  hsv = cv2.cvtColor(blur, cv2.COLOR_BGR2HSV)
  color = hsv.mean(axis=0).mean(axis=0)
  hue = int(color[0])

  red = range(0, 20)
  yellow = range(20, 40)
  green = range(60, 85)
  blue = range(85, 120)

  color = vision_pb2.InfoUpdate.Color.UNDEFINED
  color_str = "undefined"
  if hue in red:
    color = vision_pb2.InfoUpdate.Color.RED
    color_str = "red"
  elif hue in yellow:
    color = vision_pb2.InfoUpdate.Color.YELLOW
    color_str = "yellow"
  elif hue in green:
    color = vision_pb2.InfoUpdate.Color.GREEN
    color_str = "green"
  elif hue in blue:
    color = vision_pb2.InfoUpdate.Color.BLUE
    color_str = "blue"

  print("Found color", color_str)

  return color
