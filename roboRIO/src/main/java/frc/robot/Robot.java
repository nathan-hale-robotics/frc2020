package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private Joystick joystick;
  private VisionServer server;
  private MecanumDrive drive;
  private Servo servo;

  @Override
  public void robotInit() {
    joystick = new Joystick(0);
    VictorSP frontLeft = new VictorSP(9);
    VictorSP backLeft = new VictorSP(8);
    VictorSP frontRight = new VictorSP(7);
    VictorSP backRight = new VictorSP(6);
    drive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
    servo = new Servo(5);
    // servo.setBounds(2500, 2500, 1500, 500, 500);
    server = new VisionServer(this);
    server.start();
  }

  @Override
  public void teleopPeriodic() {
    double speed = joystick.getRawButton(5) ? .5 : 1;
    double forward = joystick.getRawAxis(1) * speed;
    double strafe = -joystick.getRawAxis(0) * speed;
    double turn = joystick.getRawAxis(4) * speed;
    double value = SmartDashboard.getNumber("DB/Slider 0", -1) / 5;
    if (value == -1) {
      System.err.println("Cannot grab value from slider!");
      drive.driveCartesian(strafe, forward, turn);
    } else {
      drive.driveCartesian(strafe, forward, turn + strafe * value);
    }
    servo.set(joystick.getRawButton(1) ? 1 : .5);
  }
}
