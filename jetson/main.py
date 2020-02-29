import grpc

import vision_pb2
import vision_pb2_grpc

import cv2

import color_sensor
import ball_sensor
cap = cv2.VideoCapture(2)

class InfoUpdateIterator:
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
    color = color_sensor.get_color(frame)
    ball_coords = ball_sensor.get_distance(frame)
    print(ball_coords)
    if (ball_coords.any() == None):
      ball_coords = [-1, -1, -1, -1, -1, -1]
    return vision_pb2.InfoUpdate(
        forward=self.forward,
        strafe=self.strafe,
        turn=self.turn,
        color=color,
        ball=vision_pb2.InfoUpdate.Ball(
          distance=ball_coords[0],
          angle=ball_coords[1],
          x=ball_coords[2],
          y=ball_coords[3],
          width=ball_coords[4],
          height=ball_coords[5]))

channel = grpc.insecure_channel('localhost:1234')
stub = vision_pb2_grpc.VisionStub(channel)

for i in InfoUpdateIterator():
  print(i)

cv2.destroyAllWindows()

# for res in stub.SetMoveDirection(MoveDirectionIterator()):
#   print("REE:", res)

