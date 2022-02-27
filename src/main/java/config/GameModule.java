package config;

import com.github.vanbv.num.json.JsonParserDefault;
import com.google.gson.Gson;
import com.github.vanbv.num.json.JsonParser;
import com.google.inject.AbstractModule;
import controller.HttpController;
import service.repository.GameStoryDao;
import service.repository.GameStoryDaoImpl;
import server.NettyGameServer;
import server.GameServer;
import service.GameService;
import service.GameServiceImpl;
import service.validator.GameDataRequestMessageValidator;

public class GameModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameStoryDao.class).to(GameStoryDaoImpl.class);
        bind(GameServer.class).to(NettyGameServer.class);
        bind(GameService.class).to(GameServiceImpl.class);
        bind(JsonParser.class).to(JsonParserDefault.class);
        bind(HttpController.class);
        bind(Gson.class);
        bind(GameDataRequestMessageValidator.class);
    }
}
