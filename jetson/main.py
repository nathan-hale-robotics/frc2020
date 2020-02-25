import grpc

import vision_pb2
import vision_pb2_grpc

import cv2

import color_sensor

cap = cv2.VideoCapture(0)

class MoveDirectionIterator:
  def __init__(self):
    self.forward = 0
    self.strafe = 0
    self.turn = 0

  def __iter__(self):
    return self

  def __next__(self):
    self.forward += 1
    if self.forward > 3:
      raise StopIteration
    return vision_pb2.MoveDirection(forward=self.forward, strafe=self.strafe, turn=self.turn)

class CameraUpdateIterator:
  def __init__(self):
    pass

  def __iter__(self):
    return self

  def __next__(self):
    val, frame = cap.read()
    array = bytearray(640 * 480 * 3)
    width = 640
    for y in range(480):
      for x in range(640):
        array[(y * width + x) * 3 + 0] = frame[y][x][0]
        array[(y * width + x) * 3 + 1] = frame[y][x][1]
        array[(y * width + x) * 3 + 2] = frame[y][x][2]
    color_sensor.get_color(cap)
    return vision_pb2.CameraUpdate(image=bytes(array))

channel = grpc.insecure_channel('localhost:1234')
stub = vision_pb2_grpc.VisionStub(channel)

while True:
  color_sensor.get_color(cap)

for res in stub.UpdateCamera(CameraUpdateIterator()):
  print("REE:", res)

