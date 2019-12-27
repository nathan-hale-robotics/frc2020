import grpc

import vision_pb2
import vision_pb2_grpc

# open channel to localhost on port 8000
channel = grpc.insecure_channel('localhost:8000')
# tell grpc that this is a vision grpc channel
stub = vision_pb2_grpc.VisionStub(channel)

print("Sending SetMotorSpeed message...")
res = stub.SetMotorSpeed(vision_pb2.MotorSpeed(left=3, right=2))
print("Message sent successfully")

