package frc.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class ColorWheel {
  private final VisionServer server;
  private int numHalfRotations = -1;
  private InfoUpdate.Color startColor;
  private InfoUpdate.Color prevColor;

  public ColorWheel(VisionServer server) {
    this.server = server;
  }

  public boolean spinColor(VictorSP colorWheel, InfoUpdate.Color target) {
    InfoUpdate.Color color = server.getColor();
    if (color == target) {
      colorWheel.set(0);
      return false;
    }
    colorWheel.set(1);
    return true;
  }

  public boolean spinNum(VictorSP colorWheel) {
    if (numHalfRotations == -1) {
      numHalfRotations = 0;
      startColor = server.getColor();
      colorWheel.set(1);
    } else if (numHalfRotations < 8) {
      colorWheel.set(1);
    } else {
      numHalfRotations = -1;
      colorWheel.set(0);
      return false;
    }
    InfoUpdate.Color color = server.getColor();
    if (prevColor != color) {
      if (color == startColor) {
        numHalfRotations++;
      }
      prevColor = color;
    }
    return true;
  }
}
