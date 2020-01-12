package frc.robot;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

public class VisionServer extends Thread {

  private static final Logger logger = Logger.getLogger(VisionServer.class.getName());
  private final Robot robot;

  public VisionServer(Robot main) {
    robot = main;
  }

  @Override
  public void run() {
    Server server = ServerBuilder.forPort(8000).addService(new VisionImpl()).build();
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
    public StreamObserver<MotorSpeed> setMotorSpeed(StreamObserver<MotorSpeedResponse> observer) {
      return new StreamObserver<>() {
        @Override
        public void onNext(MotorSpeed speed) {
          logger.info(
              "Setting motor speed to: right: " + speed.getRight() + ", left: " + speed.getLeft());
          MotorSpeedResponse.Builder res = MotorSpeedResponse.newBuilder();
          observer.onNext(res.build());
        }

        @Override
        public void onError(Throwable t) {
          logger.warning("Exeption in setMotorSpeed StreamObserver: " + t);
        }

        @Override
        public void onCompleted() {
          logger.info("Connection with client terminated");
          observer.onCompleted();
        }
      };
    }
  }
}
