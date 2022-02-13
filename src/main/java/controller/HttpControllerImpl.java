package controller;

import com.github.vanbv.num.AbstractHttpMappingHandler;
import com.github.vanbv.num.annotation.*;
import com.github.vanbv.num.json.JsonParserDefault;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import server.model.GameDataRequestMessage;
import service.GameService;

import javax.inject.Inject;

@ChannelHandler.Sharable
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HttpControllerImpl extends AbstractHttpMappingHandler implements HttpController {
    @Inject
    GameService gameService;
    public HttpControllerImpl() {
        super(new JsonParserDefault());
    }

    /**
     * <b> Обработка GET запроса</b>
     * <p>В маппинге запроса ожидается существующий в БД id.
     * <br> Метод извлекает из БД запись игры по запрашиваемому id и возвращает response с данными в формате JSON.
     *
     * <p> Метод вернет status 400 BAD REQUEST в случае, если по данному id нет записи в БД.
     */
    @Get("/load/{id}")
    public DefaultFullHttpResponse loadInfo(@PathParam(value = "id") long id) {
        return gameService.loadGameStoryById(id);
    }

    /**
     * <b> Обработка POST запроса</b>
     * <p>В теле ожидается JSON (соответствующий классу GameDataRequestMessage) или null.
     *
     * <p>Если в теле приходит null:
     * <br> - в БД создается запись о новой игре со стартовыми параметрами и новым id;
     * <br> - в response возвращаются данные с id и стартовыми параметрами.
     *
     * <p> Если в теле приходит JSON в ожидаемом формате:
     * <br> - из БД достается запись с игрой;
     * <br> - вычисляются новые значения на требуемом шаге игры;
     * <br> - новые параметры перезаписываются в БД (при этом последующие шаги удаляются);
     * <br> - новые параметры возвращаются в response.
     *
     * <p> Метод вернет status 400 BAD REQUEST в случаях:
     * <br> - передаваемый в теле JSON не соответствует классу GameDataRequestMessage;
     * <br> - по запрашиваемому id отсутствует запись в БД;
     * <br> - передаваемое значение step превышает количество записей по запрашиваемому id<br> более чем на 1
     * (т.е. для данной игры этот номер шага не является следующим и нет существующей записи с таким номером);
     * <br> - если в теле передается что-либо, кроме JSON или null.
     */
    @Post("/game")
    public DefaultFullHttpResponse gameStep(@RequestBody GameDataRequestMessage dataMessage) {
        return gameService.executeGameStep(dataMessage);
    }

// в этот метод можно дописать свою логику для обработки исключений (взамен возвращения status 500)
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                HttpResponseStatus.INTERNAL_SERVER_ERROR));
//    }
}
