package net.whispwriting.lotus.util.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicManager {

    private AudioPlayer player;
    private TrackScheduler scheduler;
    private SoundHandler soundHandler;

    public MusicManager(AudioPlayerManager manager){
        this.player = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.player);
        this.player.addListener(this.scheduler);
        this.soundHandler = new SoundHandler(this.player);
    }

    public SoundHandler getSoundHandler(){
        return soundHandler;
    }

    public void queue(AudioTrack track){
        scheduler.queue(track);
    }

    public boolean isQueueEmpty(){
        return scheduler.isQueueEmpty();
    }

    public void clearQueue(){
        scheduler.clearQueue();
    }

    public boolean isPlaying(){
        return scheduler.isPlaying();
    }

    public boolean skip() {
        return scheduler.nextTrack();
    }

    public void setVolume(int vol){
         player.setVolume(vol);
    }
}
