package org.firstinspires.ftc.teamcode.Controllers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor intakeMotor, rollerMotor;

    public Intake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rollerMotor = hardwareMap.get(DcMotor.class, "roller");

        rollerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rollerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rollerMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    }
    public void set_Power(double power) {
        intakeMotor.setPower(power);
    }

    public void forward(double power) {
        intakeMotor.setPower(0.6 * power);
        rollerMotor.setPower(2 * power);
    }

    public void backward(double power) {
        intakeMotor.setPower(0.6 * -power);
        rollerMotor.setPower(2 * -power);

    }

    public void stop() {
        intakeMotor.setPower(0);
        rollerMotor.setPower(0);
    }
    public void rollerpower(double p) {
        rollerMotor.setPower(p);
    }

    public void rollerForward(double power) {
        rollerMotor.setPower(power);
    }

    public void rollerstop(double power) {
        rollerMotor.setPower(0);
    }

}