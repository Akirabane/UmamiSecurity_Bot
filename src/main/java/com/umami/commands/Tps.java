package com.umami.commands;

import com.umami.interfaces.SlashCommands;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Duration;
import java.time.Instant;

public class Tps implements SlashCommands {
    private Instant lastInvocationTime;
    private int transactions;

    public Tps() {
        lastInvocationTime = Instant.now();
        transactions = 0;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        if (!event.getGuild().getId().equals("1217876786768248923")) {
            event.reply("Cette commande ne peut être exécutée que dans le serveur du Umami.").setEphemeral(true).queue();
            return;
        }

        // Calcul du temps écoulé depuis la dernière invocation en millisecondes
        Instant now = Instant.now();
        long timeElapsedMillis = Duration.between(lastInvocationTime, now).toMillis();

        // Incrémenter le nombre de transactions
        transactions++;

        // Calculer le TPS en millisecondes avec arrondi à la décimale supérieure
        double tps = (double) transactions / timeElapsedMillis * 1000; // Convertir en TPS par seconde
        double roundedTps = Math.ceil(tps * 10) / 10; // Arrondir à une décimale

        // Mettre à jour le temps de la dernière invocation
        lastInvocationTime = now;

        // Répondre avec le TPS calculé en millisecondes
        event.reply("Le TPS du bot est actuellement : " + roundedTps + " ms").queue();
    }
}
