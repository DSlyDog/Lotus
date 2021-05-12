package net.whispwriting.lotus.command.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.Announcer;
import net.whispwriting.lotus.util.JsonFile;

import java.io.IOException;
import java.util.List;

public class CreateAnnounceChannel implements Command {

    private Lotus bot = Lotus.getInstance();

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        boolean permission = false;
        for (Role role : sender.getRoles()){
            if (role.getName().equals("Co Owner"))
                permission = true;
        }

        if (!permission){
            bot.sendMessage("You do not have permission to use that command.", channel);
            return;
        }

        if (args.length < 2){
            bot.sendMessage("Please provide the announcer name and the output channel.", channel);
            return;
        }

        JsonFile file = new JsonFile(args[0], "announcers");
        String avatarURL = file.getString("avatar");

        if (avatarURL.equals("")){
            bot.sendMessage("Announcer not found: " + args[0], channel);
            return;
        }

        TextChannel outputChannel = bot.getChannel(args[1]);

        if (outputChannel == null){
            bot.sendMessage("Channel not found: " + args[1], channel);
        }

        if (bot.getAnnouncerChannels().containsKey(channel)){
            bot.sendMessage("An auto announcer already exists for this channel.", channel);
        }

        JsonFile announcerFile = new JsonFile(channel.getName(), "auto announcers");
        announcerFile.set("announcer", args[0]);
        announcerFile.set("output", outputChannel.getName());
        try {
            announcerFile.save();
        } catch (IOException e) {
            bot.sendMessage("Failed to save auto announcer. Please notify Maika and try again later.", channel);
            e.printStackTrace();
        }

        Announcer announcer = new Announcer(args[0], outputChannel);

        bot.getAnnouncerChannels().put(channel, announcer);
        bot.sendMessage("Messages sent in this channel will now be announced in \"" + args[1] + "\"", channel);
    }
}
