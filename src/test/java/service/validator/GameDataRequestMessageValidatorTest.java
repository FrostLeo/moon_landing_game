package service.validator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import model.request.GameDataRequestMessage;

@RunWith(Theories.class)
public class GameDataRequestMessageValidatorTest extends Assert {
    private static GameDataRequestMessageValidator validator;
    private static GameDataRequestMessage message;

    @BeforeClass
    public static void beforeClass() {
        validator = new GameDataRequestMessageValidator();
        message = new GameDataRequestMessage();
    }

    @DataPoints
    public static Object[][] notValidTestData = new Object[][] {
            { -1L, 1, 1.0 },
            { 1L, -1, 1.0 },
            { 1L, 1, -1.0 },
            { 1L, 0, 1.0 },
            { 0L, 1, 1.0 },
    };

    @Test
    public void validTest() {
        message.setId(1);
        message.setStep(1);
        message.setFuelUsage(0);

        assertTrue(validator.isValidRequestMessage(message));
    }

    @Theory
    public void notValidTest(final Object... testData) {
        message.setId((long) testData[0]);
        message.setStep((int) testData[1]);
        message.setFuelUsage((double) testData[2]);

        assertFalse(validator.isValidRequestMessage(message));
    }
}
