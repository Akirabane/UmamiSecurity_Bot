package com.umami.eventListeners;

import com.umami.interfaces.SlashCommands;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {
    private final Map<String, SlashCommands> commands;

    public CommandHandler() {
        this.commands = new HashMap<>();
    }

    public void registerCommand(String name, SlashCommands command) {
        commands.put(name, command);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getGuild() == null) return;

        String commandName = event.getName();
        if (commands.containsKey(commandName)) {
            SlashCommands command = commands.get(commandName);
            command.execute(event);
        } else {
            event.reply("Commande inconnue.").queue();
        }
    }
}
