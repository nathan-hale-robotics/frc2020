package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;

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
}
