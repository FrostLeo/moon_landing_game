package controller;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import server.model.GameDataRequestMessage;

public interface HttpController {
    DefaultFullHttpResponse loadInfo(long id);
    DefaultFullHttpResponse gameStep(GameDataRequestMessage message);
}
