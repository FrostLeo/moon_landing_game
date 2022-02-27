package service;

import model.request.GameDataRequestMessage;

public interface GameService {
    String createNewGame();
    String loadGameStoryById(long id);
    String executeGameStep(GameDataRequestMessage dataMessage);
}
