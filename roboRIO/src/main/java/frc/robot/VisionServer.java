package frc.robot;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

public class VisionServer extends Thread {

  private static final Logger logger = Logger.getLogger(VisionServer.class.getName());
  private final Robot robot;
  private float forward;
  private float strafe;
  private float turn;
  private InfoUpdate.Color color;

  public VisionServer(Robot main) {
    robot = main;
  }

  @Override
  public void run() {
    Server server = ServerBuilder.forPort(8000).addService(new VisionImpl(this)).build();
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

  public float getForward() {
    return forward;
  }

  public float getStrafe() {
    return strafe;
  }

  public float getTurn() {
    return turn;
  }

  public InfoUpdate.Color getColor() {
    return color;
  }

  public void setInfo(float forward, float strafe, float turn, InfoUpdate.Color color) {
    this.forward = forward;
    this.strafe  = strafe;
    this.turn    = turn;
    this.color   = color;
  }

  private static class VisionImpl extends VisionGrpc.VisionImplBase {
    private final VisionServer server;

    public VisionImpl(VisionServer server) {
      this.server = server;
    }

    @Override
    public StreamObserver<InfoUpdate> updateInfo(
        StreamObserver<InfoResponse> observer) {
      return new StreamObserver<>() {
        @Override
        public void onNext(InfoUpdate info) {
          logger.info(
              "Setting info to: forward: "
                  + info.getForward()
                  + ", strafe: "
                  + info.getStrafe()
                  + ", turn: "
                  + info.getTurn()
                  + ", color: "
                  + info.getColor());
          server.setInfo(info.getForward(), info.getStrafe(), info.getTurn(), info.getColor());
          InfoResponse.Builder res = InfoResponse.newBuilder();
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
