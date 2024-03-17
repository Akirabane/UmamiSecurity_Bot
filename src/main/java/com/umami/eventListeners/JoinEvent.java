package com.umami.eventListeners;

import com.umami.constants.Constants;
import com.umami.dao.UserDao;
import com.umami.database.DatabaseManager;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Timestamp;

public class JoinEvent extends ListenerAdapter {
    private final UserDao userDAO;

    public JoinEvent(DatabaseManager db) {
        this.userDAO = new UserDao(db);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        //Setting the default role to the user when he joins the server
        Role role = event.getGuild().getRolesByName(Constants.DEFAULT_ROLE_NAME_ON_JOIN, true).get(0);
        if (role != null) {
            event.getGuild().addRoleToMember(event.getMember(), role).queue();
        } else {
            System.err.println("Le rôle spécifié est introuvable.");
        }

        String userId = event.getUser().getId();
        String userName = event.getUser().getName();
        Timestamp joinedAt = new Timestamp(System.currentTimeMillis());
        userDAO.addUser(userId, userName, joinedAt);

        event.getGuild().getTextChannelsByName("général", true).get(0).sendMessage("Bienvenue " + event.getUser().getAsMention() + " sur le serveur !").queue();
    }
}
