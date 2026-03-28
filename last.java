package org.firstinspires.ftc.teamcode.pedroPathing.Autons.SDU;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Controllers.MecanumDrive;

@Autonomous(name = "Auto: Stay then Forward 0.5s", group = "Autonomous")
public class last extends LinearOpMode {

    MecanumDrive drive = null;
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        drive = new MecanumDrive(hardwareMap);

        telemetry.addLine("Ready. Will wait until 25s mark.");
        telemetry.update();

        waitForStart();
        runtime.reset(); // Таймер пошел от нажатия кнопки PLAY

        // =======================================================
        // 1. ОЖИДАНИЕ (Робот просто стоит 25 секунд)
        // =======================================================
        while (opModeIsActive() && runtime.seconds() < 25.0) {
            drive.drive(0, 0, 0); // Моторы выключены
            telemetry.addData("Status", "Waiting...");
            telemetry.addData("Time", "%.1f / 25.0", runtime.seconds());
            telemetry.update();
        }

        // =======================================================
        // 2. ДВИЖЕНИЕ ВПЕРЕД (Ровно 0.5 секунды)
        // =======================================================
        // Едем с 25.0 до 25.5 секунд
        while (opModeIsActive() && runtime.seconds() < 25.5) {
            drive.drive(0.5, 0, 0); // Едем вперед
            telemetry.addData("Status", "Last Second Move!");
            telemetry.update();
        }

        // =======================================================
        // 3. ПОЛНАЯ ОСТАНОВКА
        // =======================================================
        drive.drive(0, 0, 0);
        telemetry.addData("Status", "Done. Parked.");
        telemetry.update();

        // Держим программу активной до конца матча
        while (opModeIsActive()) {
            idle();
        }
    }
}