package server;

import config.HibernateConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import controller.HttpController;

import javax.inject.Inject;

public class NettyGameServer implements GameServer {
    private HttpController controller;

    @Inject
    public NettyGameServer(HttpController controller) {
        this.controller = controller;
    }

    public void begin() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast("HttpServerCodec", new HttpServerCodec())
                                    .addLast("HttpServerKeepAlive", new HttpServerKeepAliveHandler())
                                    .addLast("HttpObjectAggregator", new HttpObjectAggregator(10 * 1024 * 102, true))
                                    .addLast("HttpChunkedWrite", new ChunkedWriteHandler())
                                    .addLast("HttpGameController", controller);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(8090).sync();
            HibernateConfig.initSessionFactory();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            HibernateConfig.close();
        }
    }
}
