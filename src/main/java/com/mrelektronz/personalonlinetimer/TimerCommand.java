package com.mrelektronz.personalonlinetimer;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.mrelektronz.personalonlinetimer.PersonalOnlineTimer.formatDateTime;
import static com.mrelektronz.personalonlinetimer.PersonalOnlineTimer.getPlayerTicksOnline;

public class TimerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player && args.length == 0) {
            Player player = (Player) sender;
            TextComponent dateTimeComponent = formatDateTime(getPlayerTicksOnline(player));
            player.sendMessage(dateTimeComponent);
            return true;
        }
        if(sender.isOp() && args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            PersonalOnlineTimer.plugin.showInActionBar = !PersonalOnlineTimer.plugin.showInActionBar;
            PersonalOnlineTimer.plugin.config.set("showTimer", PersonalOnlineTimer.plugin.showInActionBar);
            PersonalOnlineTimer.plugin.saveConfig();
            return true;
        }
        if(sender.isOp() && args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if(target == null) {
                return false;
            }
            target.setStatistic(Statistic.PLAY_ONE_MINUTE, 0);
            sender.sendMessage("Reset the time for this player");
            return true;
        }

        return false;
    }
}
