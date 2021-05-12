package net.whispwriting.lotus.command.commands.audio_cmds;

import net.dv8tion.jda.api.entities.*;
import net.whispwriting.lotus.Lotus;
import net.whispwriting.lotus.command.Command;
import net.whispwriting.lotus.util.audio.LotusPlayer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Play implements Command {

    private Lotus bot = Lotus.getInstance();

    @Override
    public void onCommand(Member sender, String label, String[] args, List<Message.Attachment> attachments, TextChannel channel) {
        Member self = channel.getGuild().getSelfMember();
        GuildVoiceState voiceState = self.getVoiceState();
        GuildVoiceState memberVoiceState = sender.getVoiceState();

        if (args.length == 0){
            bot.sendMessage("Missing arguments: lo!play <link>", channel);
            return;
        }

        if (!voiceState.inVoiceChannel()){
            bot.sendMessage("I must be in a voice channel to play music.", channel);
            return;
        } else if (!memberVoiceState.inVoiceChannel()){
            bot.sendMessage("You must be in my voice channel to play music.", channel);
            return;
        } else if (!memberVoiceState.getChannel().getName().equals(voiceState.getChannel().getName())){
            bot.sendMessage("You must be in my voice channel to play music.", channel);
            return;
        }

        if (args.length == 2 && args[0].equals("local")){
            playLocal(args, channel);
            return;
        }

        String link = args[0];

        if (!isUrl(args[0])){
            link = "ytsearch:" + link;
        }

        LotusPlayer.getInstance(channel.getGuild()).load(channel, link);
    }

    private void playLocal(String[] args, TextChannel channel) {
        switch (args[1].toLowerCase()){
            case "monk":
                LotusPlayer.getInstance(channel.getGuild()).load(channel, "./sounds/gtfo.wav");
                break;
            default:
                bot.sendMessage("I don't seem to have any soundtracks with that name.", channel);
        }
    }

    private boolean isUrl(String url){
        return url.startsWith("http");
    }
}
