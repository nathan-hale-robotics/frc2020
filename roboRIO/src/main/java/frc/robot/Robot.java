package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import java.util.logging.Logger;

public class Robot extends TimedRobot {
  private Joystick driveJoystick;
  private Joystick ballsJoystick;
  private VisionServer server;
  private MecanumDrive drive;
  private VictorSP loaderWheels;
  private VictorSP conveyor;
  private VictorSP robotLiftLeft;
  private VictorSP robotLiftRight;
  private VictorSP colorWheel;
  private Servo hopperBar;
  private Solenoid hopperLift;
  private Solenoid loaderLiftUp;
  private Solenoid loaderLiftDown;
  private Solenoid robotLiftBrakes;
  private DigitalOutput conveyorLaser;
  private DigitalInput conveyorSensor;
  private Compressor compressor;
  private Timer conveyorTimer;
  private ImageProcessor proc;

  private static final Logger logger = Logger.getLogger(Robot.class.getName());

  @Override
  public void robotInit() {
    VictorSP frontLeft  = new VictorSP(9);
    VictorSP backLeft   = new VictorSP(8);
    robotLiftLeft       = new VictorSP(7);
    conveyor            = new VictorSP(6);
    hopperBar           = new Servo(0);
    VictorSP backRight  = new VictorSP(4);
    VictorSP frontRight = new VictorSP(3);
    robotLiftRight      = new VictorSP(2);
    loaderWheels        = new VictorSP(1);
    colorWheel          = new VictorSP(5);

    conveyorLaser       = new DigitalOutput(0);
    conveyorSensor      = new DigitalInput(1);

    compressor          = new Compressor();
    hopperLift          = new Solenoid(0);
    robotLiftBrakes     = new Solenoid(1);
    loaderLiftDown      = new Solenoid(2);
    loaderLiftUp        = new Solenoid(3);

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
    conveyor.set(ballsJoystick.getRawButton(BallButtons.Y.getValue()) ? 1 : 0);

    hopperLift.set(ballsJoystick.getPOV() == 0);
    hopperBar.set(ballsJoystick.getRawButton(BallButtons.X.getValue()) ? 0 : .5);

    loaderWheels.set(ballsJoystick.getRawButton(BallButtons.LEFT_TRIGGER.getValue()) ? 1 : 0);
    loaderLiftUp.set(ballsJoystick.getRawButtonPressed(BallButtons.RIGHT_TRIGGER.getValue()));
    loaderLiftDown.set(ballsJoystick.getRawButtonReleased(BallButtons.RIGHT_TRIGGER.getValue()));

    double left = -ballsJoystick.getRawAxis(1);
    double right = -ballsJoystick.getRawAxis(3);
    if (ballsJoystick.getRawButton(BallButtons.RIGHT_BUMPER.getValue())) {
      robotLiftBrakes.set(true);
      robotLiftLeft.set(left);
      robotLiftRight.set(right);
    } else {
      robotLiftBrakes.set(false);
    }
    if (ballsJoystick.getRawButton(1)) {
      compressor.start();
    }
    if (ballsJoystick.getRawButton(3)) {
      compressor.stop();
    }
    colorWheel.set(ballsJoystick.getRawButton(BallButtons.LEFT_BUMPER.getValue()) ? 1 : 0);
  }

  public void updateDrive() {
    double speed = driveJoystick.getRawButton(DriveButtons.LEFT_BUMPER.getValue()) ? .5 : 1;
    double forward = -driveJoystick.getRawAxis(1) * speed;
    double strafe = driveJoystick.getRawAxis(0) * speed;
    double turn = driveJoystick.getRawAxis(4) * speed;
    drive.driveCartesian(
      joystickFunc(strafe, 20.0), 
      joystickFunc(forward, 20.0), 
      joystickFunc(turn, 20.0));
  }

  public void updateColorWheel() {
  }

  private enum DriveButtons {
    UNDEFINED(0),
    A(1), B(2), X(3), Y(4),
    LEFT_BUMPER(5), RIGHT_BUMPER(6),
    BACK(7), START(8),
    LEFT_STICK(9), RIGHT_STICK(10);

    private final int value;
    DriveButtons(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  private enum BallButtons {
    UNDEFINED(0),
    X(1), A(2), B(3), Y(4),
    LEFT_BUMPER(5), RIGHT_BUMPER(6),
    LEFT_TRIGGER(7), RIGHT_TRIGGER(8),
    BACK(9), START(10),
    LEFT_STICK(11), RIGHT_STICK(12);

    private final int value;
    BallButtons(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  private double joystickFunc(
    double x, 
    double c) {
    return Math.signum(x)*
      (Math.pow(c+1.0, Math.abs(x)) - 1.0)/c;
  }
}
