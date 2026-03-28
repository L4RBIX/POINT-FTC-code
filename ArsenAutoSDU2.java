package org.firstinspires.ftc.teamcode.pedroPathing.Autons.SDU;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Controllers.Intake;
import org.firstinspires.ftc.teamcode.Controllers.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "1 RED PLAYOFF")
public class ArsenAutoSDU2 extends OpMode {

    private Follower follower;
    private Outtake outtake;
    private Timer pathTimer, actionTimer, opmodeTimer;
    Intake intake = null;
    private int pathState;

    private final Pose startPose    = new Pose(117.3,   132.84, Math.toRadians(-53));
    private final Pose shootingpose = new Pose(96.831,  97.598,  Math.toRadians(40));
    private final Pose l2p          = new Pose(92.831,  60.632,  Math.toRadians(0));
    private final Pose l2ep         = new Pose(129.5,   60.632,  Math.toRadians(0));
    private final Pose gatep        = new Pose(125,     71,      Math.toRadians(0));
    private final Pose cp1          = new Pose(103,     70,      Math.toRadians(0));
    private final Pose cp2          = new Pose(86.093,  59.824,  Math.toRadians(0));
    private final Pose l1p          = new Pose(99.022,  84.984,  Math.toRadians(0));
    private final Pose l1ep         = new Pose(120.400, 84.984,  Math.toRadians(0));
    private final Pose l3p          = new Pose(98.639,  38.472,  Math.toRadians(0));
    private final Pose l3ep         = new Pose(130,     38.472,  Math.toRadians(0));
    private final Pose parkingpose  = new Pose(124,     97,      Math.toRadians(90));

    public static Pose endpose = new Pose(124, 97, Math.toRadians(90));

    public PathChain toshootpreload;
    public PathChain to2ndline;
    public PathChain eat2ndline;
    public PathChain opengate;
    public PathChain toshoot1;
    public PathChain to1stline;
    public PathChain eat1stline;
    public PathChain toshoot2;
    public PathChain to3rdline;
    public PathChain eat3rdline;
    public PathChain toshoot3;
    public PathChain park;

    ElapsedTime shootTimer = new ElapsedTime();

    public void buildPaths() {

        toshootpreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, new Pose(shootingpose.getX() + 5, shootingpose.getY() + 5)))
                .addParametricCallback(0, () -> {
                    outtake.setMidRange();
                    outtake.setangle(34);
                })
                .setLinearHeadingInterpolation(startPose.getHeading(), Math.toRadians(30))
                .build();

        to2ndline = follower.pathBuilder()
                .addPath(new BezierLine(shootingpose, new Pose(l2p.getX(), l2p.getY() + 8)))
                .addParametricCallback(0, () -> {
                    outtake.stop();
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                })
                .setLinearHeadingInterpolation(shootingpose.getHeading(), Math.toRadians(10))
                .addPath(new BezierLine(new Pose(l2p.getX(), l2p.getY()), l2ep))
                .setLinearHeadingInterpolation(l2p.getHeading(), l2ep.getHeading())
                .addParametricCallback(1, () -> {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                })
                .build();

        eat2ndline = follower.pathBuilder()
                .addPath(new BezierLine(l2p, l2ep))
                .setLinearHeadingInterpolation(l2p.getHeading(), l2ep.getHeading())
                .addParametricCallback(1, () -> {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                })
                .addPath(new BezierCurve(l2ep, cp1, gatep))
                .setLinearHeadingInterpolation(l2ep.getHeading(), gatep.getHeading())
                .build();

        opengate = follower.pathBuilder()
                .addPath(new BezierCurve(l2ep, cp1, gatep))
                .addParametricCallback(0, () -> {
                    intake.set_Power(0);
                    intake.rollerpower(0);
                })
                .setLinearHeadingInterpolation(l2ep.getHeading(), gatep.getHeading())
                .build();

        toshoot1 = follower.pathBuilder()
                .addPath(new BezierLine(l2ep, new Pose(l2ep.getX() - 15, l2ep.getY())))
                .setLinearHeadingInterpolation(gatep.getHeading(), Math.toRadians(0))
                .addPath(new BezierCurve(
                        new Pose(l2ep.getX() - 15, l2ep.getY()),
                        cp2,
                        new Pose(shootingpose.getX() - 1, shootingpose.getY() - 8)
                ))
                .addParametricCallback(0, () -> {
                    outtake.setMidRange();
                    outtake.setangle(34);
                })
                .setLinearHeadingInterpolation(gatep.getHeading(), Math.toRadians(144))
                .build();

        to1stline = follower.pathBuilder()
                .addPath(new BezierLine(shootingpose, new Pose(l1p.getX(), l1p.getY() + 3)))
                .addParametricCallback(0, () -> {
                    outtake.stop();
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                })
                .setLinearHeadingInterpolation(shootingpose.getHeading(), Math.toRadians(7))
                .build();

        eat1stline = follower.pathBuilder()
                .addPath(new BezierLine(l1p, l1ep))
                .setLinearHeadingInterpolation(l1p.getHeading(), l1ep.getHeading())
                .build();

        toshoot2 = follower.pathBuilder()
                .addPath(new BezierLine(l1ep, new Pose(shootingpose.getX() + 4, shootingpose.getY() - 2)))
                .addParametricCallback(0, () -> {
                    outtake.setMidRange();
                    outtake.setangle(34);
                })
                .setLinearHeadingInterpolation(l1ep.getHeading(), Math.toRadians(36))
                .build();

        to3rdline = follower.pathBuilder()
                .addPath(new BezierLine(shootingpose, new Pose(l3p.getX(), l3p.getY() + 6)))
                .addParametricCallback(0, () -> {
                    outtake.stop();
                    intake.set_Power(-1);
                    intake.rollerpower(1);
                })
                .setLinearHeadingInterpolation(shootingpose.getHeading(), l3p.getHeading())
                .build();

        eat3rdline = follower.pathBuilder()
                .addPath(new BezierLine(l3p, l3ep))
                .setLinearHeadingInterpolation(l3p.getHeading(), l3ep.getHeading())
                .build();

        toshoot3 = follower.pathBuilder()
                .addPath(new BezierLine(l3ep, new Pose(shootingpose.getX() + 2, shootingpose.getY() - 8)))
                .addParametricCallback(0, () -> {
                    outtake.setMidRange();
                    outtake.setangle(34);
                })
                .setLinearHeadingInterpolation(l3ep.getHeading(), Math.toRadians(36))
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(shootingpose, parkingpose))
                .setLinearHeadingInterpolation(shootingpose.getHeading(), parkingpose.getHeading())
                .build();
    }

    // прогрев 2с → 3 импульса по 0.35с с паузой 0.3с
    private boolean shootSequence(double t) {
        if (t < 2.0) {
            intake.set_Power(0);
            intake.rollerpower(0);
        } else if (t < 2.35) {
            intake.set_Power(-0.6);
            intake.rollerpower(-1);
        } else if (t < 2.65) {
            intake.set_Power(0);
            intake.rollerpower(0);
        } else if (t < 3.0) {
            intake.set_Power(-0.6);
            intake.rollerpower(-1);
        } else if (t < 3.3) {
            intake.set_Power(0);
            intake.rollerpower(0);
        } else if (t < 3.65) {
            intake.set_Power(-0.6);
            intake.rollerpower(-1);
        } else if (t < 4.0) {
            intake.set_Power(0);
            intake.rollerpower(0);
        } else {
            intake.set_Power(0);
            intake.rollerpower(0);
            outtake.stop();
            return true;
        }
        return false;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {

            case 0:
                follower.followPath(toshootpreload);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()) {
                    follower.holdPoint(shootingpose);
                    outtake.setMidRange();
                    outtake.setangle(34);
                    shootTimer.reset();
                    setPathState(11);
                }
                break;

            case 11:
                if (shootSequence(shootTimer.seconds())) {
                    follower.followPath(to2ndline, true);
                    setPathState(4);
                }
                break;

            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(eat2ndline, true);
                    setPathState(3);
                }
                break;

            case 3:
                intake.set_Power(-1);
                intake.rollerpower(1);
                if (!follower.isBusy()) {
                    sleep(500);
                    intake.set_Power(0);
                    intake.rollerpower(0);
                    follower.followPath(opengate, true);
                    setPathState(5);
                }
                break;

            case 4:
                if (!follower.isBusy()) {
                    sleep(500);
                    follower.followPath(toshoot1, true);
                    setPathState(5);
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    follower.holdPoint(shootingpose);
                    outtake.setMidRange();
                    outtake.setangle(34);
                    shootTimer.reset();
                    setPathState(51);
                }
                break;

            case 51:
                if (shootSequence(shootTimer.seconds())) {
                    follower.followPath(to1stline, true);
                    setPathState(6);
                }
                break;

            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(eat1stline, true);
                    setPathState(7);
                }
                break;

            case 7:
                if (!follower.isBusy()) {
                    sleep(500);
                    follower.followPath(toshoot2, true);
                    setPathState(8);
                }
                break;

            case 8:
                if (!follower.isBusy()) {
                    follower.holdPoint(shootingpose);
                    outtake.setMidRange();
                    outtake.setangle(34);
                    shootTimer.reset();
                    setPathState(81);
                }
                break;

            case 81:
                if (shootSequence(shootTimer.seconds())) {
                    follower.followPath(to3rdline, true);
                    setPathState(9);
                }
                break;

            case 9:
                if (!follower.isBusy()) {
                    follower.followPath(eat3rdline, true);
                    setPathState(10);
                }
                break;

            case 10:
                if (!follower.isBusy()) {
                    sleep(500);
                    follower.followPath(toshoot3, true);
                    setPathState(111);
                }
                break;

            case 111:
                if (!follower.isBusy()) {
                    follower.holdPoint(shootingpose);
                    outtake.setMidRange();
                    outtake.setangle(34);
                    shootTimer.reset();
                    setPathState(112);
                }
                break;

            case 112:
                if (shootSequence(shootTimer.seconds())) {
                    follower.followPath(park, true);
                    setPathState(12);
                }
                break;

            case 12:
                if (!follower.isBusy()) {
                    follower.holdPoint(parkingpose);
                    setPathState(-1);
                }
                break;
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
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
        outtake.start();
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