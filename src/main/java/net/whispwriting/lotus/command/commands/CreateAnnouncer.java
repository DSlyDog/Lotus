package net.whispwriting.lotus.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.JsonFile;

import java.io.IOException;
import java.util.List;

public class CreateAnnouncer implements Command {

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        boolean permission = false;
        for (Role role : sender.getRoles()){
            if (role.getName().equals("Co Owner") || role.getName().equals("Jodas' Bitch"))
                permission = true;
        }

        if (!permission){
            Lotus.sendMessage("You do not have permission to use that command.", channel);
            return;
        }

        if (args.length != 1){
            Lotus.sendMessage("Incorrect argument count. Please only provide the name and attach an icon", channel);
            return;
        }

        if (args[0].equals("self")){

        }

        if (attachments.size() < 1 && !args[0].equals("self")){
            Lotus.sendMessage("You must attach an image for the announcer's avatar.", channel);
            return;
        }

        JsonFile file = new JsonFile(args[0], "announcers");
        if (args[0].equals("self"))
            file.set("avatar", Lotus.avatar);
        else
            file.set("avatar", attachments.get(0).getUrl());
        try {
            file.save();
            Lotus.sendMessage("Successfully created announcer", channel);
        } catch (IOException e) {
            Lotus.sendMessage("Could not create announcer; check the bot's console.", channel);
            e.printStackTrace();
        }
    }
}
