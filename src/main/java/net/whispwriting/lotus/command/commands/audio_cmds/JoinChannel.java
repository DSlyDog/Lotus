package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.internal.managers.AudioManagerImpl;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;

import java.util.List;

public class JoinChannel implements Command {

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        if (args.length < 1){
            Lotus.sendMessage("Please provide the voice channel you would like Lotus to join.", channel);
            return;
        }

        Guild guild = channel.getGuild();
        VoiceChannel voiceChannel;

        try {
            voiceChannel = guild.getVoiceChannelsByName(args[0], true).get(0);
        }catch(ArrayIndexOutOfBoundsException e){
            Lotus.sendMessage("No channel was found with that name.", channel);
            return;
        }

        AudioManager manager = channel.getGuild().getAudioManager();;
        manager.openAudioConnection(voiceChannel);
        Lotus.sendMessage("Connecting to <#" + voiceChannel.getId() + ">.", channel);
    }
}
