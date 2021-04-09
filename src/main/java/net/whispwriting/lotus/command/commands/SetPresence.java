package net.whispwriting.lotus.command.commands;

import net.dv8tion.jda.api.entities.*;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;

import java.util.List;

public class SetPresence implements Command {

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

        if (args.length < 2){
            Lotus.sendMessage("Missing argument. Please provide the activity type and activity.", channel);
            return;
        }

        switch (args[0].toLowerCase()){
            case "playing":
                Lotus.setPresence(Activity.playing(args[1]));
                Lotus.sendMessage("Status updated.", channel);
                break;
            case "watching":
                Lotus.setPresence(Activity.watching(args[1]));
                Lotus.sendMessage("Status updated.", channel);
                break;
            case "listening":
                Lotus.setPresence(Activity.listening(args[1]));
                Lotus.sendMessage("Status updated.", channel);
                break;
            case "competing":
                Lotus.setPresence(Activity.competing(args[1]));
                Lotus.sendMessage("Status updated.", channel);
                break;
            case "streaming":
                Lotus.setPresence(Activity.streaming(args[1], args[2]));
                Lotus.sendMessage("Status updated.", channel);
                break;
            default:
                Lotus.sendMessage(args[1] + " is an invalid activity type.", channel);
                break;
        }
    }
}
