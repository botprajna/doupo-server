package com.doupo.server.foundation.player;

import com.doupo.protocol.MessageWrapper;
import com.doupo.server.module.scene.SceneHandler;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

import org.gaming.fakecmd.side.game.IPlayerContext;
import org.gaming.fakecmd.side.game.PlayerCmdRegister.IPlayerCmdMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerProtocolMessage implements IPlayerCmdMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerProtocolMessage.class);

    private final PlayerConnectionContext context;
    private final MessageWrapper wrapper;

    public PlayerProtocolMessage(
            PlayerConnectionContext context,
            MessageWrapper wrapper) {
        this.context = context;
        this.wrapper = wrapper;
    }

    @Override
    public IPlayerContext getPlayerContext() {
        return context;
    }

    @Override
    public int getMessageId() {
        return (int) wrapper.getRequestId();
    }

    @Override
    public int getCmd() {
        return wrapper.getProtoId();
    }

    @Override
    public ByteString getData() {
        return wrapper.getData();
    }

    @Override
    public void onResponse(Object response) {
        if (wrapper.getProtoId() == 50051) {
            context.write(
                    50052,
                    (GeneratedMessageV3) response,
                    getMessageId());

            context.write(
                    50354,
                    ByteString.EMPTY,
                    0);
            return;
        }

        int responseCmd;

        switch (wrapper.getProtoId()) {
            case 50002:
                responseCmd = 50003;
                break;

            case 50371:
                responseCmd = 50365;
                break;

            case 50502:
                responseCmd = 50503;
                break;

            case 50780:
                responseCmd = 50753;
                break;

            case 50755:
                responseCmd = 50756;
                break;
            
            case 51605:
                responseCmd = 51606;
                break;

            case 51607:
                responseCmd = 51608;
                break;

            case 51609:
                responseCmd = 51610;
                break;

            case 71601:
                responseCmd = 71602;
                break;

            case 61506:
                responseCmd = 61507;
                break;

            default:
                responseCmd = wrapper.getProtoId();
                break;
        }

        context.write(
                responseCmd,
                (GeneratedMessageV3) response,
                getMessageId());

        if (wrapper.getProtoId() == 50502) {
            context.write(
                    50790,
                    SceneHandler.nonSceneHeroSnapshot(context.getId()),
                    0);

            context.write(
                    50753,
                    SceneHandler.initialScene(),
                    0);
        }
    }

    @Override
    public void onException(Exception exception, Message request) {
        LOGGER.error("Protocol processing failed: protoId={}",
                wrapper.getProtoId(), exception);

        MessageWrapper response = MessageWrapper.newBuilder()
                .setProtoId(wrapper.getProtoId())
                .setRequestId(wrapper.getRequestId())
                .setCode(500)
                .setMessage("Internal server error")
                .build();

        context.write(response);
    }
}
