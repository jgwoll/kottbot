package dev.jgwoll.kottbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.awt.*;

public class AudioLoadResultHandler implements com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler {

    private AudioPlayer player;
    private SlashCommandEvent event;

    public AudioLoadResultHandler(SlashCommandEvent event, AudioPlayer player) {
        this.event = event;
        this.player = player;
    }

    public AudioLoadResultHandler(AudioPlayer player) {
        this.player = player;
        this.event = null;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        EmbedBuilder builder = new EmbedBuilder();
        if(player.getPlayingTrack() == null) {
            player.playTrack(track);
            builder.setDescription("\uD83C\uDFA7 Played Track **" + "[" + track.getInfo().title + "](" + track.getInfo().uri + ")" + "**!");
        } else {
            Util.addToQueue(track.getInfo().uri);
            builder.setDescription("\uD83C\uDFA7 Added Track **" + "[" + track.getInfo().title + "](" + track.getInfo().uri + ")" + "** to the end of the queue!");
        }

        builder.setColor(Color.GREEN);
        if(event != null) {
            event.replyEmbeds(builder.build()).queue();
        }

    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack track = playlist.getTracks().get(0);
        EmbedBuilder builder = new EmbedBuilder();
        if(player.getPlayingTrack() == null) {
            player.playTrack(track);
            builder.setDescription("\uD83C\uDFA7 Played Track **" + "[" + track.getInfo().title + "](" + track.getInfo().uri + ")" + "**!");
        } else {
            Util.addToQueue(track.getInfo().uri);
            builder.setDescription("\uD83C\uDFA7 Added Track **" + "[" + track.getInfo().title + "](" + track.getInfo().uri + ")" + "** to the end of the queue!");
        }

        builder.setColor(Color.GREEN);
        if(event != null) {
            event.replyEmbeds(builder.build()).queue();
        }
    }

    @Override
    public void noMatches() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("No matches.");
        builder.setColor(Color.RED);
        if(event != null) {
            event.replyEmbeds(builder.build()).queue();
        }
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Failed loading.");
        builder.setColor(Color.RED);
        if(event != null) {
            event.replyEmbeds(builder.build()).queue();
        }
        exception.printStackTrace();
    }

}
