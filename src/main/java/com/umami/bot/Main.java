package com.umami.bot;

import com.umami.commands.Tps;
import com.umami.database.DatabaseManager;
import com.umami.eventListeners.CommandHandler;
import com.umami.eventListeners.JoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        DatabaseManager db = null;
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
            db = new DatabaseManager("jdbc:mysql://", "localhost:3306", "umami", "root", "root");
            db.connexion();
            //création des tables par défault
            db.executeSQLScriptFromFile("src/main/resources/SQL/TABLE_ROLES.sql");
            db.executeSQLScriptFromFile("src/main/resources/SQL/INSERT_ROLES.sql");
            db.executeSQLScriptFromFile("src/main/resources/SQL/TABLE_USERS.sql");

            //register database events
            JoinEvent joinEvent = new JoinEvent(db);
            shardManager.addEventListener(joinEvent);

            //Bot is ready
            System.out.println("Le bot est prêt !");
            System.out.println("Shards :" + shardManager.getShardsTotal());

        } catch (Exception e) {
            System.out.println("Le bot n'est pas prêt !");
            e.printStackTrace();
        }
    }
}
