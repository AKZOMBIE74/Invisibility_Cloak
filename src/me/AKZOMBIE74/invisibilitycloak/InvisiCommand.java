package me.AKZOMBIE74.invisibilitycloak;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class InvisiCommand implements CommandExecutor {

    @Override
    public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;
        if(sender instanceof Player) {
            if(cmd.getName().equalsIgnoreCase("ic")) {
                if(p.hasPermission("ic.command")) {
                    if(args.length > 0) {
                        if(args[0].equalsIgnoreCase("give")) {
                            if(Bukkit.getPlayerExact(args[1]) != null) {
                                Bukkit.getPlayerExact(args[1]).getInventory().addItem(Main.a);
                            } else if(Bukkit.getPlayerExact(args[1]) == null) {
                                p.sendMessage(ChatColor.DARK_RED + "[Error] Try Again!");
                                p.sendMessage(ChatColor.DARK_RED + "[Error] That player is not online or you typed in their name wrong.");
                                p.sendMessage(ChatColor.GOLD + "[Tip] Do /ic - to give you a list of the commands.");
                            }
                        } else if(args[0].equalsIgnoreCase("me")) {
                            //give player cloak
                            p.getInventory().addItem(Main.a);
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        Main.getInstance().reloadConfig();
                        Main.getInstance().checkForUpdates();
                        Main.getInstance().updateMessage(p);
                        p.sendMessage("Successfully reloaded Invisibility_Cloak!");
                    } else if(args.length == 0) {
                        Main.helpMessage(p);
                    }
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "You do not have the permission \'ic.command\'");
                }
            }
        } else {
            Main.getInstance().getLogger().info("Only a player can use this command.");
        }

        return false;
    }
}