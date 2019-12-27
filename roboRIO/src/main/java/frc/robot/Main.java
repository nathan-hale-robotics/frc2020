package frc.robot;

public final class Main {
  public static void main(String... args) throws InterruptedException {
    System.out.println("Creating vision server...");
    Thread server = new VisionServer(null);
    server.start();
    // waits for thread to complete
    server.join();
    // RobotBase.startRobot(Robot::new);
  }
}
