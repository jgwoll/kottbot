package dev.jgwoll.kottbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.jgwoll.kottbot.MusicController;
import dev.jgwoll.kottbot.Util;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class StopCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if(!(event.getName().equals("stop"))) return;

        if(!Util.checkCommandChannel(event.getTextChannel(), event.getMember())) return;

        if(!Util.checkDJPermissions(event.getMember())) return;

        MusicController controller = Main.INSTANCE.playerManager.getController(event.getGuild().getIdLong());

        AudioPlayer player = controller.getPlayer();

        Util.deleteQueue();
        player.stopTrack();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("⏹  Stopped the current track and deleted the queue!");
        builder.setColor(Color.GREEN);
        event.replyEmbeds(builder.build()).queue();

    }

}
