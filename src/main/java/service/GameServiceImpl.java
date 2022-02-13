package service;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import lombok.NoArgsConstructor;
import model.SpaceLanderState;
import model.dto.GameStoryDto;
import model.story.GameStory;
import model.story.dao.GameStoryDao;
import server.model.GameDataRequestMessage;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

@NoArgsConstructor
public class GameServiceImpl implements GameService {
    @Inject
    private GameStoryDao gameStoryDao;

    @Override
    public DefaultFullHttpResponse loadGameStoryById(long id) {
        Optional<GameStoryDto> currentDto = gameStoryDao.getById(id);
        if (currentDto.isEmpty()) {
            return getBadRequestStatus();
        }
        String response = getJson(currentDto.get());
        return getHttpResponse(response);
    }

    @Override
    public DefaultFullHttpResponse executeGameStep(GameDataRequestMessage dataMessage) {
        String response = null;
        if (dataMessage == null) {
            response = processNullRequest();

        } else {
            Optional<GameStoryDto> currentDto = gameStoryDao.getById(dataMessage.getId());
            int currentStep = dataMessage.getStep();
            if (currentDto.isPresent()

                    && currentDto
                    .get()
                    .toGameStory()
                    .isLegalStep(currentStep)

                    && dataMessage
                    .getStep() > 0) {

                response = processMessageRequest(currentDto.get(), currentStep,
                        dataMessage.getFuelUsage());
            }
        }
        return (response == null)
                ? getBadRequestStatus()
                : getHttpResponse(response);
    }

    // обрабатывает request, содержащий null и возвращает response
    private String processNullRequest() {
        GameStoryDto currentDto = new GameStoryDto();
        gameStoryDao.save(currentDto);
        return getJson(currentDto);
    }

    // обрабатывает request, содержащий JSON в ожидаемом формате и возвращает response
    private String processMessageRequest(GameStoryDto currentDto, int step,
                                         double currentFuelUsage) {
        GameStory currentGameStory = currentDto.toGameStory();
        SpaceLanderState currentState = currentGameStory
                                        .getPreviousSpaceLanderState(step);

        if (currentState.isDead()) {
            return null;
        }

        SpaceLanderState newState = currentState.upgradeToStep(currentFuelUsage);
        String newGameData = currentGameStory
                    .updateGameData(newState, step);

        currentDto.setGameData(newGameData);
        gameStoryDao.update(currentDto);
        return getJson(currentDto);
    }

    // формирует и возвращает JSON
    private String getJson(GameStoryDto currentDto) {
        return new Gson().toJson(currentDto);
    }

    // формирует и возвращает response (status 200 - OK)
    private DefaultFullHttpResponse getHttpResponse(String response) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK,
                Unpooled.copiedBuffer(response, StandardCharsets.UTF_8));
    }

    // формирует и возвращает response (status 400 - BAD REQUEST)
    private DefaultFullHttpResponse getBadRequestStatus() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, BAD_REQUEST);
    }
}
