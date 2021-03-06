package net.whispwriting.lotus.command.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.JsonFile;

import java.util.List;

public class Announce implements Command {

    private Lotus bot = Lotus.getInstance();

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        boolean permission = false;
        for (Role role : sender.getRoles()){
            if (role.getName().equals("Co Owner") || role.getName().equals("Admin"))
                permission = true;
        }

        if (!permission){
            bot.sendMessage("You do not have permission to use that command.", channel);
            return;
        }

        if (args.length < 3){
            bot.sendMessage("Not enough arguments, either the announcer name, output channel, or message is missing.", channel);
            return;
        }

        JsonFile file = new JsonFile(args[0], "announcers");
        String avatarURL = file.getString("avatar");

        if (avatarURL.equals("")){
            bot.sendMessage("Announcer not found: " + args[0], channel);
            return;
        }

        TextChannel outputChannel = bot.getChannel(args[1]);

        if (args[0].equals("self")){
            bot.sendMessage(args[2], outputChannel);
        }

        if (outputChannel == null){
            bot.sendMessage("Channel not found: " + args[1], channel);
        }

        Webhook outputWebhook = getWebhook(outputChannel);


        if (outputWebhook == null){
            outputChannel.createWebhook("Lotus").complete();
            outputWebhook = getWebhook(outputChannel);
        }

        WebhookClient client = buildClient(outputWebhook);
        WebhookMessage message = buildMessage(args[0], avatarURL, args[2]);
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
