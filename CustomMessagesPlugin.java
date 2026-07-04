package com.tuservidor.custommessages;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
    id = "custommessages",
    name = "VelocityCustomMessages",
    version = "1.0.0",
    description = "Permite personalizar todos los mensajes internos de desconexion/kick de Velocity",
    authors = {"TuServidor"}
)
public class CustomMessagesPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private MessagesConfig messagesConfig;

    @Inject
    public CustomMessagesPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.messagesConfig = new MessagesConfig(dataDirectory, logger);
        this.messagesConfig.loadOrCreate();

        server.getEventManager().register(this, new KickListener(messagesConfig));
        server.getCommandManager().register(
            server.getCommandManager().metaBuilder("customessages")
                .aliases("cmsg")
                .plugin(this)
                .build(),
            new ReloadCommand(messagesConfig)
        );

        logger.info("VelocityCustomMessages habilitado. Edita messages.yml en plugins/customessages/ y usa /customessages reload.");
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }
}
