package service;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import server.model.GameDataRequestMessage;

public interface GameService {
    DefaultFullHttpResponse loadGameStoryById(long id);
    DefaultFullHttpResponse executeGameStep(GameDataRequestMessage dataMessage);
}
