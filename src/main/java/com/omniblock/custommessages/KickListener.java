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

        // ANTES: el fallback "Sin razon especificada" estaba escrito directamente aqui en Java.
        // AHORA: si el backend no envio razon, usamos el texto configurable de messages.yml
        // (clave "default-kick-reason"), en vez de un string fijo en el codigo.
        Component originalReason = event.getServerKickReason()
            .orElseGet(messages::getDefaultKickReason);

        boolean wasAlreadyConnected = player.getCurrentServer().isPresent()
            && player.getCurrentServer().get().getServerInfo().getName().equals(serverName);

        String key = wasAlreadyConnected ? "kicked-while-connected" : "kicked-while-connecting";

        Component cleanMessage = messages.get(key, originalReason, serverName);

        event.setResult(KickedFromServerEvent.DisconnectPlayer.create(cleanMessage));
    }
}
