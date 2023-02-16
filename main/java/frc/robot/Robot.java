// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
//import edu.wpi.first.wpilibj.Encoder; 
//import edu.wpi.first.wpilibj.XboxController;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  NetworkTableEntry xEntry;
  NetworkTableEntry yEntry;

  public static final int can0 = 0;
  public static final int can1 = 1;
  public static final int can2 = 2;
  public static final int can3 = 3;
  public static final int can4 = 4;

  Joystick joy1 = new Joystick(0); //inputs for Joystick
  WPI_VictorSPX leftFrontMotor = new WPI_VictorSPX(can0);
  WPI_VictorSPX rightFrontMotor = new WPI_VictorSPX(can3);
  WPI_VictorSPX leftRearMotor = new WPI_VictorSPX(can1);
  WPI_VictorSPX rightRearMotor = new WPI_VictorSPX(can2);
  DifferentialDrive _drive = new DifferentialDrive(leftFrontMotor, rightFrontMotor);
  WPI_VictorSPX CIMcoder = new WPI_VictorSPX(can4);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

     //Get the default instance of NetworkTables that was created automatically
      //when your program starts
      NetworkTableInstance inst = NetworkTableInstance.getDefault();

      //Get the table within that instance that contains the data. There can
      //be as many tables as you like and exist to make it easier to organize
      //your data. In this case, it's a table called datatable.
      NetworkTable table = inst.getTable("datatable");

      //Get the entries within that table that correspond to the X and Y values
      //for some operation in your program.
      xEntry = table.getEntry("x"); //Gets number with default 1 (False) 

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */

   private double startTime;

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    Double time = Timer.getFPGATimestamp();

    while (time - startTime < 5) {
      leftFrontMotor.set(0.5);
      leftRearMotor.set(0.5);
      rightFrontMotor.set(-0.5);
      rightRearMotor.set(-0.5);
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
 /* factory default values */
   leftFrontMotor.configFactoryDefault();
   rightFrontMotor.configFactoryDefault();

 /* flip values so robot moves forward when stick-forward/LEDs-green */
    leftFrontMotor.setInverted(true); // <<<<<< Adjust this
    rightFrontMotor.setInverted(false); // <<<<<< Adjust this

    /*
      * WPI drivetrain classes defaultly assume left and right are opposite. call
      * this so we can apply + to both sides when moving forward. DO NOT CHANGE
      */
    //_drive.setRightSideInverted(false);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double xSpeed = joy1.getRawAxis(1);
    double zRotation = joy1.getRawAxis(2);
    _drive.arcadeDrive(xSpeed * 0.3, zRotation * 0.3);
    
    _drive.arcadeDrive(xSpeed, zRotation);

    // hold down btn1 to print stick values 
    if (joy1.getRawButton(1)) {
      System.out.println("xSpeed:" + xSpeed + "    zRotation:" + zRotation);

    if (joy1.getRawButtonPressed(3)) {
      CIMcoder.set(0.3);
    }

    else {
      CIMcoder.set(0.0);
    }
    }
  }
  

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}