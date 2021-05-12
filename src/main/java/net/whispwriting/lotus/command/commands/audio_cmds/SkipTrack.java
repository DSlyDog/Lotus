package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.audio.LotusPlayer;

import java.util.List;

public class SkipTrack implements Command {

    private Lotus bot = Lotus.getInstance();

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        LotusPlayer player = LotusPlayer.getInstance(channel.getGuild());

        GuildVoiceState memberState = sender.getVoiceState();
        GuildVoiceState botState = channel.getGuild().getSelfMember().getVoiceState();

        if (!botState.inVoiceChannel()){
            bot.sendMessage("I am not in a voice channel right now.", channel);
            return;
        }

        if (!memberState.inVoiceChannel() || !memberState.getChannel().getName().equals(botState.getChannel().getName())){
            bot.sendMessage("You cannot skip a song if you are not in my voice channel.", channel);
            return;
        }

        if (!player.isPlaying()){
            bot.sendMessage("Nothing is playing right now.", channel);
            return;
        }

        if (!player.skip()){
            bot.sendMessage("There queue is empty. I cannot skip.", channel);
        }else{
            bot.sendMessage("Skipping current track...", channel);
        }
    }
}
