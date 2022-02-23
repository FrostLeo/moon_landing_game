package model.data;

import model.SpaceLanderState;

public interface DataList {
    boolean isLegalStep(int step);
    void updateStoryList(SpaceLanderState newState, int step);
}
