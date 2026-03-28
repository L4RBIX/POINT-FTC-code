package org.firstinspires.ftc.teamcode.Controllers;

import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Hardware.GoBildaPinpointDriver;


public class MecanumDrive {

    private DcMotorEx fl, fr, bl, br;
    private GoBildaPinpointDriver pinpoint;

    public MecanumDrive(HardwareMap hw) {
        fl = hw.get(DcMotorEx.class, "fl");
        fr = hw.get(DcMotorEx.class, "fr");
        bl = hw.get(DcMotorEx.class, "bl");
        br = hw.get(DcMotorEx.class, "br");

        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);




        setBrake(true);

        // Initialize Pinpoint
        pinpoint = hw.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(-84.0, -168.0); // Adjust to your robot!
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        //  pinpoint.setEncoderResolution(GoBildaPinpointDriver.goBILDA_4_BAR_POD);
        pinpoint.resetPosAndIMU();
    }

    public void drive(double y, double x, double rx) {
        double denom = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double flP = (y + x + rx) / denom;
        double blP = (y - x + rx) / denom;
        double frP = (y - x - rx) / denom;
        double brP = (y + x - rx) / denom;

        fl.setPower(flP);
        bl.setPower(blP);
        fr.setPower(frP);
        br.setPower(brP);
    }

    // Add this method
    public Pose2D getPosition() {
        pinpoint.update();
        return pinpoint.getPosition();
    }

    // Add this method
    public void resetPosition() {
        pinpoint.resetPosAndIMU();
    }

    public void stop() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    private void setBrake(boolean brake) {
        DcMotor.ZeroPowerBehavior b =
                brake ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT;

        fl.setZeroPowerBehavior(b);
        fr.setZeroPowerBehavior(b);
        bl.setZeroPowerBehavior(b);
        br.setZeroPowerBehavior(b);
    }
}