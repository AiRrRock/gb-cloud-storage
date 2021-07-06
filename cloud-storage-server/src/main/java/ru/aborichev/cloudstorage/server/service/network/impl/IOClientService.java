package ru.aborichev.cloudstorage.server.service.network.impl;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.aborichev.cloudstorage.server.factory.Factory;
import ru.aborichev.cloudstorage.server.service.network.ClientService;
import ru.aborichev.cloudstorage.server.service.commandservice.CommandDictionaryService;

import java.io.*;
import java.net.Socket;
@Deprecated
public class IOClientService implements ClientService {

    private Socket clientSocket;

    private ObjectDecoderInputStream in;
    private ObjectEncoderOutputStream out;

    private CommandDictionaryService dictionaryService;

    public IOClientService(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.dictionaryService = Factory.getCommandDirectoryService();

        initializeIOStreams();
    }

    private void initializeIOStreams() {
        try {
            this.in = new ObjectDecoderInputStream(clientSocket.getInputStream());
            this.out = new ObjectEncoderOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startIOProcess() {
        new Thread(() -> {
            try {
                while (true) {
                   /* String clientCommand = readCommand();
                    String commandResult = dictionaryService.processCommand(clientCommand);

                    writeCommandResult(commandResult);*/
                }
            } catch (Exception ex) {
                System.err.println("Client error: " + ex.getMessage());
            } finally {
                closeConnection();
            }

        }).start();
    }

    public void writeCommandResult(Object commandResult) {
        try {
            out.writeObject(commandResult);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readCommand() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Read command result exception: " + e.getMessage());
        }
    }

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
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
