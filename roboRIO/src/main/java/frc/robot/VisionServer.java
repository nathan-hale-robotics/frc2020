package frc.robot;

import com.google.protobuf.ByteString;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class VisionServer extends Thread {

  private static final Logger logger = Logger.getLogger(VisionServer.class.getName());
  private final Robot robot;
  private final CvSource video;

  public VisionServer(Robot main) {
    robot = main;
    video = CameraServer.getInstance().putVideo("video", 640, 480);
  }

  @Override
  public void run() {
    CameraServer.getInstance().startAutomaticCapture();
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

  public void updateCamera(ByteString image) {
    Mat mat = new Mat(480, 640, CvType.CV_32FC1);
    int width = 640;
    for (int y = 0; y < 480; y++) {
      for (int x = 0; x < 640; x++) {
        mat.put(
            y,
            x,
            image.byteAt((y * width + x) * 4 + 0),
            image.byteAt((y * width + x) * 4 + 1),
            image.byteAt((y * width + x) * 4 + 2),
            image.byteAt((y * width + x) * 4 + 3));
      }
    }
    video.putFrame(mat);
  }

  private static class VisionImpl extends VisionGrpc.VisionImplBase {

    private VisionServer server;

    public VisionImpl(VisionServer server) {
      this.server = server;
    }

    @Override
    public StreamObserver<CameraUpdate> updateCamera(
        StreamObserver<CameraUpdateResponse> observer) {
      return new StreamObserver<>() {
        @Override
        public void onNext(CameraUpdate update) {
          server.updateCamera(update.getImage());
          CameraUpdateResponse.Builder res = CameraUpdateResponse.newBuilder();
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

    @Override
    public StreamObserver<MoveDirection> setMoveDirection(
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
