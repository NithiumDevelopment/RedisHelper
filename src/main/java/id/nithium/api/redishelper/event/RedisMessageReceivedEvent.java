package id.nithium.api.redishelper.event;

import cz.foresttech.forestredis.shared.events.IRedisMessageReceivedEvent;
import cz.foresttech.forestredis.shared.models.MessageTransferObject;
import id.nithium.api.event.Event;
import id.nithium.api.redishelper.RedisHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RedisMessageReceivedEvent extends Event implements IRedisMessageReceivedEvent {

    private final RedisHelper redisHelper = RedisHelper.getInstance();
    @NonNull
    private final String channel;
    @NonNull
    @Getter
    private final MessageTransferObject messageTransferObject;

    @Override
    public String getSenderIdentifier() {
        return messageTransferObject.getSenderIdentifier();
    }

    @Override
    public String getChannel() {
        return channel;
    }

    @Override
    public String getMessage() {
        return messageTransferObject.getMessage();
    }

    @Override
    public long getTimeStamp() {
        return messageTransferObject.getTimestamp();
    }

    @Override
    public <T> T getMessageObject(Class<T> aClass) {
        return messageTransferObject.parseMessageObject(aClass);
    }

    @Override
    public boolean isSelfSender() {
        return getSenderIdentifier().equals(redisHelper.getIdentifier());
    }
}
