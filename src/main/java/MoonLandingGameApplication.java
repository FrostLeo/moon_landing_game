import com.google.inject.Guice;
import com.google.inject.Injector;
import config.GameModule;
import server.NettyGameServer;
import server.GameServer;

public class MoonLandingGameApplication {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new GameModule());
        GameServer gameServer = injector.getInstance(NettyGameServer.class);
        gameServer.begin();
    }
}
