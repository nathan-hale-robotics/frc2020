package frc.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {
  private CvSink in;
  private CvSource out;
  private Mat frame;
  private Mat output;

  public ImageProcessor() {
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    camera.setResolution(160, 120);

    SmartDashboard.putNumber("REE", 1);
    SmartDashboard.updateValues();

    in = CameraServer.getInstance().getVideo("USB Camera 0");
    out = CameraServer.getInstance().putVideo("video", 160, 120);

    frame = new Mat();
    output = new Mat();

    new Thread(
            () -> {
              while (!Thread.interrupted()) {
                update();
              }
            })
        .start();
  }

  public void update() {
    if (in.grabFrame(frame) == 0) {
      return;
    }
    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
    Core.flip(frame, output, 0);
    out.putFrame(output);
  }
}
