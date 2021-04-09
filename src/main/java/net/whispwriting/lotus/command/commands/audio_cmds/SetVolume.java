package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.audio.LotusPlayer;

import java.util.List;

public class SetVolume implements Command {

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        LotusPlayer player = LotusPlayer.getInstance(channel.getGuild());

        if (args.length < 1){
            Lotus.sendMessage("Please provide a new volume level.", channel);
            return;
        }

        int volume;

        try{
            volume = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            Lotus.sendMessage("Volume must be a number.", channel);
            return;
        }

        player.setVolume(volume);
        Lotus.sendMessage("New volume level set.", channel);
    }
}
