package dev.jgwoll.kottbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.jgwoll.kottbot.TrackLoader;
import dev.jgwoll.kottbot.Util;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class QueueCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if (!(event.getName().equals("queue"))) return;

        if(!Util.checkCommandChannel(event.getTextChannel(), event.getMember())) return;

        EmbedBuilder builder = new EmbedBuilder();
        String description;

        if (Util.getQueue().isEmpty()) {

            description = "The Current Queue is empty.";
            builder.setColor(Color.RED);

        } else {

            StringBuilder descriptionBuilder = new StringBuilder();

            for (int i = 0; i < Util.getQueue().size(); i++) {

                String current = Util.getQueue().get(i);

                AudioPlayerManager audioPlayerManager = Main.INSTANCE.audioPlayerManager;

                AudioTrack track = new TrackLoader(current, audioPlayerManager).loadTrack();

                AudioTrackInfo info = track.getInfo();

                long seconds = info.length / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                minutes %= 60;
                seconds %= 60;

                String time = info.isStream ? "\uD83D\uDD34  STREAM" : (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + seconds + "s";

                descriptionBuilder.append(i + 1).append(". **").append(info.title).append("** (").append(time).append(")\n");

            }

            descriptionBuilder.setLength(descriptionBuilder.length() - 1);
            description = descriptionBuilder.toString();
            builder.setColor(Color.GREEN);

        }

        builder.setDescription(description);
        event.replyEmbeds(builder.build()).queue();

    }

}
