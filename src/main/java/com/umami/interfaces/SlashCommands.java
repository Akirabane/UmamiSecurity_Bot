package com.umami.interfaces;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface SlashCommands {
    void execute(SlashCommandInteractionEvent event);
}
