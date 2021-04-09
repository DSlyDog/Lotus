package net.whispwriting.lotus.util.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayDeque;
import java.util.Queue;

public class TrackScheduler extends AudioEventAdapter {

    private AudioPlayer player;
    private Queue<AudioTrack> trackList = new ArrayDeque<>();

    public TrackScheduler(AudioPlayer player){
        this.player = player;
    }

    public void queue(AudioTrack track){
        if (!player.startTrack(track, true)){
            trackList.offer(track);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext){
            nextTrack();
        }
    }

    public boolean nextTrack(){
        if (trackList.isEmpty())
            return false;
        this.player.startTrack(this.trackList.poll(), false);
        return true;
    }

    public boolean isQueueEmpty(){
        return trackList.isEmpty();
    }

    public void clearQueue(){
        trackList.clear();
    }

    public boolean isPlaying(){
        AudioTrack track = player.getPlayingTrack();
        return track != null;
    }
}
