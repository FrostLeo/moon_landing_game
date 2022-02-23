package service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import model.SpaceLanderState;
import model.data.GameStoryDto;
import model.entity.GameStory;
import model.data.GameDataList;
import model.data.dao.GameStoryDao;
import service.model.GameDataRequestMessage;
import service.validator.GameDataRequestMessageValidator;

import javax.inject.Inject;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GameServiceImpl implements GameService {
    GameStoryDao gameStoryDao;
    GameDataRequestMessageValidator validator;

    @Inject
    public GameServiceImpl(GameStoryDao gameStoryDao, GameDataRequestMessageValidator validator) {
        this.gameStoryDao = gameStoryDao;
        this.validator = validator;
    }

    @Override
    public String loadGameStoryById(long id) {
        Optional<GameStory> currentGameStory = getGameStory(id);
        return currentGameStory.map(gameStory -> getDataView(GameStoryDto.getInstance(gameStory)))
                                                .orElse(null);
    }

    @Override
    public String executeGameStep(GameDataRequestMessage dataMessage) {
        if (dataMessage == null) {
            return processNullRequest();

        } else if (validator.isValidRequestMessage(dataMessage)) {
            Optional<GameStory> optionGameStory = getGameStory(dataMessage.getId());
            int currentStep = dataMessage.getStep();

            if (optionGameStory.isPresent()) {
                GameStory gameStory = optionGameStory.get();
                GameDataList currentGameDataList = GameStoryDto.getInstance(gameStory).toGameDataList();
//                GameDataList currentGameDataList = optionGameStory.get().toGameStoryDto().toGameDataList();

                if (currentGameDataList.isLegalStep(currentStep)) {
                    return processMessageRequest(gameStory, currentStep, dataMessage.getFuelUsage());
                }
            }
        }
        return null;
    }

    private String processNullRequest() {
        GameStory gameStory = new GameStory();
        gameStoryDao.save(gameStory);
        return getDataView(GameStoryDto.getInstance(gameStory));
    }

    private String processMessageRequest(GameStory gameStory, int step, double currentFuelUsage) {
        GameDataList gameDataList = GameStoryDto.getInstance(gameStory).toGameDataList();
        SpaceLanderState currentState = gameDataList.getPreviousSpaceLanderState(step);

        if (currentState.isDead()) {
            return null;
        }

        SpaceLanderState newState = currentState.upgradeToStep(currentFuelUsage);
        gameDataList.updateStoryList(newState, step);

        GameStoryDto updatedGameStoryDto = GameStoryDto.getInstance(gameDataList);
        String updatedGameData = updatedGameStoryDto.getGameStoryListToJson();

        gameStory.setGameStoryData(updatedGameData);
        gameStoryDao.update(gameStory);
        return getDataView(updatedGameStoryDto);
    }

    private Optional<GameStory> getGameStory(long id) {
        return gameStoryDao.getById(id);
    }

    private String getDataView(GameStoryDto gameStoryDto) {
        return gameStoryDto.toView().getDataView();
    }
}
