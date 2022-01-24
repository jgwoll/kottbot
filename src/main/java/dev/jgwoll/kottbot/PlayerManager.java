package dev.jgwoll.kottbot;

import dev.jgwoll.kottbot.main.Main;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    public ConcurrentHashMap<Long, MusicController> controller;

    public PlayerManager() {
        this.controller = new ConcurrentHashMap<>();
    }

    public MusicController getController(long guildID) {

        MusicController musicController = null;

        if(controller.containsKey(guildID)) {

            musicController = controller.get(guildID);

        } else {

            musicController = new MusicController(Main.INSTANCE.jda.getGuildById(guildID));

            this.controller.put(guildID, musicController);

        }

        return musicController;

    }

    public long getGuildByPlayerHash(int hash) {

        for(MusicController controller : this.controller.values()) {

            if(controller.getPlayer().hashCode() == hash) {

                return controller.getGuild().getIdLong();

            }

        }

        return -1;
    }

}
