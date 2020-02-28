package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.logging.Logger;

public class Robot extends TimedRobot {
  private Joystick driveJoystick;
  private Joystick ballsJoystick;
  private VisionServer server;
  private MecanumDrive drive;
  private VictorSP loaderWheels;
  private VictorSP conveyor;
  private VictorSP liftLeft;
  private VictorSP liftRight;
  private VictorSP colorWheel;
  private Servo liftBar;
  private Solenoid dumpLift;
  private Solenoid loaderLift;
  private DigitalOutput conveyorLaser;
  private DigitalInput conveyorSensor;
  private Timer conveyorTimer;
  private ImageProcessor proc;

  private static final Logger logger = Logger.getLogger(Robot.class.getName());

  @Override
  public void robotInit() {
    VictorSP backRight  = new VictorSP(9);
    VictorSP backLeft   = new VictorSP(8);
    liftLeft            = new VictorSP(7);
    conveyor            = new VictorSP(6);
    liftBar             = new Servo(5);
    VictorSP frontLeft  = new VictorSP(4);
    VictorSP frontRight = new VictorSP(3);
    liftRight           = new VictorSP(2);
    colorWheel          = new VictorSP(1);
    loaderWheels        = new VictorSP(0);

    conveyorLaser       = new DigitalOutput(0);
    conveyorSensor      = new DigitalInput(1);
    conveyorTimer       = new Timer();
    conveyorTimer.start();
    conveyorLaser.set(true);

    drive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

    driveJoystick = new Joystick(0);
    ballsJoystick = new Joystick(1);
    server = new VisionServer(this);
    server.start();
    proc = new ImageProcessor();
  }

  @Override
  public void autonomousPeriodic() {
    drive.driveCartesian(server.getStrafe(), server.getForward(), server.getTurn());
  }

  @Override
  public void teleopPeriodic() {
    updateBalls();
    updateDrive();
  }

  public void updateBalls() {
    if (conveyorSensor.get() || ballsJoystick.getRawButton(4)) {
      conveyorTimer.reset();
      conveyorTimer.start();
    }
    conveyor.set(conveyorTimer.get() <= 3 ? 1 : 0);
    dumpLift.set(ballsJoystick.getRawAxis(3) > .5);
    liftBar.set(ballsJoystick.getRawButton(3) ? 0 : .5);
  }

  public void updateDrive() {
    double speed = driveJoystick.getRawButton(5) ? .5 : 1;
    double forward = driveJoystick.getRawAxis(1) * speed;
    double strafe = -driveJoystick.getRawAxis(0) * speed;
    double turn = driveJoystick.getRawAxis(4) * speed;
    drive.driveCartesian(strafe, forward, turn);
  }

  public void updateColorWheel() {
    if (ballsJoystick.getRawButton(1)) {
    }
  }
}
