package org.firstinspires.ftc.teamcode.Hardware; // Путь согласно структуре на скриншоте

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// Импортируем драйвер из правильной папки Hardware
import org.firstinspires.ftc.teamcode.Hardware.GoBildaPinpointDriver;

@TeleOp(name = "Pinpoint Only TeleOp", group = "Sensor")
public class MegaTag2Pinpoint extends LinearOpMode {

    private GoBildaPinpointDriver odo;

    @Override
    public void runOpMode() throws InterruptedException {
        // Инициализация Pinpoint
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "Pinpoint");

        // Настройки подов (проверьте ваши параметры)
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);

        odo.recalibrateIMU();
        odo.resetPosAndIMU();

        waitForStart();

        while (opModeIsActive()) {
            odo.update(); // Обновляем данные

            // Получаем данные напрямую из одометрии
            double x = odo.getPosX();
            double y = odo.getPosY();
            double heading = Math.toDegrees(odo.getHeading());

            telemetry.addData("X (Inches)", "%.2f", x);
            telemetry.addData("Y (Inches)", "%.2f", y);
            telemetry.addData("Heading", "%.2f", heading);
            telemetry.update();
        }
    }
}