package config;

import com.google.inject.AbstractModule;
import controller.HttpController;
import controller.HttpControllerImpl;
import model.story.GameStory;
import model.SpaceLander;
import model.Spacecraft;
import model.story.Story;
import model.story.dao.GameStoryDao;
import model.story.dao.GameStoryDaoImpl;
import server.NettyGameServer;
import server.GameServer;
import service.GameService;
import service.GameServiceImpl;

public class GameModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Spacecraft.class).to(SpaceLander.class);
        bind(Story.class).to(GameStory.class);
        bind(GameStoryDao.class).to(GameStoryDaoImpl.class);
        bind(GameServer.class).to(NettyGameServer.class);
        bind(HttpController.class).to(HttpControllerImpl.class);
        bind(GameService.class).to(GameServiceImpl.class);
    }
}
