package model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceLander implements Spacecraft {
    static final double FALL_ACCELERATION = 9.8;
    static final int DEADLY_LANDING_SPEED = 5;

    double flyHeight;
    double fuelReserve;
    double currentFuelUsage;
    double fallSpeed;
    boolean isDead;

    /* выполнение одного шага игры, который включает в себя:
        - расходование топлива;
        - изменение параметров - скорости падения и высоты;
        - проверку, остался ли в живых экипаж
     */
    @Override
    public void motion() {
        spendFuel();
        changeFlyParams();
        survivalCheck();
    }

    public SpaceLanderState toState() {
        return new SpaceLanderState(roundToTwoDecPlaces(getFlyHeight()),
                roundToTwoDecPlaces(getFuelReserve()),
                roundToTwoDecPlaces(getCurrentFuelUsage()),
                roundToTwoDecPlaces(getFallSpeed()),
                isDead());
    }

    // округляет значения до двух знаков после запятой
    private double roundToTwoDecPlaces(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    private void spendFuel() {
        if (fuelReserve >= currentFuelUsage) {
            fuelReserve -= currentFuelUsage;
        } else {
            currentFuelUsage = fuelReserve;
            fuelReserve = 0;
        }
    }

    private void changeFlyParams() {
        double startFallSpeed = fallSpeed;
        fallSpeed += (FALL_ACCELERATION - currentFuelUsage);
        double middleFallSpeed = (startFallSpeed + fallSpeed) / 2;
        flyHeight -= middleFallSpeed;
    }

    private void survivalCheck() {
        if (flyHeight <= 0 && calculateLandingSpeed() > 5)
            isDead = true;
    }

    private double calculateLandingSpeed() {
        double startFallSpeed = fallSpeed - (FALL_ACCELERATION - currentFuelUsage);
        double middleFallSpeed = (startFallSpeed + fallSpeed) / 2;
        double startFlyHeight = flyHeight + middleFallSpeed;

        double proportion = Math.abs((startFlyHeight - flyHeight) / flyHeight);
        return (fallSpeed - (fallSpeed - startFallSpeed) / proportion);
    }
}
