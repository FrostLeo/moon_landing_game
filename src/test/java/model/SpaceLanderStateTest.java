package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SpaceLanderStateTest extends Assert {
    private SpaceLanderState state;

    @Before
    public void setUp() {
        state = new SpaceLanderState();
    }

    @Test
    public void constructorTest() {
        assertEquals(state.getFlyHeight(), 100, 0);
        assertEquals(state.getFuelReserve(), 140, 0);
        assertEquals(state.getCurrentFuelUsage(), 0, 0);
        assertEquals(state.getFallSpeed(), 0, 0);
        assertFalse(state.isDead());
    }

    @Test
    public void upgradeToStepTest() {
        SpaceLanderState newState = state.upgradeToStep(10);

        assertEquals(newState.getFlyHeight(), 100.1, 0);
        assertEquals(newState.getFuelReserve(), 130, 0);
        assertEquals(newState.getCurrentFuelUsage(), 10, 0);
        assertEquals(newState.getFallSpeed(), -0.2, 0);
        assertFalse(newState.isDead());
    }
}
