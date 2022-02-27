package service.validator;

import model.request.GameDataRequestMessage;

import javax.inject.Singleton;

@Singleton
public class GameDataRequestMessageValidator {
    public boolean isValidRequestMessage(GameDataRequestMessage message) {
        return isValidId(message.getId())
                && isValidStep(message.getStep())
                && iValidFuelUsage(message.getFuelUsage());
    }

    private boolean isValidId(long id) {
        return id > 0;
    }

    private boolean isValidStep(int step) {
        return step > 0;
    }

    private boolean iValidFuelUsage(double fuelUsage) {
        return fuelUsage >= 0;
    }
}
