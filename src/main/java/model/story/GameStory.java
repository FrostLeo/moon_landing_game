package model.story;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import model.SpaceLanderState;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameStory implements Story {
    List<SpaceLanderState> gameStoryList;

    // конструктор для новой игры
    public GameStory() {
        gameStoryList = new ArrayList<>();
        gameStoryList.add(new SpaceLanderState());
    }

    // конструктор для десериализации из DTO
    public GameStory(String gameData) {
        Type listType = new TypeToken<ArrayList<SpaceLanderState>>(){}.getType();
        gameStoryList = new Gson().fromJson(gameData, listType);
    }

    public SpaceLanderState getPreviousSpaceLanderState(int step) {
        return gameStoryList.get(--step);
    }

    // для переписывания существующей записи
    public String updateGameData(SpaceLanderState newState, int step) {
        if (isContainNum(step)) {
            gameStoryList = gameStoryList
                    .stream()
                    .limit(step)
                    .collect(Collectors.toList());
        }
        gameStoryList.add(newState);
        return toJson();
    }

    private boolean isContainNum(int step) {
        return step < gameStoryList.size();
    }

    @Override
    public boolean isLegalStep(int step) {
        return gameStoryList.size() >= step;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(gameStoryList);
    }
}

