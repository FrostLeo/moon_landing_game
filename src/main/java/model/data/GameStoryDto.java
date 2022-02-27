package model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import model.SpaceLanderState;
import model.entity.GameStory;
import util.converter.Converter;
import util.converter.ConverterToJson;
import view.GameStoryView;
import view.ViewData;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameStoryDto {
    static final Converter converter = new ConverterToJson();
    long id;
    final List<SpaceLanderState> gameStoryList;

    public GameStoryDto() {
        gameStoryList = new ArrayList<>();
        gameStoryList.add(new SpaceLanderState());
    }

    public static GameStoryDto getInstance(GameStory gameStory) {
        return new GameStoryDto(gameStory.getId(),
                converter.recoveryListFromJson(gameStory.getGameStoryData()));
    }

    public static GameStoryDto getInstance(GameDataList gameDataList) {
        return new GameStoryDto(gameDataList.getId(), gameDataList.getGameStoryList());
    }

//    public static GameStoryDto getInstance(String viewString) {
//        return converter.recoveryDtoFromJson(viewString);
//    }

    private GameStoryDto(long id, List<SpaceLanderState> gameStoryList) {
        this.id = id;
        this.gameStoryList = gameStoryList;
    }

    public GameStoryView toView() {
        int currentStep = gameStoryList.size()-1;
        ViewData viewData = new ViewData(id, currentStep, gameStoryList.get(currentStep));
        return new GameStoryView(converter.convert(viewData));
    }

    public GameDataList toGameDataList() {
        return new GameDataList(id, gameStoryList);
    }

    public String getGameStoryListToJson() {
        return converter.convert(gameStoryList);
    }
}
