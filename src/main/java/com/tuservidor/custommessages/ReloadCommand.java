package com.tuservidor.custommessages;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ReloadCommand implements SimpleCommand {

    private final MessagesConfig messagesConfig;

    public ReloadCommand(MessagesConfig messagesConfig) {
        this.messagesConfig = messagesConfig;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            messagesConfig.reload();
            invocation.source().sendMessage(
                Component.text("VelocityCustomMessages: messages.yml recargado correctamente.", NamedTextColor.GREEN)
            );
        } else {
            invocation.source().sendMessage(
                Component.text("Uso: /customessages reload", NamedTextColor.YELLOW)
            );
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("customessages.reload")
            || !invocation.source().getClass().getSimpleName().equals("ConnectedPlayer");
    }
}
