package net.whispwriting.lotus.command.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.auto_announcer.Announcement;

import java.io.IOException;
import java.util.List;

public class Notify implements Command {

    private Lotus bot = Lotus.getInstance();

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        boolean permission = false;
        for (Role role : sender.getRoles()){
            if (role.getName().equals("Co Owner") || role.getName().equals("Officers") || role.getName().equals("Admin"))
                permission = true;
        }

        if (!permission){
            bot.sendMessage("You do not have permission to use that command.", channel);
            return;
        }

        if (args.length != 8){
            bot.sendMessage("Invalid argument length. Notify requires 8 arguments.", channel);
            return;
        }

        int targetDay = 0;
        int targetHour = 0;
        int targetMin = 0;
        int targetSec = 0;

        try{
            targetDay = Integer.parseInt(args[2]);
            targetHour = Integer.parseInt(args[3]);
            targetMin = Integer.parseInt(args[4]);
            targetSec = Integer.parseInt(args[5]);
        }catch(NumberFormatException e){
            bot.sendMessage("Please provide all day and time arguments as integers.", channel);
            return;
        }

        TextChannel annChannel = bot.getChannel(args[1]);
        if (annChannel == null){
            bot.sendMessage("Channel \"" + args[1] + "\' not found.", channel);
            return;
        }

        Role role = bot.getRole(args[6]);
        if (role == null) {
            bot.sendMessage("Role \"" + args[6] + "\" not found.", channel);
            return;
        }

        Announcement announcement = new Announcement(
                args[0],
                args[7],
                role.getId(),
                annChannel,
                targetDay,
                targetHour,
                targetMin,
                targetSec
        );
        try {
            announcement.save();
            bot.sendMessage("Message successfully added.", channel);
        } catch (IOException e) {
            bot.sendMessage("Failed to save announcement. Please check the terminal.", channel);
        }
    }
}
