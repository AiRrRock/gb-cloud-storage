package ru.aborichev.cloudstorage.server.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.server.handlers.AuthenticationHandler;
import ru.aborichev.cloudstorage.server.service.ServerService;
import ru.aborichev.cloudstorage.server.handlers.FileCommandHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import static ru.aborichev.cloudstorage.core.constants.MainConstants.MAX_OBJECT_SIZE;
import static ru.aborichev.cloudstorage.core.constants.MainConstants.SERVER_PORT;

public class NettyServerService implements ServerService {
    private static final Logger LOGGER = LogManager.getLogger(NettyServerService.class);

    private static NettyServerService instance;

    private NettyServerService() {
    }

    public static ServerService getInstance() {
        if (instance == null) {
            LOGGER.debug("Creating new instance of NettyServer");
            instance = new NettyServerService();
        }
        return instance;
    }


    @Override
    public void startServer() {
        //  Configuration for local variables.
        final int soBackLog = 128;

        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(
                                final SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(MAX_OBJECT_SIZE,
                                            ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new AuthenticationHandler(),
                                    new FileCommandHandler()
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, soBackLog)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(SERVER_PORT).sync();
            LOGGER.info("Server started at port " + SERVER_PORT);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e);
        } finally {
            LOGGER.info("Shutting down server");
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            LOGGER.info("Server shut down");
        }
    }


}
