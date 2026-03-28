package org.firstinspires.ftc.teamcode.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Controllers.Intake;
import org.firstinspires.ftc.teamcode.Controllers.MecanumDrive;
import org.firstinspires.ftc.teamcode.Controllers.Outtake;


@TeleOp(name = "TeleOp PS4 FINAL")
public class Teleop extends LinearOpMode {
    MecanumDrive drive = null;
   Intake intake = null;
    Outtake outtake = null;

    boolean lastR1 = false;
    boolean isOpen = false;


    @Override
    public void runOpMode() {
        drive = new MecanumDrive(hardwareMap);
//        intake = new Intake(hardwareMap);
//        outtake = new Outtake(hardwareMap, telemetry, gamepad1);

        waitForStart();

     //   outtake.setangle(outtake.closedPos);
        isOpen = false;

        while (opModeIsActive()) {
            drive.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x * 0.7);

            boolean currentR1 = gamepad1.right_bumper;

            if (currentR1 && !lastR1) {
                isOpen = !isOpen;
                if (isOpen) {
              //      outtake.setangle(outtake.openPos);
                } else {
//                    outtake.setangle(outtake.closedPos);
                }
            }

            lastR1 = currentR1;

//            if (gamepad1.triangle) outtake.setHighRange();
//            else if (gamepad1.square) outtake.setMinZone();
//            else if (gamepad1.cross) outtake.stop();

            if (gamepad1.left_bumper) {
//                intake.set_Power(-1);
//                intake.rollerpower(0.8);
            } else {
                double intakePower = gamepad1.left_trigger - gamepad1.right_trigger;
//                intake.set_Power(intakePower);

//                if (gamepad1.right_trigger > 0.2) intake.rollerpower(-0.8);
//                else if (gamepad1.left_trigger > 0.2) intake.rollerpower(-0.8);
//                else intake.rollerpower(0);
            }

//            outtake.update();
            telemetry.addData("Servo", isOpen ? "OPEN" : "CLOSED");
            telemetry.update();
        }
    }
    }
