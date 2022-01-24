package dev.jgwoll.kottbot.main;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.jgwoll.kottbot.PlayerManager;
import dev.jgwoll.kottbot.commands.*;
import dev.jgwoll.kottbot.listener.MusicButtonListener;
import dev.jgwoll.kottbot.listener.ReadyListener;
import dev.jgwoll.kottbot.listener.VoiceListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.exceptions.InvalidConfigurationException;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static Main INSTANCE;

    public JDA jda;

    public AudioPlayerManager audioPlayerManager;
    public PlayerManager playerManager;

    private Guild guild;
    private Role djRole;
    private TextChannel musicChannel;
    private TextChannel commandChannel;
    private VoiceChannel tempChannel;

    private EmbedBuilder DMBuilder;

    public static void main(String[] args) {

        try {
            new Main();
        } catch (LoginException | IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }

    }

    public Main() throws LoginException, IOException, InvalidConfigurationException {

        INSTANCE = this;

        YamlFile file = new YamlFile("config.yml");
        if(!file.exists()) {
            file.createNewFile(false);
            file.addDefault("token", "");
            file.addDefault("djRoleID", "");
            file.addDefault("musicChannelID", "");
            file.addDefault("commandChannelID", "");
            file.addDefault("tempChannelID", "");
            file.save();
        }

        file.load();

        if(file.getString("token").equals("")) {
            System.out.println("Please provide the token!");
            return;
        }

        JDABuilder jdaBuilder = JDABuilder.createDefault(file.getString("token"));

        jdaBuilder.setActivity(Activity.playing("Music-Bot developed by jgwoll"));
        jdaBuilder.setStatus(OnlineStatus.ONLINE);

        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.playerManager = new PlayerManager();

        jdaBuilder.addEventListeners(new ReadyListener());

        jdaBuilder.addEventListeners(new VoiceListener());
        jdaBuilder.addEventListeners(new MusicButtonListener());

        jdaBuilder.addEventListeners(new PlayCommand());
        jdaBuilder.addEventListeners(new StopCommand());
        jdaBuilder.addEventListeners(new QueueCommand());
        jdaBuilder.addEventListeners(new SkipCommand());
        jdaBuilder.addEventListeners(new PauseCommand());

        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);

        jda = jdaBuilder.build();

        jda.upsertCommand("play", "Plays a song.").addOptions(new OptionData(OptionType.STRING, "song", "The song to be played. Can be either an url or a search query.").setRequired(true)).queue();
        jda.upsertCommand("stop", "Stop the current queue.").queue();
        jda.upsertCommand("skip", "Skip the current track").queue();
        jda.upsertCommand("queue", "Shows the current queue.").queue();
        jda.upsertCommand("pause", "Pause or resume the queue").queue();

        System.out.println("Bot online. - Type 'exit' to shutdown!");

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);

        audioPlayerManager.getConfiguration().setFilterHotSwapEnabled(true);

        idleShutdown();

    }

    public void idleShutdown() {

        new Thread(() -> {

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {

                while ((line = reader.readLine()) != null) {

                    if(line.equalsIgnoreCase("exit")) {

                        if(jda != null) {

                            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
                            jda.shutdown();

                            System.out.println("Bot offline.");

                        }

                        reader.close();

                    } else {

                        System.out.println("Use 'exit' to Shutdown!");

                    }

                }

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }).start();

    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Role getDjRole() {
        return djRole;
    }

    public void setDjRole(Role djRole) {
        this.djRole = djRole;
    }

    public TextChannel getCommandChannel() {
        return commandChannel;
    }

    public void setCommandChannel(TextChannel commandChannel) {
        this.commandChannel = commandChannel;
    }


    public TextChannel getMusicChannel() {
        return musicChannel;
    }

    public void setMusicChannel(TextChannel musicChannel) {
        this.musicChannel = musicChannel;
    }

    public VoiceChannel getTempChannel() {
        return tempChannel;
    }

    public void setTempChannel(VoiceChannel tempChannel) {
        this.tempChannel = tempChannel;
    }

    public void setDMBuilder(EmbedBuilder DMBuilder) {
        this.DMBuilder = DMBuilder;
    }

    public EmbedBuilder getDMBuilder() {
        return DMBuilder;
    }
}
