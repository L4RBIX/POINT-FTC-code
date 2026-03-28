package org.firstinspires.ftc.teamcode.pedroPathing.Autons.SDU;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Controllers.Intake;
import org.firstinspires.ftc.teamcode.Controllers.Outtake;
import org.firstinspires.ftc.teamcode.Controllers.MecanumDrive;

@Autonomous(name = "6 Ball Timed Auto", group = "Test")
public class Beka666Auto extends OpMode {

    private Intake intake;
    private Outtake outtake;
    private MecanumDrive drive;

    private ElapsedTime timer;
    private int state;

    @Override
    public void init() {
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap, telemetry, gamepad1);
        drive = new MecanumDrive(hardwareMap);

        timer = new ElapsedTime();
        state = 0;

        telemetry.addLine("6 Ball Auto Ready");
        telemetry.update();
    }

    @Override
    public void start() {
        timer.reset();
        state = 0;
    }

    @Override
    public void loop() {

        switch (state) {

            // ===============================
            // SPIN UP
            // ===============================
            case 0:
                outtake.start();
                outtake.setHighRange();
                outtake.setangle(-43);

                if (timer.seconds() >= 3.0) {
                    timer.reset();
                    state = 1;
                }
                break;

            // ===============================
            // SHOOT FIRST 3
            // ===============================
            case 1:
                double t = timer.seconds();

                // ring 1
                if (t < 0.15) {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                }
                else if (t < 1.6) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }

                // ring 2
                else if (t < 1.75) {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                }
                else if (t < 3.2) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }
                // ring 3
                else if (t < 4.5) {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                }
                else if (t < 4.8) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }

                else {
                    timer.reset();
                    state = 2;
                }
                break;

            // ===============================
            // TURN RIGHT
            // ===============================
            case 2:
                drive.drive(0, 0, 0.5);  // rotate right

                if (timer.seconds() >= 0.7) {   // tune this
                    drive.stop();
                    timer.reset();
                    state = 3;
                }
                break;

            // ===============================
            // DRIVE TO HUMAN ZONE
            // ===============================
            case 3:
                drive.drive(0.7, 0, 0);

                if (timer.seconds() >= 1.5) {  // tune distance
                    drive.stop();
                    timer.reset();
                    state = 4;
                }
                break;

            // ===============================
            // INTAKE 3 BALLS
            // ===============================
            case 4:
                intake.set_Power(-1);
                intake.rollerpower(-1);

                if (timer.seconds() >= 2.0) {   // tune intake time
                    intake.set_Power(0);
                    intake.rollerpower(0);
                    timer.reset();
                    state = 5;
                }
                break;

            // ===============================
            // DRIVE BACK
            // ===============================
            case 5:
                drive.drive(-0.7, 0, 0);

                if (timer.seconds() >= 1.5) {
                    drive.stop();
                    timer.reset();
                    state = 6;
                }
                break;

            // ===============================
            // TURN BACK LEFT
            // ===============================
            case 6:
                drive.drive(0, 0, -0.5);

                if (timer.seconds() >= 0.7) {
                    drive.stop();
                    timer.reset();
                    state = 7;
                }
                break;

            // ===============================
            // SHOOT SECOND 3
            // ===============================
            case 7:
                double t2 = timer.seconds();

                // same shooting pattern again
                if (t2 < 0.15) {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                }
                else if (t2 < 1.6) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }
                else if (t2 < 1.75) {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                }
                else if (t2 < 3.2) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }
                else if (t2 < 3.5) {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                }
                else if (t2 < 4.8) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }
                else {
                    outtake.stop();
                    state = -1;
                }
                break;

            case -1:
                drive.stop();
                intake.set_Power(0);
                intake.rollerpower(0);
                break;
        }

        outtake.update();

        telemetry.addData("State", state);
        telemetry.update();
    }
}