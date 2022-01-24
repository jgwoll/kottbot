package dev.jgwoll.kottbot;

import dev.jgwoll.kottbot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<String> getQueue() {

        List<String> queue = new ArrayList<>();

        File file = new File("queue.txt");

        if(!file.exists()) {

            try {

                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return queue;
        }

        try {

            queue = Files.readAllLines(file.toPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return queue;

    }

    public static void addToQueue(String url) {

        try {

            File file = new File("queue.txt");
            String uri;

            if(getQueue().size() > 0) uri = "\n" + url;
            else uri = url;

            Files.writeString(file.toPath(), uri, StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeFromQueue() {

        try {

            File file = new File("queue.txt");

            List<String> queue = getQueue();

            if(queue.size() > 0) {

                queue.remove(0);

                if (queue.size() > 0) {

                    StringBuilder builder = new StringBuilder();
                    builder.append(queue.get(0));
                    queue.remove(0);

                    for (String current : queue) {

                        builder.append("\n").append(current);

                    }

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(builder.toString());
                    fileWriter.close();

                } else {

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write("");
                    fileWriter.close();

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void deleteQueue() {

        try {

            File file = new File("queue.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static java.util.List<Message> getMessageHistory(MessageChannel channel, int amount) {

        List<Message> messages = new ArrayList<>();
        int i = amount + 1;

        for(Message  message : channel.getIterableHistory().cache(false)) {

            messages.add(message);

            if(--i <= 0) break;

        }

        return messages;
    }

    public static boolean checkPermissions(Member member, Permission permission) {

        if(!member.hasPermission(Permission.ADMINISTRATOR)) {

            EmbedBuilder builder = Main.INSTANCE.getDMBuilder();
            builder.setDescription("ERROR | You don't have the permissions to perform this command!");
            builder.setColor(Color.RED);

            member.getUser().openPrivateChannel().queue(tc ->
                    tc.sendMessageEmbeds(builder.build()).queue());

            return false;

        }

        return true;

    }

    public static boolean checkRole(Member member, Role role) {

        if(!Main.INSTANCE.getGuild().getMembersWithRoles(role).contains(member)) {

            EmbedBuilder builder = Main.INSTANCE.getDMBuilder();
            builder.setDescription("ERROR | You don't have the permissions to perform this command!");
            builder.setColor(Color.RED);

            member.getUser().openPrivateChannel().queue(tc ->
                    tc.sendMessageEmbeds(builder.build()).queue());

            return false;

        }

        return true;

    }

    public static boolean checkDJPermissions(Member member) {

        if(Main.INSTANCE.getDjRole() == null) {

            checkPermissions(member, Permission.ADMINISTRATOR);

        } else {

            checkRole(member, Main.INSTANCE.getDjRole());

        }

        return true;

    }

    public static boolean checkCommandChannel(TextChannel channel, Member member) {

        if(!channel.equals(Main.INSTANCE.getCommandChannel())) {

            EmbedBuilder builder = Main.INSTANCE.getDMBuilder();
            builder.setDescription("ERROR | You're in the wrong channel!");
            builder.setColor(Color.RED);

            member.getUser().openPrivateChannel().queue(tc ->
                    tc.sendMessageEmbeds(builder.build()).queue());

            return false;

        } else return true;

    }

}
