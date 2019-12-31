import grpc

import vision_pb2
import vision_pb2_grpc


class MotorSpeedIterator:
  def __init__(self):
    self.right = 0
    self.left = 0

  def __iter__(self):
    return self

  def __next__(self):
    self.right += 1
    if self.right > 3:
      raise StopIteration
    return vision_pb2.MotorSpeed(left=self.left, right=self.right)

channel = grpc.insecure_channel('localhost:8000')
stub = vision_pb2_grpc.VisionStub(channel)

for res in stub.SetMotorSpeed(MotorSpeedIterator()):
  print("Got res:", res)
