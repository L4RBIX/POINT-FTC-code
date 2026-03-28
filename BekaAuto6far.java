package org.firstinspires.ftc.teamcode.pedroPathing.Autons.SDU;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Controllers.Intake;
import org.firstinspires.ftc.teamcode.Controllers.Outtake;
import org.firstinspires.ftc.teamcode.Controllers.MecanumDrive;

@Autonomous(name = "Timed Auto Simple 067", group = "Test")
public class BekaAuto6far extends OpMode {

    private Intake intake;
    private Outtake outtake;
    private MecanumDrive drive;

    private ElapsedTime timer = new ElapsedTime();
    private ElapsedTime matchTimer = new ElapsedTime();

    private int state = -2;
    private boolean emergencyActivated = false;

    @Override
    public void init() {
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap, telemetry, gamepad1);
        drive = new MecanumDrive(hardwareMap);

        telemetry.addLine("Auto Ready");
        telemetry.update();
    }

    @Override
    public void start() {
        timer.reset();
        matchTimer.reset();
        state = -2;
        emergencyActivated = false;
    }

    @Override
    public void loop() {

        // ===============================
        // LAST 5 SECONDS PARK
        // ===============================
        if (!emergencyActivated && matchTimer.seconds() >= 25.0) {

            emergencyActivated = true;

            intake.set_Power(0);
            intake.rollerpower(0);
            outtake.stop();

            timer.reset();
            state = 200;
        }

        switch (state) {

            case 200:
                drive.drive(0.5, 0, 0);

                if (timer.seconds() >= 0.5) {
                    drive.stop();
                    state = 201;
                }
                break;

            case 201:
                drive.stop();
                break;


            // ===============================
            // PRE MOVE
            // ===============================
            case -2:
                drive.drive(0.5, 0, 0);

                if (timer.seconds() >= 0.2) {
                    drive.stop();
                    timer.reset();
                    state = -1;
                }
                break;

            case -1:
                drive.drive(0, 0, 0.4);

                if (timer.seconds() >= 0.15) {
                    drive.stop();
                    timer.reset();
                    state = 0;
                }
                break;


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
            // SHOOT 3
            // ===============================
            case 1:

                double t = timer.seconds();

                if (t < 0.35) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                }
                else if (t < 1.6) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }
                else if (t < 2.7) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                }
                else if (t < 4.5) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                }
                else if (t < 5.5) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                }
                else {
                    // STOP EVERYTHING AFTER SHOOTING
                    intake.set_Power(0);
                    intake.rollerpower(0);
                    outtake.stop();
                    state = 99;
                }

                break;


            case 99:
                drive.stop();
                outtake.stop();  // 🔥 гарантируем выключение
                break;
        }

        outtake.update();

        telemetry.addData("State", state);
        telemetry.addData("Match Time", matchTimer.seconds());
        telemetry.update();
    }
}