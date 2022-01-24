package dev.jgwoll.kottbot.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.jgwoll.kottbot.MusicController;
import dev.jgwoll.kottbot.Util;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class MusicButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {

        if(!Util.checkDJPermissions(event.getMember())) return;

        MusicController controller = Main.INSTANCE.playerManager.getController(event.getGuild().getIdLong());

        AudioPlayer player = controller.getPlayer();

        if(event.getButton().getId().equals("resume")) {

            player.setPaused(false);

        } else if(event.getButton().getId().equals("pause")) {

            player.setPaused(true);

        } else if(event.getButton().getId().equals("skip")) {

            player.stopTrack();

        } else if(event.getButton().getId().equals("stop")) {

            Util.deleteQueue();
            player.stopTrack();

        }

    }

}
