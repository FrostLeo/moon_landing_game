package util;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

public class HttpResponseStaticFactory {
    public static DefaultFullHttpResponse getResponse(String result) {
        return (result == null)
                ? getBadRequest()
                : getOk(result);
    }

    private static DefaultFullHttpResponse getOk(String result) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(result, StandardCharsets.UTF_8));
    }

    private static DefaultFullHttpResponse getBadRequest() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
    }
}
