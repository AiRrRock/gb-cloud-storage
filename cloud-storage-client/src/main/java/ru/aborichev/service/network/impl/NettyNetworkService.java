package ru.aborichev.service.network.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.service.network.NetworkService;

import java.net.InetSocketAddress;

public class NettyNetworkService implements NetworkService {

    private static NettyNetworkService instance;

    private NettyNetworkService() {
    }

    public static NettyNetworkService getInstance() {
        if (instance == null) {
            instance = new NettyNetworkService();
        }
        initialize();
        return instance;
    }

    private static void initialize(){
        // TODO do netty client
/*        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 9999));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast();
                }
            });
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }*/
    }


    @Override
    public void sendCommand(AbstractMessage command) {

    }

    @Override
    public Object readCommandResult() {
        return null;
    }

    @Override
    public void closeConnection() {

    }
//TODO add netty service implementation.
}
