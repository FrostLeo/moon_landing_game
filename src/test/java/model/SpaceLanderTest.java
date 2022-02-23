package model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SpaceLanderTest extends Assert {
    private SpaceLander spaceLander;

    @Before
    public void setUp() {
        spaceLander = new SpaceLander(100.00123, 140.00132, 9.80123, 0.00115, false);
    }

    @Test
    public void motionStepTest() {
        spaceLander.motion();

        assertEquals(spaceLander.getFallSpeed(), 0, 0.01);
        assertEquals(spaceLander.getCurrentFuelUsage(), 9.8, 0.01);
        assertEquals(spaceLander.getFlyHeight(), 100, 0.01);
        assertEquals(spaceLander.getFuelReserve(), 130.2, 0.01);
        assertFalse(spaceLander.isDead());
    }

    @Test
    public void motionFuelIsRunningOutAndFallSpeedTest() {
        // тест в условиях, когда fuelReserve < currentFuelUsage
        spaceLander.setFuelReserve(4.8);
        spaceLander.motion();

        assertEquals(spaceLander.getCurrentFuelUsage(), 4.8, 0.01);
        assertEquals(spaceLander.getFuelReserve(), 0, 0.01);
        assertEquals(spaceLander.getFlyHeight(), 97.5, 0.01);

        // тест в условиях, когда fuelReserve = 0, при currentFuelUsage != 0
        spaceLander.setCurrentFuelUsage(10);
        spaceLander.motion();

        assertEquals(spaceLander.getCurrentFuelUsage(), 0, 0.01);
        assertEquals(spaceLander.getFuelReserve(), 0, 0.01);
        assertEquals(spaceLander.getFlyHeight(), 87.6, 0.01);
    }

    @Test
    public void motionSurvivalTest() {
        spaceLander.setFlyHeight(10);
        spaceLander.setFallSpeed(15.05);
        spaceLander.setCurrentFuelUsage(19.9);
        spaceLander.motion();

        assertEquals(spaceLander.getFallSpeed(), 4.95, 0.01);
        assertEquals(spaceLander.getFlyHeight(), 0, 0.01);
        assertFalse(spaceLander.isDead());
    }

    @Test
    public void motionCrashTest() {
        spaceLander.setFlyHeight(2.6);
        spaceLander.setFallSpeed(0);
        spaceLander.setCurrentFuelUsage(0);
        spaceLander.motion();

        assertTrue(spaceLander.isDead());
    }

    @Test
    public void motionLandingSpeedCalculateSurvivalTest() {
        spaceLander.setFlyHeight(10);
        spaceLander.setFallSpeed(15);
        spaceLander.setCurrentFuelUsage(19.8);
        spaceLander.motion();

        assertFalse(spaceLander.isDead());
    }

    @Test
    public void motionLandingSpeedCalculateCrashTest() {
        spaceLander.setFlyHeight(4.8);
        spaceLander.setFallSpeed(5);
        spaceLander.setCurrentFuelUsage(9.7);
        spaceLander.motion();

        assertTrue(spaceLander.isDead());
    }

    @Test
    public void transformToStateTest() {
        SpaceLanderState state = spaceLander.toState();

        assertEquals(state.getFlyHeight(), 100.00, 0);
        assertEquals(state.getFuelReserve(), 140.00, 0);
        assertEquals(state.getCurrentFuelUsage(), 9.80, 0);
        assertEquals(state.getFallSpeed(), 0.00, 0);
        assertFalse(state.isDead());
    }
}
