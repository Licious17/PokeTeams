package io.github.tsecho.poketeams.eventlisteners;

import io.github.tsecho.poketeams.enums.ChatTypes;
import io.github.tsecho.poketeams.language.ChatUtils;
import io.github.tsecho.poketeams.pixelmon.QueueManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class ConnectionListener {

    @Listener
    public void onLeave(ClientConnectionEvent.Disconnect e, @Root Player player) {
        ChatUtils.setChat(player.getName(), ChatTypes.PUBLIC);
        if(QueueManager.queue.contains(player.getName())) QueueManager.queue.remove(player.getName());
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join e, @Root Player player) {
        ChatUtils.setChat(player.getName(), ChatTypes.PUBLIC);
    }
}
