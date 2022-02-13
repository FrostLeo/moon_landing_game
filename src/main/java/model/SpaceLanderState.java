package model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceLanderState {
    double flyHeight;
    double fuelReserve;
    double currentFuelUsage;
    double fallSpeed;
    boolean isDead;

    public SpaceLanderState() {
        flyHeight = 100.0;
        fuelReserve = 140.0;
        currentFuelUsage = 0.0;
        fallSpeed = 0.0;
        isDead = false;
    }

    // выполняет шаг игры и возвращает новый SpaceLanderState с обновленными параметрами
    public SpaceLanderState upgradeToStep(double newFuelUsage) {
        SpaceLander currentSpaceLander = this.toSpaceLander();
        currentSpaceLander.setCurrentFuelUsage(newFuelUsage);
        currentSpaceLander.motion();
        return currentSpaceLander.toState();
    }

    private SpaceLander toSpaceLander() {
        return new SpaceLander(getFlyHeight(),
                getFuelReserve(),
                getCurrentFuelUsage(),
                getFallSpeed(),
                isDead());
    }
}
