package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.auto.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.auto.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Data;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Lift;
import org.firstinspires.ftc.teamcode.subsystems.OuttakeDifferential;
import org.firstinspires.ftc.teamcode.vision.PropDetectionProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.ArrayList;
import java.util.List;

//@Disabled
@Autonomous(name="Blue Audience-Side Auto", group="Auto")
public class BlueFarAuto extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Vision
    private PropDetectionProcessor propDetector;
    private VisionPortal visionPortal;

    Lift lift;
    Intake intake;
    OuttakeDifferential outtake;

    PropDetectionProcessor.Location location;

    final int STACK_TIME = 3; // seconds

    @Override
    public void runOpMode() throws InterruptedException {

        // ----- HARDWARE INITIALIZATION ---- //

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        propDetector = new PropDetectionProcessor();
        propDetector.propColor = PropDetectionProcessor.Prop.BLUE;
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "CAM"), propDetector);

        lift = new Lift(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        outtake = new OuttakeDifferential(hardwareMap, telemetry, OuttakeDifferential.State.DOWN);

        lift.setManual(false);
        intake.setSpeed(0.8);

        // ----- TRAJECTORIES ----- //

        Pose2d startPose = new Pose2d(-40, 63, Math.toRadians(90));
        drive.setPoseEstimate(startPose);

        // LEFT ------------------- //

        Trajectory toSpikeMarkLeft = drive.trajectoryBuilder(startPose, true)
                .splineToLinearHeading(new Pose2d(-27, 37, Math.toRadians(130)), Math.toRadians(315),
                        SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toBackToStackLeft = drive.trajectoryBuilder(toSpikeMarkLeft.end())
                .forward(9)
                .build();

        Trajectory toStackLeft = drive.trajectoryBuilder(toBackToStackLeft.end(), true)
                .splineToLinearHeading(new Pose2d(-57.8, 35, Math.toRadians(0)), Math.toRadians(180),
                        SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toBackdropLeft = drive.trajectoryBuilder(toStackLeft.end())
                .splineToConstantHeading(new Vector2d(-54, 54), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, 54), Math.toRadians(0))
                .build();

        Trajectory toScoreLeft = drive.trajectoryBuilder(toBackdropLeft.end())
                .splineToConstantHeading(new Vector2d(45, 39), Math.toRadians(0),
                        SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toParkLeft = drive.trajectoryBuilder(toScoreLeft.end(), true)
                .splineToLinearHeading(new Pose2d(45, 40, Math.toRadians(90)), Math.toRadians(0))
                .build();

        // CENTER ------------------- //
        Trajectory toSpikeMarkCenter = drive.trajectoryBuilder(startPose, true)
                .splineToConstantHeading(new Vector2d(-40, 30), Math.toRadians(90),
                        SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toBackToStackCenter = drive.trajectoryBuilder(toSpikeMarkCenter.end())
                .forward(8)
                .build();

        Trajectory toStackCenter = drive.trajectoryBuilder(toBackToStackCenter.end(), true)
                .splineToLinearHeading(new Pose2d(-58, 34.5, Math.toRadians(0)), Math.toRadians(180),
                        SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toBackdropCenter = drive.trajectoryBuilder(toStackCenter.end())
                .splineToConstantHeading(new Vector2d(-54, 54), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, 54), Math.toRadians(0))
                .build();

        Trajectory toScoreCenter = drive.trajectoryBuilder(toBackdropCenter.end())
                .splineToConstantHeading(new Vector2d(43, 34), Math.toRadians(0),
                        SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toParkCenter = drive.trajectoryBuilder(toScoreCenter.end(), true)
                .splineToLinearHeading(new Pose2d(45, 40, Math.toRadians(90)), Math.toRadians(0))
                .build();

        // RIGHT ------------------- //

        Trajectory toSpikeMarkRight = drive.trajectoryBuilder(startPose, true)
                .splineToLinearHeading(new Pose2d(-42.5, 31, Math.toRadians(50)), Math.toRadians(200),
                        SampleMecanumDrive.getVelocityConstraint(40, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toBackToStackRight = drive.trajectoryBuilder(toSpikeMarkRight.end())
                .lineToConstantHeading(new Vector2d(-37, 48))
                .build();

        Trajectory toStackRight = drive.trajectoryBuilder(toBackToStackRight.end(), true)
                .splineToLinearHeading(new Pose2d(-59.5, 35, Math.toRadians(0)), Math.toRadians(180),
                        SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toBackdropRight = drive.trajectoryBuilder(toStackRight.end())
                .splineToConstantHeading(new Vector2d(-54, 55), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, 55), Math.toRadians(0))
                .build();

        Trajectory toScoreRight = drive.trajectoryBuilder(toBackdropRight.end())
                .splineToConstantHeading(new Vector2d(43, 33), Math.toRadians(0),
                        SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toParkRight = drive.trajectoryBuilder(toScoreRight.end(), true)
                .splineToLinearHeading(new Pose2d(45, 40, Math.toRadians(90)), Math.toRadians(0))
                .build();


        // ----- WAIT FOR START ----- //

        while (!isStarted() && !isStopRequested()) {
            telemetry.addLine(String.valueOf(propDetector.getLocation()));
            telemetry.update();

            location = propDetector.getLocation();
        }

        // ----- START PRESSED ----- //

        runtime.reset();

        if (isStopRequested()) return;

        // ----- RUNNING OP-MODE ----- //
        intake.setTargetPositionPreset(Intake.Position.DOWN);
        intake.update();
        sleep(1500);

        if (location == PropDetectionProcessor.Location.Left) {
            // Location.Left
            drive.setPoseEstimate(startPose);
            drive.followTrajectory(toSpikeMarkLeft);

            // drop purple pixel
            intake.setTargetPositionPreset(Intake.Position.P5);
            intake.update();
            sleep(30000);

            // get white pixel
            drive.followTrajectory(toBackToStackLeft);
            intake.spinForward();
            outtake.box.intake();
            drive.followTrajectory(toStackLeft);
            drive.setMotorPowers(-0.1, -0.1, -0.1, -0.1);
            sleep(600);
            drive.setMotorPowers(0, 0, 0, 0);
            long curr = System.currentTimeMillis();
            while (System.currentTimeMillis() - curr < 1000 * STACK_TIME && opModeIsActive()) {
                if (outtake.boxIsFull()) break;
            }
            intake.spinBackwards();
            sleep(1500);
            intake.stopSpin();
            outtake.box.stopSpinning();

            // drop yellow pixel
            drive.followTrajectory(toBackdropLeft);
            new Thread(() -> {
                outtake.goTo(OuttakeDifferential.State.UP, true);
                while (lift.getEncoderValue() < 150 && opModeIsActive()) {
                    lift.setPower(0.8);
                }
                lift.setPower(0.1);
            }).start();
            drive.followTrajectory(toScoreLeft);
            drive.setMotorPowers(0.25, 0.25, 0.25, 0.25);
            curr = System.currentTimeMillis();
            while (System.currentTimeMillis() - curr < 3500 && opModeIsActive()) {
                if (outtake.boxIsEmpty()) break;
            }
            sleep(1000);
            drive.setMotorPowers(0, 0, 0, 0);

            new Thread(() -> {
                sleep(200);
                while (lift.getEncoderValue() > 10 && opModeIsActive()) {
                    lift.setPower(-0.5);
                }
                lift.setPower(0);
                outtake.goTo(OuttakeDifferential.State.DOWN, true);
                sleep(2000);
            }).start();

            drive.followTrajectory(toParkLeft);
        } else if (location == PropDetectionProcessor.Location.Center) {
            // Location.Center
            drive.setPoseEstimate(startPose);
            drive.followTrajectory(toSpikeMarkCenter);

            // drop purple pixel
            intake.setTargetPositionPreset(Intake.Position.P5);
            intake.update();
            sleep(30000);

            // get white pixel
            drive.followTrajectory(toBackToStackCenter);
            intake.spinForward();
            outtake.box.intake();
            drive.followTrajectory(toStackCenter);
            drive.setMotorPowers(-0.1, -0.1, -0.1, -0.1);
            sleep(500);
            drive.setMotorPowers(0, 0, 0, 0);
            long curr = System.currentTimeMillis();
            while (System.currentTimeMillis() - curr < 1000 * STACK_TIME && opModeIsActive()) {
                if (outtake.boxIsFull()) break;
            }
            intake.spinBackwards();
            sleep(1500);
            intake.stopSpin();
            outtake.box.stopSpinning();

            // drop yellow pixel
            drive.followTrajectory(toBackdropCenter);
            new Thread(() -> {
                outtake.goTo(OuttakeDifferential.State.UP, true);
                while (lift.getEncoderValue() < 150 && opModeIsActive()) {
                    lift.setPower(0.8);
                }
                lift.setPower(0.1);
            }).start();
            drive.followTrajectory(toScoreCenter);
            drive.setMotorPowers(0.25, 0.25, 0.25, 0.25);
            curr = System.currentTimeMillis();
            while (System.currentTimeMillis() - curr < 3500 && opModeIsActive()) {
                if (outtake.boxIsEmpty()) break;
            }
            sleep(1000);
            drive.setMotorPowers(0, 0, 0, 0);

            new Thread(() -> {
                sleep(200);
                while (lift.getEncoderValue() > 10 && opModeIsActive()) {
                    lift.setPower(-0.5);
                }
                lift.setPower(0);
                outtake.goTo(OuttakeDifferential.State.DOWN, true);
                sleep(2000);
            }).start();

            drive.followTrajectory(toParkCenter);
        } else {
            // Location.Right
            drive.setPoseEstimate(startPose);
            drive.followTrajectory(toSpikeMarkRight);

            // drop purple pixel
            intake.setTargetPositionPreset(Intake.Position.P5);
            intake.update();
            sleep(30000);

            // get white pixel
            drive.followTrajectory(toBackToStackRight);
            intake.spinForward();
            outtake.box.intake();
            drive.followTrajectory(toStackRight);
            drive.setMotorPowers(-0.1, -0.1, -0.1, -0.1);
            sleep(500);
            drive.setMotorPowers(0, 0, 0, 0);
            long curr = System.currentTimeMillis();
            while (System.currentTimeMillis() - curr < 1000 * STACK_TIME && opModeIsActive()) {
                if (outtake.boxIsFull()) break;
            }
            intake.spinBackwards();
            sleep(30000);
            intake.stopSpin();
            outtake.box.stopSpinning();

            // drop yellow pixel
            drive.followTrajectory(toBackdropRight);
            new Thread(() -> {
                outtake.goTo(OuttakeDifferential.State.UP, true);
                while (lift.getEncoderValue() < 150 && opModeIsActive()) {
                    lift.setPower(0.8);
                }
                lift.setPower(0.1);
            }).start();
            drive.followTrajectory(toScoreRight);
            drive.setMotorPowers(0.25, 0.25, 0.25, 0.25);
            curr = System.currentTimeMillis();
            while (System.currentTimeMillis() - curr < 3500 && opModeIsActive()) {
                if (outtake.boxIsEmpty()) break;
            }
            sleep(1000);
            drive.setMotorPowers(0, 0, 0, 0);

            new Thread(() -> {
                sleep(200);
                while (lift.getEncoderValue() > 10 && opModeIsActive()) {
                    lift.setPower(-0.5);
                }
                lift.setPower(0);
                outtake.goTo(OuttakeDifferential.State.DOWN, true);
                sleep(2000);
            }).start();

            drive.followTrajectory(toParkRight);
        }

        Data.liftPosition = lift.getEncoderValue();
        visionPortal.close();

        while (opModeIsActive()) {
        }
    }
}