package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.audio.LotusPlayer;

import java.util.List;

public class LeaveChannel implements Command {
    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        LotusPlayer player = LotusPlayer.getInstance(channel.getGuild());
        Member self = channel.getGuild().getSelfMember();

        if (!player.isQueueEmpty()){
            Lotus.sendMessage("I am playing music right now. Please wait until I've played all the queued songs.",
                    channel);
            return;
        }

        if (!self.getVoiceState().inVoiceChannel()){
            Lotus.sendMessage("I am not currently in a voice channel.", channel);
            return;
        }

        Lotus.sendMessage("Leaving voice channel...", channel);
        AudioManager manager = channel.getGuild().getAudioManager();
        manager.closeAudioConnection();
    }
}
