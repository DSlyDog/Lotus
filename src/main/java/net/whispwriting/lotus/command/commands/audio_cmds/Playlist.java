package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.audio.LotusPlayer;

import java.util.List;

public class Playlist implements Command {

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        Member self = channel.getGuild().getSelfMember();
        GuildVoiceState voiceState = self.getVoiceState();
        GuildVoiceState memberVoiceState = sender.getVoiceState();

        if (args.length == 0){
            Lotus.sendMessage("Missing arguments: lo!play <link>", channel);
            return;
        }

        if (!voiceState.inVoiceChannel()){
            Lotus.sendMessage("I must be in a voice channel to play music.", channel);
            return;
        } else if (!memberVoiceState.inVoiceChannel()){
            Lotus.sendMessage("You must be in my voice channel to play music.", channel);
            return;
        } else if (!memberVoiceState.getChannel().getName().equals(voiceState.getChannel().getName())){
            Lotus.sendMessage("You must be in my voice channel to play music.", channel);
            return;
        }

        String link = args[0];

        if (!isUrl(args[0])){
            link = "ytsearch:" + link;
        }

        LotusPlayer.getInstance(channel.getGuild()).loadPlaylist(channel, link);
    }

    private boolean isUrl(String url){
        return url.startsWith("http");
    }
}
