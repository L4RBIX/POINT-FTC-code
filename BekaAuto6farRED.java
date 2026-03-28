package org.firstinspires.ftc.teamcode.pedroPathing.Autons.SDU;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Controllers.Intake;
import org.firstinspires.ftc.teamcode.Controllers.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "0+6 RED", group = "blue")
public class BekaAuto6farRED extends OpMode {

    private Follower follower;
    private Outtake outtake;
    private Timer pathTimer, actionTimer, opmodeTimer;
    Intake intake = null;
    private int pathState;

    private final Pose startPose    = new Pose(89.76,   7.82,   Math.toRadians(90));
    private final Pose shootingpose = new Pose(85.737,  16.105, Math.toRadians(113));
    private final Pose hp           = new Pose(132.026, 19.526, Math.toRadians(220));
    private final Pose hp2          = new Pose(133.658, 7.289,  Math.toRadians(180));
    private final Pose hp3          = new Pose(127.658, 7.289,  Math.toRadians(180));
    private final Pose parkingpose  = new Pose(86.947,  33.500, Math.toRadians(90));

    public static Pose endpose = new Pose(86.947, 33.500, Math.toRadians(90));

    public PathChain toshootpreload;
    public PathChain tohp;
    public PathChain eathp;
    public PathChain toshoot1;
    public PathChain park;

    ElapsedTime timer      = new ElapsedTime();
    ElapsedTime shootTimer = new ElapsedTime();

    public void buildPaths() {

        toshootpreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootingpose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootingpose.getHeading())
                .build();

        tohp = follower.pathBuilder()
                .addPath(new BezierLine(shootingpose, hp))
                .addParametricCallback(0, () -> {
                    intake.set_Power(-1);
                    intake.rollerpower(-1);
                })
                .setLinearHeadingInterpolation(shootingpose.getHeading(), hp.getHeading())
                .build();

        eathp = follower.pathBuilder()
                .addPath(new BezierLine(hp,  hp2)).setLinearHeadingInterpolation(hp.getHeading(),  hp2.getHeading())
                .addPath(new BezierLine(hp2, hp3)).setLinearHeadingInterpolation(hp2.getHeading(), hp3.getHeading())
                .addPath(new BezierLine(hp3, hp2)).setLinearHeadingInterpolation(hp3.getHeading(), hp2.getHeading())
                .addPath(new BezierLine(hp2, hp3)).setLinearHeadingInterpolation(hp2.getHeading(), hp3.getHeading())
                .addPath(new BezierLine(hp3, hp2)).setLinearHeadingInterpolation(hp3.getHeading(), hp2.getHeading())
                .build();

        toshoot1 = follower.pathBuilder()
                .addPath(new BezierLine(hp2, shootingpose))
                .addParametricCallback(0, () -> {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                    outtake.setHighRangeauto();
                    outtake.setangle(-43);
                })
                .setLinearHeadingInterpolation(hp2.getHeading(), shootingpose.getHeading())
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(shootingpose, parkingpose))
                .setLinearHeadingInterpolation(shootingpose.getHeading(), parkingpose.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                outtake.setHighRangeauto();
                timer.reset();
                follower.followPath(toshootpreload);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()) {
                    follower.holdPoint(shootingpose);
                    outtake.setHighRangeauto();
                    shootTimer.reset();
                    setPathState(11);
                }
                break;

            case 11: {
                double t = shootTimer.seconds();
                if (t < 3.5) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else if (t < 3.65) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                } else if (t < 5.25) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else if (t < 5.45) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                } else if (t < 7.35) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else if (t < 8.05) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                } else if (t < 8.95) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                    outtake.stop();
                    outtake.setangle(43);
                    follower.followPath(tohp, true);
                    setPathState(2);
                }
                break;
            }

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(eathp, true);
                    follower.setMaxPower(0.5);
                    setPathState(3);
                }
                break;


            case 3:
                if (timer.milliseconds() > 15000) {
                    follower.pausePathFollowing();
                    follower.setMaxPower(1);
                    follower.followPath(toshoot1, true);
                    setPathState(4);
                    break;
                }
                if (!follower.isBusy()) {
                    follower.setMaxPower(1);
                    sleep(1500);
                    follower.followPath(toshoot1, true);
                    setPathState(4);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    follower.holdPoint(shootingpose);
                    outtake.setHighRangeauto();
                    outtake.setangle(-43);
                    shootTimer.reset();
                    setPathState(41);
                }
                break;

            case 41: {
                double t = shootTimer.seconds();
                if (t < 3.5) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else if (t < 3.65) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                } else if (t < 5.25) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else if (t < 5.45) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                } else if (t < 7.35) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else if (t < 8.05) {
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                } else if (t < 8.95) {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                } else {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                    outtake.stop();
                    outtake.setangle(43);
                    sleep(26000 - timer.milliseconds());
                    follower.followPath(park, true);
                    setPathState(-1);
                }
                break;
            }
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {
        outtake.update();
        follower.update();
        autonomousPathUpdate();
        endpose = follower.getPose();
        telemetry.addData("path state",  pathState);
        telemetry.addData("x",           follower.getPose().getX());
        telemetry.addData("y",           follower.getPose().getY());
        telemetry.addData("heading",     follower.getPose().getHeading());
        telemetry.addData("timer ms",    timer.milliseconds());
        telemetry.addData("shootTimer",  shootTimer.seconds());
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer   = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();
        intake   = new Intake(hardwareMap);
        outtake  = new Outtake(hardwareMap, telemetry, gamepad1);
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
        outtake.start();
        outtake.setangle(-43);
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {}

    ElapsedTime timersleep;
    public void sleep(double m) {
        timersleep = new ElapsedTime();
        timersleep.reset();
        while (timersleep.milliseconds() <= m) {
            follower.update();
            outtake.update();
            telemetry.update();
            endpose = follower.getPose();
        }
    }
}