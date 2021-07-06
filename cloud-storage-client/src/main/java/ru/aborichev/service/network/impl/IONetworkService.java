package ru.aborichev.service.network.impl;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.aborichev.cloudstorage.core.messages.AbstractMessage;
import ru.aborichev.service.network.NetworkService;

import java.io.*;
import java.net.Socket;

import static ru.aborichev.cloudstorage.core.constants.MainConstants.*;

public class IONetworkService implements NetworkService {

    private static IONetworkService instance;

    public static Socket socket;
    public static ObjectDecoderInputStream in;
    public static ObjectEncoderOutputStream out;

    private IONetworkService() {
    }

    public static IONetworkService getInstance() {
        if (instance == null) {
            instance = new IONetworkService();

            initializeSocket();
            initializeIOStreams();
        }

        return instance;
    }

    private static void initializeSocket() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeIOStreams() {
        try {
            in = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJECT_SIZE);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendCommand(AbstractMessage command) {
        try {
            out.writeObject(command);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object readCommandResult() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Read command result exception: " + e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}