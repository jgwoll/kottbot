package dev.jgwoll.kottbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.jgwoll.kottbot.listener.TrackScheduler;
import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

    private Guild guild;
    private AudioPlayer player;

    public MusicController(Guild guild) {

        this.guild = guild;
        this.player = Main.INSTANCE.audioPlayerManager.createPlayer();

        this.guild.getAudioManager().setSendingHandler(new AudioPlayerSentHandler(player));
        this.player.addListener(new TrackScheduler());
        this.player.setVolume(10);
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioPlayer getPlayer() {
        return player;
    }
}
