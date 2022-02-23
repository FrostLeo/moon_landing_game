package util.converter;

import model.SpaceLanderState;
import model.data.GameStoryDto;

import java.util.List;

public interface Converter {
    List<SpaceLanderState> recoveryListFromJson (String gameStoryData);
    String convert(List<SpaceLanderState> gameDataList);
    String convert(GameStoryDto gameStoryDto);
    GameStoryDto recoveryDtoFromJson(String json);
}
