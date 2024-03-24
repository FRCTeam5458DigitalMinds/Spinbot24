package frc.lib;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

public final class CTREConfigs {
  public CANcoderConfiguration swerveCanCoderConfig;

  public CTREConfigs() {
    swerveCanCoderConfig = new CANcoderConfiguration();

    /* Swerve CANCoder Configuration */
    swerveCanCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;

    //swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
    swerveCanCoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;

    //sensors always initialized to their absolute positions.
  }
}