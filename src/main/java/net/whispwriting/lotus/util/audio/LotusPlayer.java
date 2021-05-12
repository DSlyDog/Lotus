package net.whispwriting.lotus.util.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.lotus.Lotus;

import java.util.List;

public class LotusPlayer {

    private Lotus bot = Lotus.getInstance();
    private static LotusPlayer INSTANCE;
    private MusicManager musicManager;
    private AudioPlayerManager audioManager;

    private LotusPlayer(Guild guild){
        this.audioManager = new DefaultAudioPlayerManager();
        this.musicManager = new MusicManager(audioManager);

        AudioSourceManagers.registerRemoteSources(this.audioManager);
        AudioSourceManagers.registerLocalSource(this.audioManager);
        guild.getAudioManager().setSendingHandler(musicManager.getSoundHandler());
    }

    public void load(TextChannel channel, String trackUrl){
        audioManager.loadItemOrdered(musicManager, trackUrl, new ResultHandler(musicManager, channel));
    }

    public void loadPlaylist(TextChannel channel, String trackUrl){
        audioManager.loadItemOrdered(musicManager, trackUrl, new ResultHandlerPlaylist(musicManager, channel));
    }

    public void setVolume(int vol){
        musicManager.setVolume(vol);
    }

    public boolean skip(){
        return musicManager.skip();
    }

    public boolean isQueueEmpty(){
        return musicManager.isQueueEmpty();
    }

    public boolean isPlaying(){
        return musicManager.isPlaying();
    }

    public void clearQueue(){
        musicManager.clearQueue();
    }

    public static LotusPlayer getInstance(Guild guild){
        if (INSTANCE == null){
            INSTANCE = new LotusPlayer(guild);
        }
        return INSTANCE;
    }

    private class ResultHandler implements AudioLoadResultHandler{
        private MusicManager musicManager;
        private TextChannel channel;

        public ResultHandler(MusicManager musicManager, TextChannel channel){
            this.musicManager = musicManager;
            this.channel = channel;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            musicManager.queue(audioTrack);
            bot.sendMessage("Adding to queue: " + audioTrack.getInfo().title + ", by " +
                    audioTrack.getInfo().author + "\n" + audioTrack.getInfo().uri, channel);
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            AudioTrack audioTrack = audioPlaylist.getTracks().get(0);
            musicManager.queue(audioTrack);
            bot.sendMessage("Adding to queue: " + audioTrack.getInfo().title + ", by " +
                    audioTrack.getInfo().author + "\n" + audioTrack.getInfo().uri, channel);
        }

        @Override
        public void noMatches() {

        }

        @Override
        public void loadFailed(FriendlyException e) {
            bot.sendMessage("Search failed", channel);
        }
    }

    private class ResultHandlerPlaylist implements AudioLoadResultHandler{
        private MusicManager musicManager;
        private TextChannel channel;

        public ResultHandlerPlaylist(MusicManager musicManager, TextChannel channel){
            this.musicManager = musicManager;
            this.channel = channel;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            musicManager.queue(audioTrack);
            bot.sendMessage("Playing: " + audioTrack.getInfo().title + ", by " +
                    audioTrack.getInfo().author + "\n" + audioTrack.getInfo().uri, channel);
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            bot.sendMessage("Adding " + audioPlaylist.getTracks().size() + " to queue from " +
                    audioPlaylist.getName(), channel);

            for (AudioTrack track : audioPlaylist.getTracks()){
                musicManager.queue(track);
            }
        }

        @Override
        public void noMatches() {

        }

        @Override
        public void loadFailed(FriendlyException e) {
            bot.sendMessage("Search failed", channel);
        }
    }
}
