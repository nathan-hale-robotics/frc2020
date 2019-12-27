package frc.robot;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class VisionServer extends Thread {
  private final Robot robot;

  public VisionServer(Robot main) {
    robot = main;
  }

  @Override
  public void run() {
    Server server = ServerBuilder.forPort(8080)
      .addService(new VisionImpl())
      .build();
    try {
      server.start();
      server.awaitTermination();
    } catch (InterruptedException e) {
      System.err.println("Interrupt received!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class VisionImpl extends VisionGrpc.VisionImplBase {
    @Override
    public void getMotorSpeed(MotorSpeed req, StreamObserver<MotorSpeedResponse> response) {
      System.out.println("Right: " + req.getRight());
      System.out.println("Left: " + req.getLeft());
      MotorSpeedResponse.Builder res = MotorSpeedResponse.newBuilder();
      // set response values here
      response.onNext(res.build());
      response.onCompleted();
    }
  }
}
