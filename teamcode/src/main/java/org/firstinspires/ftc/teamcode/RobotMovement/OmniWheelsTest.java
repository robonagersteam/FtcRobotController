package org.firstinspires.ftc.teamcode.RobotMovement;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


public class OmniWheelsTest {

    private DcMotor frontLeftMotor,backLeftMotor, frontRightMotor, backRightMotor;

    private IMU imu;

    public void init(HardwareMap hwMap) {
        //set up
        frontLeftMotor = hwMap.get(DcMotor.class, "front_left_motor");
        backLeftMotor = hwMap.get(DcMotor.class, "back_left_motor");
        frontRightMotor = hwMap.get(DcMotor.class, "front_right_motor");
        backRightMotor = hwMap.get(DcMotor.class, "back_right_motor");

        //set direction of each motor
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);

        //set encoder
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        imu = hwMap.get(IMU.class,"imu");

        //setting up contror hub orientation
        RevHubOrientationOnRobot Revorientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        imu.initialize(new IMU.Parameters(Revorientation));
    }

    //Math for omni wheels
    public void drive(double forward, double strafe, double rotate) {
        double frontLeftPower = forward + strafe + rotate;
        double backLeftPower = forward - strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;

        //This are the max values
        double maxPower = 1.0;
        double maxSpeed = 1.0; //You can control the speed with this one

        //cheks who has highest power
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        //For the maxSpeed
        frontLeftMotor.setPower(maxSpeed *(frontLeftPower / maxPower));
        backLeftMotor.setPower(maxSpeed *(backLeftPower / maxPower));
        frontRightMotor.setPower(maxSpeed *(frontRightPower / maxPower));
        backRightMotor.setPower(maxSpeed *(backRightPower / maxPower));

    }

    //this is so the robot is oriented with the field and not itself
    //it will make it a loot easier for the drivers
    public void driveFieldRelative(double forward, double strafe, double rotate) {
        double theta = Math.atan2(forward, strafe);
        double r =Math.hypot(strafe , forward);

        theta = AngleUnit.normalizeDegrees(theta -
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        double newForward = r * Math.sin(theta);
        double newStrafe = r * Math.cos(theta);

        this.drive(newForward, newStrafe, rotate);

    }

    public void loop() {


    }
}
