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

@Autonomous(name="Roadrunner Red Backdrop-Side Auto", group="Auto")
public class RoadrunnerRedCloseAuto extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Vision
    private PropDetectionProcessor propDetector;
    private VisionPortal visionPortal;

    Lift lift;
    Intake intake;
    OuttakeDifferential outtake;

    PropDetectionProcessor.Location location;

    @Override
    public void runOpMode() throws InterruptedException {

        // ----- HARDWARE INITIALIZATION ---- //

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        propDetector = new PropDetectionProcessor();
        propDetector.propColor = PropDetectionProcessor.Prop.RED;
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "CAM"), propDetector);

        lift = new Lift(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        outtake = new OuttakeDifferential(hardwareMap, telemetry, OuttakeDifferential.State.DOWN);

        lift.setManual(false);

        // ----- TRAJECTORIES ----- //

        Pose2d startPose = new Pose2d(12, -60, 270);
        drive.setPoseEstimate(startPose);

        Trajectory moveBack = drive.trajectoryBuilder(startPose)
                .back(5)
                .build();

        startPose = new Pose2d(12, -60, 0);

        // LEFT ------------------- //

        Trajectory toBackdropLeft = drive.trajectoryBuilder(startPose)
                .splineToConstantHeading(new Vector2d(48, -34), 0)
                .splineToConstantHeading(new Vector2d(52, -34), 0,
                        SampleMecanumDrive.getVelocityConstraint(6, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toSpikeMarkLeft = drive.trajectoryBuilder(toBackdropLeft.end(), true)
                .splineToConstantHeading(new Vector2d(7, -29), Math.toRadians(180))
                .build();

        Trajectory toParkLeft = drive.trajectoryBuilder(toSpikeMarkLeft.end())
                .splineToLinearHeading(new Pose2d(45, -60, Math.toRadians(90)), Math.toRadians(0))
                .build();

        // CENTER ------------------- //

        Trajectory toBackdropCenter = drive.trajectoryBuilder(startPose)
                .splineToConstantHeading(new Vector2d(48, -39), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(52, -39), Math.toRadians(0),
                        SampleMecanumDrive.getVelocityConstraint(6, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();



        Trajectory toSpikeMarkCenter = drive.trajectoryBuilder(toBackdropCenter.end(), true)
                .splineToConstantHeading(new Vector2d(14, -30), Math.toRadians(180))
                .build();

        Trajectory toParkCenter = drive.trajectoryBuilder(toSpikeMarkCenter.end())
                .splineToLinearHeading(new Pose2d(45, -60, Math.toRadians(90)), Math.toRadians(0))
                .build();

        // RIGHT ------------------- //

        Trajectory toBackdropRight = drive.trajectoryBuilder(startPose)
                .splineToConstantHeading(new Vector2d(48, -43), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(52, -43), Math.toRadians(0),
                        SampleMecanumDrive.getVelocityConstraint(8, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        Trajectory toSpikeMarkRight = drive.trajectoryBuilder(toBackdropRight.end(), true)
                .splineToConstantHeading(new Vector2d(27, -29), Math.toRadians(180))
                .build();

        Trajectory toParkRight = drive.trajectoryBuilder(toSpikeMarkRight.end())
                .splineToLinearHeading(new Pose2d(45, -60, Math.toRadians(90)), Math.toRadians(0))
                .build();

        // ----- WAIT FOR START ----- //

        while (!isStarted() && !isStopRequested()) {
            telemetry.addLine(String.valueOf(propDetector.getLocation()));
            telemetry.update();

            location = propDetector.getLocation();
        }

        // ----- START PRESSED ----- //

        runtime.reset();

        if(isStopRequested()) return;

        // ----- RUNNING OP-MODE ----- //

        if (location == PropDetectionProcessor.Location.Left) {
            // Location.Left
            drive.followTrajectory(moveBack);
            drive.turn(Math.toRadians(95));

            outtake.goTo(OuttakeDifferential.State.UP);

            drive.setPoseEstimate(startPose);
            drive.followTrajectory(toBackdropLeft);

            // drop yellow pixel
            outtake.goTo(OuttakeDifferential.State.DOWN);

            drive.followTrajectory(toSpikeMarkLeft);

            // drop purple pixel
            intake.setSpeed(0.35);
            intake.spinBackwards();
            sleep(2000);
            intake.stopSpin();

            drive.followTrajectory(toParkLeft);
        } else if (location == PropDetectionProcessor.Location.Center) {
            // Location.Center
            drive.followTrajectory(moveBack);
            drive.turn(Math.toRadians(95));

            outtake.goTo(OuttakeDifferential.State.UP);

            drive.setPoseEstimate(startPose);
            drive.followTrajectory(toBackdropCenter);

            // drop yellow pixel
            outtake.goTo(OuttakeDifferential.State.DOWN);

            drive.followTrajectory(toSpikeMarkCenter);

            // drop purple pixel
            intake.setSpeed(0.35);
            intake.spinBackwards();
            sleep(2000);
            intake.stopSpin();

            drive.followTrajectory(toParkCenter);
        } else {
            // Location.Right
            drive.followTrajectory(moveBack);
            drive.turn(Math.toRadians(95));

            outtake.goTo(OuttakeDifferential.State.UP);

            drive.setPoseEstimate(startPose);
            drive.followTrajectory(toBackdropRight);

            // drop yellow pixel
            outtake.goTo(OuttakeDifferential.State.DOWN);

            drive.followTrajectory(toSpikeMarkRight);

            // drop purple pixel
            intake.setSpeed(0.35);
            intake.spinBackwards();
            sleep(2000);
            intake.stopSpin();

            drive.followTrajectory(toParkRight);
        }

        Data.liftPosition = lift.getEncoderValue();
        visionPortal.close();
    }
}
