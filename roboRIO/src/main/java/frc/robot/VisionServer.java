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
      logger.info("Starting vision server!");
      server.start();
      server.awaitTermination();
    } catch (InterruptedException e) {
      System.err.println("Interrupt received!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class VisionImpl extends VisionGrpc.VisionImplBase {

    public StreamObserver<MoveDirection> setMotorSpeed(
        StreamObserver<MoveDirectionResponse> observer) {
      return new StreamObserver<>() {
        @Override
        public void onNext(MoveDirection speed) {
          logger.info(
              "Setting motor speed to: forward: "
                  + speed.getForward()
                  + ", strafe: "
                  + speed.getStrafe()
                  + ", turn: "
                  + speed.getTurn());
          MoveDirectionResponse.Builder res = MoveDirectionResponse.newBuilder();
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
