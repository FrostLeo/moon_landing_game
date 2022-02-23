package model.data;

import model.SpaceLanderState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GameDataListTest extends Assert {
    private GameDataList gameDataList;

    @Before
    public void setUp() {
        gameDataList = new GameDataList(1, new ArrayList<>());
        gameDataList.getGameStoryList().add(new SpaceLanderState());
    }

    @Test
    public void getPreviousSpaceLanderStateTest() {
        SpaceLanderState newState = new SpaceLanderState();
        gameDataList.getGameStoryList().add(newState);

        SpaceLanderState previousState = gameDataList.getPreviousSpaceLanderState(2);
        assertEquals(newState, previousState);
    }

    @Test
    public void isLegalStepTest() {
        assertTrue(gameDataList.isLegalStep(1));
        assertFalse(gameDataList.isLegalStep(-1));
        assertFalse(gameDataList.isLegalStep(0));
        assertFalse(gameDataList.isLegalStep(10));
    }

    @Test
    public void updateStoryListTest() {
        invalidIdNonUpdateTest();

        addStateSuccessfulUpdateTest(1);
        addStateSuccessfulUpdateTest(2);
        addStateSuccessfulUpdateTest(3);

        addStateSuccessfulUpdateTest(1);
        checkReductionListWhenUpdatingTest(2);
    }

    private void invalidIdNonUpdateTest() {
        SpaceLanderState newState = new SpaceLanderState();
        gameDataList.updateStoryList(newState, 0);
        SpaceLanderState updatedState = gameDataList.getGameStoryList().get(0);
        assertNotSame(newState, updatedState);
    }

    private void addStateSuccessfulUpdateTest(int step) {
        SpaceLanderState newState = new SpaceLanderState();
        gameDataList.updateStoryList(newState, step);
        SpaceLanderState updatedState = gameDataList.getGameStoryList().get(step);
        assertEquals(newState, updatedState);
    }

    private void checkReductionListWhenUpdatingTest(int expectedListSize) {
        assertEquals(expectedListSize, gameDataList.getGameStoryList().size());
    }
}
