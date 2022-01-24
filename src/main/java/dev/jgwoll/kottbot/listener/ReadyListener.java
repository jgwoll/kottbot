package dev.jgwoll.kottbot.listener;

import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

        YamlFile file = new YamlFile("config.yml");

        String djRoleID = file.getString("djRoleID");
        String musicChannelID = file.getString("musicChannelID");
        String commandChannelID = file.getString("commandChannelID");
        String tempChannelID = file.getString("tempChannelID");

        if(Main.INSTANCE.jda.getGuilds().isEmpty()) {

            System.out.println("The Bot is not on a Guild! Shutdown...");

            Main.INSTANCE.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            Main.INSTANCE.jda.shutdown();

            System.out.println("Bot offline.");

            return;

        } else {

            Guild guild = Main.INSTANCE.jda.getGuilds().get(0);

            Main.INSTANCE.setGuild(guild);
            Main.INSTANCE.setDMBuilder(new EmbedBuilder()
                    .setAuthor(guild.getName(), null, guild.getIconUrl())
                    .setFooter("KottBot developed by jgwoll#0001.", "https://i.ytimg.com/vi/iCnL9RKr9Kg/maxresdefault.jpg"));

        }

        if(!djRoleID.equals("")) {

            if (Main.INSTANCE.getGuild().getRoleById(djRoleID) == null) {

                System.out.println("The Role from provided dj-role-id has not been found on your server.");
                Main.INSTANCE.setDjRole(null);

            } else {

                Main.INSTANCE.setDjRole(Main.INSTANCE.getGuild().getRoleById(djRoleID));

            }

        } else {

            System.out.println("Please provide the dj-role-id!");

        }

        if(!musicChannelID.equals("")) {

            if (Main.INSTANCE.getGuild().getTextChannelById(musicChannelID) == null) {

                System.out.println("The Channel from provided music-channel-id has not been found on your server.");
                Main.INSTANCE.setMusicChannel(null);

            } else {

                Main.INSTANCE.setMusicChannel(Main.INSTANCE.getGuild().getTextChannelById(musicChannelID));

            }

        } else {

            System.out.println("Please provide the music-channel-id!");

        }

        if(!musicChannelID.equals("")) {

            if (Main.INSTANCE.getGuild().getTextChannelById(commandChannelID) == null) {

                System.out.println("The Channel from provided command-channel-id has not been found on your server.");
                Main.INSTANCE.setCommandChannel(null);

            } else {

                Main.INSTANCE.setCommandChannel(Main.INSTANCE.getGuild().getTextChannelById(commandChannelID));

            }

        } else {

            System.out.println("Please provide the command-channel-id!");

        }

        if(!tempChannelID.equals("")) {

            if (Main.INSTANCE.getGuild().getVoiceChannelById(tempChannelID) == null) {

                System.out.println("The Channel from provided temp-channel-id has not been found on your server.");
                Main.INSTANCE.setTempChannel(null);

            } else {

                Main.INSTANCE.setTempChannel(Main.INSTANCE.getGuild().getVoiceChannelById(tempChannelID));

            }

        } else {

            System.out.println("Please provide the temp-channel-id!");

        }

    }

}
