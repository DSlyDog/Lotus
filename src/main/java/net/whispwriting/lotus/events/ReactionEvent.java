package net.whispwriting.lotus.events;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.util.JsonFile;
import org.jetbrains.annotations.NotNull;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        Lotus bot = Lotus.getInstance();
        if (event.getMember().getUser().isBot()) {
            return;
        }
        String id = event.getChannel().getLatestMessageId();
        MessageHistory history = event.getChannel().getHistory();
        history.retrievePast(100).complete();
        Message message = history.getMessageById(id);
        JsonFile file = new JsonFile("role message", "messages");
        String messageText = file.getString("messageText");
        if (message.getContentRaw().equals(messageText)){
            MessageReaction.ReactionEmote star = MessageReaction.ReactionEmote.fromUnicode("\u2B50", bot.getJDA());
            MessageReaction.ReactionEmote megaphone = MessageReaction.ReactionEmote.fromUnicode("\uD83D\uDCE3", bot.getJDA());
            if (event.getReaction().getReactionEmote().equals(star)) {
                Role role = event.getGuild().getRolesByName("Raider Notify", false).get(0);
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            }if (event.getReaction().getReactionEmote().equals(megaphone)){
                Role role = event.getGuild().getRolesByName("Announcements", false).get(0);
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event){
        Lotus bot = Lotus.getInstance();
        if (event.getMember().getUser().isBot()) {
            return;
        }
        String id = event.getChannel().getLatestMessageId();
        MessageHistory history = event.getChannel().getHistory();
        history.retrievePast(100).complete();
        Message message = history.getMessageById(id);
        JsonFile file = new JsonFile("role message", "messages");
        String messageText = file.getString("messageText");
        if (message.getContentRaw().equals(messageText)){
            MessageReaction.ReactionEmote star = MessageReaction.ReactionEmote.fromUnicode("\u2B50", bot.getJDA());
            MessageReaction.ReactionEmote megaphone = MessageReaction.ReactionEmote.fromUnicode("\uD83D\uDCE3", bot.getJDA());
            if (event.getReaction().getReactionEmote().equals(star)) {
                Role role = event.getGuild().getRolesByName("Raider Notify", false).get(0);
                event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
            }if (event.getReaction().getReactionEmote().equals(megaphone)){
                Role role = event.getGuild().getRolesByName("Announcements", false).get(0);
                event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
            }
        }
    }
}
