package dev.jgwoll.kottbot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.jgwoll.kottbot.MusicController;
import dev.jgwoll.kottbot.Util;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class PauseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        if(!(event.getName().equals("pause"))) return;

        if(!Util.checkCommandChannel(event.getTextChannel(), event.getMember())) return;

        if(!Util.checkDJPermissions(event.getMember())) return;

        MusicController controller = Main.INSTANCE.playerManager.getController(event.getGuild().getIdLong());

        AudioPlayer player = controller.getPlayer();

        EmbedBuilder builder = new EmbedBuilder();

        if(player.isPaused()) {

            builder.setDescription("▶️  Resumed the current track.");
            builder.setColor(Color.GREEN);

        } else {

            builder.setDescription("⏸  Paused the current track.");
            builder.setColor(Color.YELLOW);

        }

        player.setPaused(!player.isPaused());
        event.replyEmbeds(builder.build()).queue();

    }

}
