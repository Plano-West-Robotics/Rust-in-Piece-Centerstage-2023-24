package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {
    private final Telemetry telemetry;

    private final DcMotor slideLeft;
    private final DcMotor slideRight;

    private boolean manual;
    private double speed;
    private int targetPosition;

    enum Position {
        BOTTOM,
        MEDIUM,
        HIGH
    }
    // THIS IS A CONSTRUCTOR

    public Lift(HardwareMap hardwareMap, Telemetry telemetry) {
        slideLeft = hardwareMap.get(DcMotor.class, "SLmotor");
        slideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        slideRight = hardwareMap.get(DcMotor.class, "SRmotor");
        slideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        slideRight.setDirection(DcMotor.Direction.REVERSE);

        manual = false;
        // a value between 0 and 1 where 1 is max speed
        speed = 1;

        setTargetPositionPreset(Position.BOTTOM);

        this.telemetry = telemetry;
    }

    //region Lift
    public void setManual(boolean manual) {
        this.manual = manual;
    }

    // TODO: add both slide powers
    public void setPower(double power) {
        slideLeft.setPower(power * speed);
        slideRight.setPower(power * speed);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setTargetPositionPreset(Position targetPosition) {
        switch (targetPosition) {
            case BOTTOM: setTargetPosition(-15); break; // Generally values from 0 to 1000
            case MEDIUM: setTargetPosition(500); break;
            case HIGH: setTargetPosition(1000); break;
        }
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }

    /** calculates the power that the slide needs
     *
     * @param currentPosition
     * @return
     */
    public double calculate(int currentPosition) {

        int distance = Math.abs(targetPosition - currentPosition);
        if (distance < 20) {
            return 0.01;
        } else if (distance < 100) {
            if (targetPosition < currentPosition) {
                return -0.4;
            }
            return 0.4;
        } else {
            if (targetPosition < currentPosition) {
                return -1;
            }
            return 1;
        }
    }

    public void update(double joystick) {
        if (manual) {
            setPower(joystick); // Maybe negative, depends on the motor
        } else {
            setPower(calculate(slideRight.getCurrentPosition()));
        }
    }

    public double getEncoderValue() {
        return slideRight.getCurrentPosition();
    }
    //endregion
}
