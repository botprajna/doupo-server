package com.doupo.server.foundation.player;

import com.doupo.pojo.account.Account;
import com.doupo.protocol.MessageWrapper;
import com.doupo.server.foundation.netty.GameFrameCodec;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;

import org.gaming.fakecmd.side.game.IPlayerContext;

import akka.actor.ActorRef;
import io.netty.channel.Channel;

public class PlayerConnectionContext implements IPlayerContext {

    private final Channel channel;
    private volatile ActorRef actor;
    private volatile long playerId;
    private volatile boolean login;
    private volatile int currentMessageId;
    private volatile Account account;
    private volatile boolean gameFrame;
    private int serverZone = 1;
    // Existing handlers send absolute balances. Keep the last sent values so the
    // chapter 15+/tower rewards add to that balance instead of overwriting it.
    private final java.util.Map<Integer, Long> currencyBalances = new java.util.HashMap<>();
    private final java.util.Map<Integer, com.doupo.protocol.UpdateItem> itemSlots = new java.util.HashMap<>();

    public PlayerConnectionContext(Channel channel) {
        this.channel = channel;
        this.actor = PlayerActorPool.nextActor();
    }

    public void tell(MessageWrapper wrapper) {
        actor.tell(new PlayerProtocolMessage(this, wrapper), ActorRef.noSender());
    }

    public void write(MessageWrapper wrapper) {
        if (channel.isActive()) {
            byte[] payload = wrapper.toByteArray();

            if (gameFrame) {
                payload = GameFrameCodec.encode(
                        wrapper.getProtoId(),
                        wrapper.getData().toByteArray(),
                        false,
                        (int) wrapper.getRequestId());
            }

            channel.writeAndFlush(payload);
        }
    }

    @Override
    public void write(int cmd, GeneratedMessageV3 message, int messageId) {
        write(cmd, message.toByteString(), messageId);
    }

    @Override
    public void write(int cmd, ByteString data, int messageId) {
        trackBalances(cmd, data);
        write(MessageWrapper.newBuilder()
                .setProtoId(cmd)
                .setRequestId(messageId)
                .setData(data)
                .setCode(0)
                .build());
    }

    public Long currencyBalance(int type) {
        return currencyBalances.get(type);
    }

    public com.doupo.protocol.UpdateItem itemStack(int key) {
        for (com.doupo.protocol.UpdateItem slot : itemSlots.values()) {
            if (slot.hasPackItem() && slot.getPackItem().getKey() == key) return slot;
        }
        return null;
    }

    private void trackBalances(int cmd, ByteString data) {
        try {
            if (cmd == 50651 || cmd == 50652) {
                // PurseInfoResp and PurseUpdateResp both carry CurrencyItemVo at field 1.
                if (cmd == 50652) currencyBalances.clear();
                for (com.doupo.protocol.CurrencyItemVo item : com.doupo.protocol.PurseUpdateResp.parseFrom(data).getItemsList()) {
                    currencyBalances.put(item.getType(), item.getValue());
                }
            }
            if (cmd == 50401 || cmd == 50402) {
                java.util.List<com.doupo.protocol.PackUpdateVo> packs;
                if (cmd == 50401) {
                    itemSlots.clear();
                    packs = com.doupo.protocol.PackInfoResp.parseFrom(data).getPacksList();
                } else {
                    packs = com.doupo.protocol.PackUpdateResp.parseFrom(data).getPacksList();
                }
                for (com.doupo.protocol.PackUpdateVo pack : packs) {
                    if (pack.getPackType() != 1) continue;
                    for (com.doupo.protocol.UpdateItem item : pack.getUpdateItemsList()) {
                        if (item.hasPackItem()) itemSlots.put(item.getItemIndex(), item);
                        else itemSlots.remove(item.getItemIndex());
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new IllegalArgumentException("Invalid outgoing balance packet " + cmd, e);
        }
    }

    public void bindPlayer(long playerId) {
        this.playerId = playerId;
        this.actor = PlayerActorPool.actorFor(playerId);
        this.login = true;
    }

    public void bindAccount(Account account) {
        this.account = account;
        bindPlayer(100_000_000_000L + account.getId());
    }

    public void setGameFrame(boolean gameFrame) {
        this.gameFrame = gameFrame;
    }

    public Account getAccount() {
        return account;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public long getId() {
        return playerId;
    }

    @Override
    public int getServerZone() {
        return serverZone;
    }

    @Override
    public int getCurrMsgId() {
        return currentMessageId;
    }

    @Override
    public void setCurrMsgId(int messageId) {
        this.currentMessageId = messageId;
    }

    @Override
    public boolean isLogin() {
        return login;
    }
}
