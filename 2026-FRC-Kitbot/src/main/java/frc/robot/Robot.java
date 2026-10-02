// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {
  // Creates drive motors
  private final SparkMax frontLeftMotor = new SparkMax(11, MotorType.kBrushed);
  private final SparkMax frontRightMotor = new SparkMax(14, MotorType.kBrushed);
  private final SparkMax backRightMotor = new SparkMax(13, MotorType.kBrushed);
  private final SparkMax backLeftMotor = new SparkMax(12, MotorType.kBrushed);
  
  // Creates shooter motors
  private final SparkMax hopperMotor = new SparkMax(21, MotorType.kBrushed);
  private final SparkMax shooterMotor = new SparkMax(22, MotorType.kBrushed);


  // Creates configs for motors
  private SparkMaxConfig frontLeftMotorConfig = new SparkMaxConfig();
  private SparkMaxConfig frontRightMotorConfig = new SparkMaxConfig();
  private SparkMaxConfig backRightMotorConfig = new SparkMaxConfig();
  private SparkMaxConfig backLeftMotorConfig = new SparkMaxConfig();

  private SparkMaxConfig hopperMotorConfig = new SparkMaxConfig();
  private SparkMaxConfig shooterMotorConfig = new SparkMaxConfig();

  // Creates differential drive
  private final DifferentialDrive m_robotDrive =
      new DifferentialDrive(frontLeftMotor::set, frontRightMotor::set);
  
  // Creates Joystick
  private final Joystick m_stick = new Joystick(0);

  // Shooter Speed Variables
  private double shooterSpeedForLaunch = 1;
  private double shooterSpeedForLoad = .75;
  private double hopperSpeed = .75;

  /** Called once at the beginning of the robot program. */
  public Robot() {
    SendableRegistry.addChild(m_robotDrive, frontLeftMotor);
    SendableRegistry.addChild(m_robotDrive, frontRightMotor);
    SendableRegistry.addChild(m_robotDrive, backLeftMotor);
    SendableRegistry.addChild(m_robotDrive, backRightMotor);

    frontRightMotorConfig.idleMode(IdleMode.kBrake).inverted(true);
    backRightMotorConfig.follow(frontRightMotor).idleMode(IdleMode.kBrake);
    backLeftMotorConfig.follow(frontLeftMotor).idleMode(IdleMode.kBrake);
    frontLeftMotorConfig.idleMode(IdleMode.kBrake);

    shooterMotorConfig.inverted(true).idleMode(IdleMode.kBrake);
    hopperMotorConfig.idleMode(IdleMode.kBrake);

    // Applies config
    frontLeftMotor.configure(frontLeftMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    frontRightMotor.configure(frontRightMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    backRightMotor.configure(backRightMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    backLeftMotor.configure(backLeftMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    shooterMotor.configure(shooterMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    hopperMotor.configure(hopperMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.

    m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getZ());

    if (m_stick.getRawButton(1)) {
      shooterMotor.set(shooterSpeedForLaunch);
      hopperMotor.set(hopperSpeed);
    }
    else if (m_stick.getRawButton(2)) {
      shooterMotor.set(shooterSpeedForLoad);
      hopperMotor.set(-hopperSpeed);
    }
    else {
      shooterMotor.set(0);
      hopperMotor.set(0);
    }
  }
}