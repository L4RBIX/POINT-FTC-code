package org.firstinspires.ftc.teamcode.Controllers;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Outtake {
    PIDFController pidfController;

    public static double kP = 0.003, kI = 0.000005, kD = 0.00001, kF = 0.00043;

    public static double HIGH_RANGE_VELOCITY      = 1730;
    public static double MIN_ZONE_VELOCITY        = 1450;
    public static double AUTO_HIGH_RANGE_VELOCITY = 1730;
    public static double AUTO_MID_RANGE_VELOCITY  = 1450; // подбери под себя через Dashboard
    public static double targetVelocity           = 0;

    // --- ПОЗИЦИИ СЕРВО ---
    public static double openPos   = 0.35;
    public static double closedPos = 0.0;

    private DcMotorEx leftMotor, rightMotor;
    private Servo aimer;
    private Telemetry telemetry;
    private boolean isRunning      = false;
    private boolean isServoEnabled = false;

    public Outtake(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        pidfController = new PIDFController(kP, kI, kD, kF);
        this.telemetry = telemetry;

        leftMotor  = hardwareMap.get(DcMotorEx.class, "leftShooter");
        rightMotor = hardwareMap.get(DcMotorEx.class, "rightShooter");
        aimer      = hardwareMap.get(Servo.class, "servoName");

        // Серво выключено при старте — не дёргается
        if (aimer instanceof ServoImplEx) {
            ((ServoImplEx) aimer).setPwmDisable();
        }

        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void setangle(double position) {
        // Включаем серво только при первом вызове
        if (!isServoEnabled && aimer instanceof ServoImplEx) {
            ((ServoImplEx) aimer).setPwmEnable();
            isServoEnabled = true;
            try { Thread.sleep(100); } catch (InterruptedException ignore) {}
        }
        aimer.setPosition(position);
    }

    public void update() {
        pidfController.setPIDF(kP, kI, kD, kF);
        if (isRunning && targetVelocity != 0) {
            double currentVelocity = (leftMotor.getVelocity() + rightMotor.getVelocity()) / 2.0;
            double power = pidfController.calculate(currentVelocity, targetVelocity);
            leftMotor.setPower(power);
            rightMotor.setPower(power);
            telemetry.addData("Flywheel velocity", "%.0f / %.0f", currentVelocity, targetVelocity);
        } else {
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }
    }

    // ✅ все методы скорости реализованы
    public void setHighRange()     { targetVelocity = HIGH_RANGE_VELOCITY;     start(); }
    public void setMinZone()       { targetVelocity = MIN_ZONE_VELOCITY;        start(); }
    public void setHighRangeauto() { targetVelocity = AUTO_HIGH_RANGE_VELOCITY; start(); }
    public void setMidRange()      { targetVelocity = AUTO_MID_RANGE_VELOCITY;  start(); } // ✅ теперь работает
    public void setMidAngle()      { setangle(0.25); }                                      // ✅ угол mid — подбери

    public void start() { isRunning = true; }
    public void stop()  { isRunning = false; targetVelocity = 0; pidfController.reset(); }

    public double getCurrentVelocity() {
        return (leftMotor.getVelocity() + rightMotor.getVelocity()) / 2.0;
    }
}