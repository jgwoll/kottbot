package dev.jgwoll.kottbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.jgwoll.kottbot.AudioLoadResultHandler;
import dev.jgwoll.kottbot.MusicController;
import dev.jgwoll.kottbot.Util;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.Objects;

public class PlayCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if(!(event.getName().equals("play"))) return;

        if(!Util.checkCommandChannel(event.getTextChannel(), event.getMember())) return;

        if(event.getGuild().getAudioManager().getConnectedChannel() != null) {

            if (!Objects.equals(event.getMember().getVoiceState().getChannel(), event.getGuild().getAudioManager().getConnectedChannel())) {

                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("You need to be connected to **" + event.getGuild().getAudioManager().getConnectedChannel().getName() + "** to perform this command!");
                builder.setColor(Color.RED);
                event.replyEmbeds(builder.build()).queue();
                return;

            }

        }

        Member member = event.getMember();
        String song = event.getOption("song").getAsString();

        GuildVoiceState state;

        if((state = member.getVoiceState()) != null) {

            if((state.getChannel()) != null) {

                MusicController controller = Main.INSTANCE.playerManager.getController(member.getGuild().getIdLong());

                AudioPlayer player = controller.getPlayer();
                AudioPlayerManager audioPlayerManager = Main.INSTANCE.audioPlayerManager;
                AudioManager manager = member.getGuild().getAudioManager();
                manager.openAudioConnection(state.getChannel());

                if(!song.startsWith("http")) {

                    song = "ytsearch: " + song;

                }

                final String url = song;

                audioPlayerManager.loadItem(url, new AudioLoadResultHandler(event, player));

            } else {

                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("You have to be connected to a voicechannel to execute this command!");
                builder.setColor(Color.RED);
                event.replyEmbeds(builder.build()).queue();

            }

        } else {

            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("You have to be connected to a voicechannel to execute this command!");
            builder.setColor(Color.RED);
            event.replyEmbeds(builder.build()).queue();

        }

    }

}
