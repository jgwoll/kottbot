package dev.jgwoll.kottbot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.concurrent.TimeUnit;

public class TrackLoader extends Thread {

    private String url;
    private AudioPlayerManager manager;
    private AudioTrack track;

    public TrackLoader(String url, AudioPlayerManager manager) {
        this.url = url;
        this.manager = manager;
    }

    public AudioTrack loadTrack() {

        manager.loadItem(url, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                setTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                setTrack(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

        try {
            TimeUnit.SECONDS.sleep(1);
            return this.track;

        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void setTrack(AudioTrack track) {
        this.track = track;
    }

}
