package com.tuservidor.custommessages;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class KickListener {

    private final MessagesConfig messages;

    public KickListener(MessagesConfig messages) {
        this.messages = messages;
    }

    @Subscribe(order = PostOrder.LAST)
    public void onKicked(KickedFromServerEvent event) {
        Player player = event.getPlayer();
        String serverName = event.getServer().getServerInfo().getName();

        Component originalReason = event.getServerKickReason()
            .orElse(Component.text("Sin razon especificada"));

        boolean wasAlreadyConnected = player.getCurrentServer().isPresent()
            && player.getCurrentServer().get().getServerInfo().getName().equals(serverName);

        String key = wasAlreadyConnected ? "kicked-while-connected" : "kicked-while-connecting";

        Component cleanMessage = messages.get(key, originalReason, serverName);

        event.setResult(KickedFromServerEvent.DisconnectPlayer.create(cleanMessage));
    }
}
