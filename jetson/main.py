import grpc

import vision_pb2
import vision_pb2_grpc

import cv2

import color_sensor

cap = cv2.VideoCapture(2)

class MoveDirectionIterator:
  def __init__(self):
    self.forward = 0
    self.strafe = 0
    self.turn = 0

  def __iter__(self):
    return self

  def __next__(self):
    val, frame = cap.read()
    if not val:
      print("Could not get frame from camera!")
      raise StopIteration
    if cv2.waitKey(1) & 0xFF == ord('q'):
      raise StopIteration
    color_sensor.get_color(frame)
    return vision_pb2.MoveDirection(forward=self.forward, strafe=self.strafe, turn=self.turn)

channel = grpc.insecure_channel('localhost:1234')
stub = vision_pb2_grpc.VisionStub(channel)

for i in MoveDirectionIterator():
  print(i)

cv2.destroyAllWindows()

# for res in stub.SetMoveDirection(MoveDirectionIterator()):
#   print("REE:", res)

