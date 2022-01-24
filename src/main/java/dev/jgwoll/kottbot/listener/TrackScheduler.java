package dev.jgwoll.kottbot.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.jgwoll.kottbot.AudioLoadResultHandler;
import dev.jgwoll.kottbot.Util;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TrackScheduler extends AudioEventAdapter {

    private TextChannel channel = Main.INSTANCE.getMusicChannel();

    @Override
    public void onPlayerPause(AudioPlayer player) {

        if (Util.getMessageHistory(channel, 1).size() != 0) {
            Util.getMessageHistory(channel, 1).get(0).delete().queue();
        }

        sendInfoToMusicChannel(player, player.getPlayingTrack(), true);

    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        sendInfoToMusicChannel(player, player.getPlayingTrack(), false);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        sendInfoToMusicChannel(player, track, false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if(Util.getQueue().size() == 0) {

            if (channel != null) {

                if (Util.getMessageHistory(channel, 1).size() != 0) {
                    Util.getMessageHistory(channel, 1).get(0).delete().queue();
                }

                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("⏹  Currently stopped.");
                builder.setColor(Color.RED);

                channel.sendMessageEmbeds(builder.build()).queue();

            }

        } else {

            String url = Util.getQueue().get(0);
            Util.removeFromQueue();

            AudioPlayerManager playerManager = Main.INSTANCE.audioPlayerManager;

            playerManager.loadItem(url, new AudioLoadResultHandler(player));

        }

    }

    private void sendInfoToMusicChannel(AudioPlayer player, AudioTrack track, boolean paused) {

        AudioTrackInfo info = track.getInfo();

        EmbedBuilder builder = new EmbedBuilder();
        if(paused) {
            builder.setColor(Color.YELLOW);
            builder.setDescription("⏸ Paused:");
        }
        else {
            builder.setColor(Color.GREEN);
            builder.setDescription("\uD83D\uDCBF  Now playing:");
        }

        long seconds = info.length / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes %= 60;
        seconds %= 60;

        String url = info.uri;

        builder.addField(info.author, "[" + info.title + "](" + url + ")", false);
        builder.addField("Length", info.isStream ? "\uD83D\uDD34  STREAM" : (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + seconds + "s", true);

        if(url.startsWith("https://www.youtube.com/watch?v=")) {

            String videoID = url.replace("https://www.youtube.com/watch?v=", "");

            InputStream file;

            try {

                file = new URL("https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg").openStream();
                builder.setImage("attachment://thumbnail.png");

                if(channel != null) {

                    if(Util.getMessageHistory(channel, 1).size() != 0) {
                        Util.getMessageHistory(channel, 1).get(0).delete().queue();
                    }

                    channel.sendTyping().queue();

                    if(paused) channel.sendFile(file, "thumbnail.png").setEmbeds(builder.build()).setActionRow(Button.danger("stop", Emoji.fromUnicode("⏹")), Button.success("resume", Emoji.fromUnicode("▶️")), Button.secondary("skip", Emoji.fromUnicode("⏭"))).queue();
                    else channel.sendFile(file, "thumbnail.png").setEmbeds(builder.build()).setActionRow(Button.danger("stop", Emoji.fromUnicode("⏹")), Button.success("pause", Emoji.fromUnicode("⏸")), Button.secondary("skip", Emoji.fromUnicode("⏭"))).queue();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            if(channel != null) {

                channel.sendMessageEmbeds(builder.build()).queue();

            }

        }

    }
}
