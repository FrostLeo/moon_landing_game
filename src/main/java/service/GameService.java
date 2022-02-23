package service;

import service.model.GameDataRequestMessage;

public interface GameService {
    String loadGameStoryById(long id);
    String executeGameStep(GameDataRequestMessage dataMessage);
}
