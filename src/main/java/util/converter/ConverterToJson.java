package util.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.SpaceLanderState;
import model.data.GameStoryDto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConverterToJson implements Converter {
    private final Gson gsonFormat;

    public ConverterToJson() {
        gsonFormat = new Gson();
    }

    @Override
    public String convert(List<SpaceLanderState> gameDataList) {
        return gsonFormat.toJson(gameDataList);
    }

    @Override
    public String convert(GameStoryDto gameStoryDto) {
        return gsonFormat.toJson(gameStoryDto);
    }

    @Override
    public GameStoryDto recoveryDtoFromJson(String json) {
        return gsonFormat.fromJson(json, GameStoryDto.class);
    }

    @Override
    public List<SpaceLanderState> recoveryListFromJson(String gameStoryData) {
        Type listType = new TypeToken<ArrayList<SpaceLanderState>>(){}.getType();
        return gsonFormat.<ArrayList<SpaceLanderState>>fromJson(gameStoryData, listType);
    }
}