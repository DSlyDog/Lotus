package net.whispwriting.lotus.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.EmoteImpl;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.util.Announcer;
import net.whispwriting.lotus.util.JsonFile;

import java.util.List;

public class MessageEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        Lotus bot = Lotus.getInstance();
        TextChannel channel = event.getChannel();

        if (event.getMessage().getContentRaw().startsWith(bot.getPrefix()))
            return;

        if (bot.getAnnouncerChannels().containsKey(channel) && !event.getAuthor().isBot()){
            announce(event.getChannel(), event.getMessage());
            return;
        }

        JsonFile file = new JsonFile("role message", "messages");
        String messageText = file.getString("messageText");
        if (event.getMessage().getContentRaw().equals(messageText)){
            event.getMessage().addReaction("\\u2B50").queue();
            return;
        }
    }

    public void announce(TextChannel channel, Message input){
        Lotus bot = Lotus.getInstance();
        Announcer announcer = bot.getAnnouncerChannels().get(channel);
        JsonFile jsonFile = new JsonFile(announcer.getName(), "announcers");
        String avatar = jsonFile.getString("avatar");
        TextChannel outputChannel = announcer.getOutput();
        if (announcer.getName().equals("self")){
            bot.sendMessage(input, outputChannel);
            return;
        }

        Webhook outputWebhook = getWebhook(outputChannel);

        if (outputWebhook == null){
            outputChannel.createWebhook("Lotus").complete();
            outputWebhook = getWebhook(outputChannel);
        }

        WebhookClient client = buildClient(outputWebhook);
        WebhookMessage message = buildMessage(announcer.getName(), avatar, input.getContentRaw());
        client.send(message);
    }

    private Webhook getWebhook(TextChannel channel){
        List<Webhook> hooks = channel.retrieveWebhooks().complete();
        for (Webhook hook : hooks){
            if (hook.getName().equals("Lotus")){
                return hook;
            }
        }
        return null;
    }

    private WebhookClient buildClient(Webhook webhook){
        WebhookClientBuilder builder = new WebhookClientBuilder(webhook.getUrl());
        builder.setWait(true);
        return builder.build();
    }

    private WebhookMessage buildMessage(String name, String avatarURL, String message){
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(name);
        builder.setAvatarUrl(avatarURL);
        builder.setContent(message);
        return builder.build();
    }
}
