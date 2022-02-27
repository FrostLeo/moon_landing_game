package controller;

import com.github.vanbv.num.AbstractHttpMappingHandler;
import com.github.vanbv.num.annotation.*;
import com.github.vanbv.num.json.JsonParser;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import model.request.GameDataRequestMessage;
import service.GameService;
import util.HttpResponseStaticFactory;

import javax.inject.Inject;

@ChannelHandler.Sharable
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HttpController extends AbstractHttpMappingHandler{
    GameService gameService;

    @Inject
    public HttpController(JsonParser parser, GameService gameService) {
        super(parser);
        this.gameService = gameService;
    }

    /**
     * <p>В маппинге <b>GET</b> запроса ожидается существующий в БД id.
     * <br> Метод извлекает из БД запись игры по запрашиваемому <b>id</b> и возвращает response с данными в формате JSON.
     *
     * <p> Метод вернет status <b>400 BAD REQUEST</b> в случае, если по данному id нет записи в БД.
     */
    @Get("/load/{id}")
    public DefaultFullHttpResponse loadInfo(@PathParam(value = "id") long id) {
//        return gameService.loadGameStoryById(id);
        String result = gameService.loadGameStoryById(id);
        return HttpResponseStaticFactory.getResponse(result);
    }

    /**
     * Обработка <b>POST</b> запроса:
     * <br> - в БД создается запись о новой игре со стартовыми параметрами и новым <b>id</b>;
     * <br> - в response возвращаются данные с <b>id</b> и стартовыми параметрами.
     */
    @Post("/new_game")
    public DefaultFullHttpResponse createNewGame() {
        String result = gameService.createNewGame();
        return HttpResponseStaticFactory.getResponse(result);
    }

    /**
     * <p>В теле <b>POST</b> запроса ожидается JSON (соответствующий классу <b>GameDataRequestMessage</b>).
     *
     * <p> Обработка запроса:
     * <br> - из БД достается запись с игрой;
     * <br> - вычисляются новые значения на требуемом шаге игры;
     * <br> - новые параметры перезаписываются в БД (при этом последующие шаги удаляются);
     * <br> - новые параметры возвращаются в response.
     *
     * <p> Метод вернет status <b>400 BAD REQUEST</b> в случаях:
     * <br> - передаваемый в теле JSON не соответствует классу <b>GameDataRequestMessage</b>;
     * <br> - по запрашиваемому <b>id</b> отсутствует запись в БД;
     * <br> - значение поля <b>step</b> превышает количество записей по запрашиваемому <b>id</b> более чем на 1
     * (т.е. для данной игры этот номер шага не является следующим и нет существующей записи с таким номером);
     * <br> - если в теле запроса передается что-либо, кроме JSON.
     */
    @Post("/game_step")
    public DefaultFullHttpResponse gameStep(@RequestBody GameDataRequestMessage dataMessage) {
        String result = gameService.executeGameStep(dataMessage);
        return HttpResponseStaticFactory.getResponse(result);
    }

// в этот метод можно дописать свою логику для обработки исключений (взамен возвращения status 500)
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                HttpResponseStatus.INTERNAL_SERVER_ERROR));
//    }
}
