package util;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.Assert;
import org.junit.Test;

public class HttpResponseStaticFactoryTest extends Assert {
    private DefaultFullHttpResponse response;

    @Test
    public void badRequestStatusTest() {
        response = HttpResponseStaticFactory.getResponse(null);
        assertSame(response.status(), HttpResponseStatus.BAD_REQUEST);
    }

    @Test
    public void oKStatusTest() {
        response = HttpResponseStaticFactory.getResponse("anything");
        assertSame(response.status(), HttpResponseStatus.OK);
    }
}
