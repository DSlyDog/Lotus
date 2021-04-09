package net.whispwriting.lotus.util.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class SoundHandler implements AudioSendHandler {

    private AudioPlayer player;
    private ByteBuffer buffer;
    private MutableAudioFrame audioFrame;

    public SoundHandler (AudioPlayer player){
        this.player = player;
        this.buffer = ByteBuffer.allocate(1024);
        this.audioFrame = new MutableAudioFrame();
        this.audioFrame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return this.player.provide(this.audioFrame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
