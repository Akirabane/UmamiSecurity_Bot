package com.umami.bot;

import com.umami.commands.Tps;
import com.umami.database.DatabaseManager;
import com.umami.eventListeners.CommandHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.sql.Statement;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        try {

            //Register commands
            CommandHandler commandHandler = new CommandHandler();
            commandHandler.registerCommand("tps", new Tps());

            String Token = System.getenv("TOKEN");

            ShardManager shardManager = DefaultShardManagerBuilder
                    .create(Token,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.MESSAGE_CONTENT,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                            GatewayIntent.SCHEDULED_EVENTS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)


                    //Event listeners
                    .addEventListeners(commandHandler)

                    //Build the bot
                    .build();

            // Déploiement des commandes pour chaque shard
            shardManager.getShards().forEach(jda -> {
                CommandListUpdateAction commands = jda.updateCommands();
                // Ajoutez vos commandes ici
                commands.addCommands(
                        Commands.slash("tps", "Donne la latence du bot.").setGuildOnly(true)
                );

                commands.queue();
            });

            //Database connexion
            DatabaseManager db = new DatabaseManager("jdbc:mysql://", "localhost:3306", "umami", "root", "root");
            db.connexion();

            // Exécution du script SQL
            String selectDBScript = "USE umami;";
            String createTableScript = "CREATE TABLE IF NOT EXISTS test (" +
                    "    test VARCHAR(20)" +
                    ");";
            db.executeSQLScript(selectDBScript);
            db.executeSQLScript(createTableScript);
            System.out.println("Script SQL exécuté avec succès.");

            //Bot is ready
            System.out.println("Le bot est prêt !");
            System.out.println("Shards :" + shardManager.getShardsTotal());

        } catch (Exception e) {
            System.out.println("Le bot n'est pas prêt !");
            e.printStackTrace();
        }
    }
}
