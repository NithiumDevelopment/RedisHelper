package id.nithium.api.redishelper;

import cz.foresttech.forestredis.shared.IForestRedisPlugin;
import cz.foresttech.forestredis.shared.RedisManager;
import cz.foresttech.forestredis.shared.models.MessageTransferObject;
import cz.foresttech.forestredis.shared.models.RedisConfiguration;
import id.nithium.api.event.EventManager;
import id.nithium.api.redishelper.event.RedisMessageReceivedEvent;
import id.nithium.api.redishelper.exception.RedisException;
import id.nithium.libraries.proxima.Proxima;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Getter
@Setter
public class RedisHelper implements IForestRedisPlugin {

    @Getter
    private static RedisHelper instance;
    private final Logger logger = Proxima.getLogger();
    private EventManager eventManager;
    private final String identifier;
    private final RedisManager redisManager;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public RedisHelper(@NonNull String identifier, @NonNull RedisConfiguration redisConfiguration) {
        instance = this;

        if (getRedisManager() != null) throw new RedisException("Could not initialize object that already initialized. (RedisManager)");

        eventManager = new EventManager();

        this.identifier = identifier;
        redisManager = new RedisManager(this, identifier, redisConfiguration);
    }

    @Override
    public void runAsync(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public void onMessageReceived(String s, MessageTransferObject messageTransferObject) {
        eventManager.callEvent(new RedisMessageReceivedEvent(s, messageTransferObject));
    }

    @Override
    public Logger logger() {
        return logger;
    }
}
