package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.audio.LotusPlayer;

import java.util.List;

public class ClearQueue implements Command {

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        LotusPlayer player = LotusPlayer.getInstance(channel.getGuild());

        if (player.isQueueEmpty()){
            Lotus.sendMessage("The queue is already empty.", channel);
            return;
        }

        player.clearQueue();
        Lotus.sendMessage("Queue cleared.", channel);
    }
}
