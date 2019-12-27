package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import io.grpc.stub.StreamObserver;

public class Robot extends TimedRobot {
  private Joystick joystick;
  private Spark motor;

  @Override
  public void robotInit() {
    joystick = new Joystick(0);
    motor = new Spark(0);
  }

  @Override
  public void teleopPeriodic() {
    if (joystick.getRawButton(0)) {
      motor.set(1);
    } else {
      motor.set(0);
    }
  }

  static class VisionImpl extends VisionGrpc.VisionImplBase {
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
