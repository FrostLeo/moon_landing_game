package model.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import model.SpaceLanderState;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameDataList implements DataList {
    long id;
    List<SpaceLanderState> gameStoryList;

    public SpaceLanderState getPreviousSpaceLanderState(int step) {
        return gameStoryList.get(--step);
    }

    @Override
    public boolean isLegalStep(int step) {
        return step > 0 && gameStoryList.size() >= step;
    }

    @Override
    public void updateStoryList(SpaceLanderState newState, int step) {
        if (isContainNum(step)) {
            gameStoryList = gameStoryList
                    .stream()
                    .limit(step)
                    .collect(Collectors.toList());
        } else if (isNotNextNum(step)) {
            return;
        }
        gameStoryList.add(newState);
    }

    private boolean isNotNextNum(int step) {
        return step != getGameStoryList().size();
    }

    private boolean isContainNum(int step) {
        return step > 0 && step < gameStoryList.size();
    }
}

