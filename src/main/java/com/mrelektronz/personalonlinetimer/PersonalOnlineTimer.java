package com.mrelektronz.personalonlinetimer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PersonalOnlineTimer extends JavaPlugin {

    public static PersonalOnlineTimer plugin;
    boolean showInActionBar = true;
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        initConfig();
        runUpdateOnlineTime();
        this.getCommand("time").setExecutor(new TimerCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initConfig() {
        config.addDefault("showTimer", true);
        config.options().copyDefaults(true);
        saveConfig();
        showInActionBar = config.getBoolean("showTimer");
    }


    private void runUpdateOnlineTime() {
        Bukkit.getScheduler().runTaskTimer(PersonalOnlineTimer.this, () -> {
            if(!showInActionBar) {
                return;
            }
            Bukkit.getOnlinePlayers().forEach(player -> {

                TextComponent dateTimeComponent = formatDateTime(getPlayerTicksOnline(player));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, dateTimeComponent);
            });
        }, 0L, 20L);
    }

    public static TextComponent formatDateTime(int time) {
        var component = new TextComponent(ticksToDateTimeString(time));
        component.setColor(ChatColor.DARK_PURPLE);
        return component;
    }

    public static int getPlayerTicksOnline(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE);
    }

    private static String ticksToDateTimeString(int ticks) {
        int baseSeconds = ticks / 20;
        int seconds = baseSeconds % 60;
        int minutes = (baseSeconds / 60) % 60;
        int hours = (baseSeconds / 3600) % 24;
        int days = baseSeconds / 86400;
        String text = "";
        if(days > 0) {
            text += days + "d ";
        }
        if(hours > 0) {
            text += hours + "h ";
        }
        text += minutes + "m ";
        text += seconds + "s ";
        return text;
    }
}
