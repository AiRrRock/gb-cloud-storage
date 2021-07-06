package ru.aborichev.cloudstorage.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.aborichev.cloudstorage.core.messages.user.AuthenticationUserMessage;
import ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment;
import ru.aborichev.cloudstorage.core.session.AuthenticationStatus;
import ru.aborichev.cloudstorage.core.session.UserSession;
import ru.aborichev.cloudstorage.core.session.impl.BasicUserSession;
import ru.aborichev.cloudstorage.server.factory.Factory;

import java.util.UUID;

public class AuthenticationHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationStatus.class);
    private static final ConfigurationEnvironment configurations = Factory.getConfigurationEnvironment();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("Received new message of " + msg.getClass() + " class");
        if (msg instanceof AuthenticationUserMessage) {
            LOGGER.debug("Authentication started");
            AuthenticationUserMessage user = (AuthenticationUserMessage) msg;
            Factory.getAuthenticationService();
            UserSession session = new BasicUserSession(user.getUserName(), UUID.randomUUID().toString(), AuthenticationStatus.SUCCESS);
            user.setSession(session);
            //TODO Save user session somewhere. Preferably in DB.
            ctx.writeAndFlush(user);
        }else {
            ctx.fireChannelRead(msg);
        }
    }
}
