package me.AKZOMBIE74.invisibilitycloak;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class InvisibilityUse implements Listener {

    private ItemStack a;

    String GLOBAL_VERSION_TEXT;

    @EventHandler
    private void interactEvent(PlayerInteractEvent e) {
        if (Main.getInstance().getConfig().getBoolean("enabled")) {
            List<String> var2 = Main.getInstance().getConfig().getStringList("put-on-messages");
            List<String> var3 = Main.getInstance().getConfig().getStringList("remove-messages");
            Player p = e.getPlayer();
            if (Main.b.contains("v1_8") || Main.b.contains("v1_9") || Main.b.contains("v1_10")) {
                this.a = p.getItemInHand();
            } else {
                this.a = p.getInventory().getItemInMainHand();
            }

            String message;
            Iterator iterator;
            if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && p.hasPermission("ic.wear") && this.a.equals(Main.a) && !p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                if (Main.b.contains("v1_10") || Main.b.contains("v1_9") || Main.b.contains("v1_8") || Main.b.contains("v1_11") || Main.b.contains("v1_12")) {
                    p.getWorld().playSound(p.getLocation(), Sound.valueOf("ENTITY_ENDERDRAGON_FLAP"), 1.0F, 1.0F);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.valueOf("ENDERDRAGON_WINGS"), 1.0F, 1.0F);
                }

                if (Main.getInstance().getConfig().getBoolean("duration-forever")) {
                    Main.getInstance().getConfig().getStringList("effects-while-wearing").forEach(effect -> {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), Integer.MAX_VALUE, 1));
                    });
                } else if (!Main.getInstance().getConfig().getBoolean("duration-forever")) {
                    Main.getInstance().getConfig().getStringList("effects-while-wearing").forEach(effect -> {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect), 20 * Main.getInstance().getConfig().getInt("duration"), 1));
                    });
                }
                p.playEffect(EntityEffect.FIREWORK_EXPLODE);
                if (var2 != null) {
                    iterator = var2.iterator();

                    while (iterator.hasNext()) {
                        message = (String) iterator.next();
                        if (message != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }

                    return;
                }
            } else if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && p.hasPermission("ic.remove") && this.a.equals(Main.a) && p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                if (Main.b.contains("v1_10") || Main.b.contains("v1_9") || Main.b.contains("v1_11") || Main.b.contains("v1_12")) {
                    p.getWorld().playSound(p.getLocation(), Sound.valueOf("ENTITY_ENDERDRAGON_FLAP"), 1.0F, 1.0F);
                } else {
                    p.getWorld().playSound(p.getLocation(), Sound.valueOf("ENDERDRAGON_WINGS"), 1.0F, 1.0F);
                }

                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.playEffect(EntityEffect.FIREWORK_EXPLODE);
                if (var3 != null) {
                    iterator = var3.iterator();

                    while (iterator.hasNext()) {
                        message = (String) iterator.next();
                        if (message != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                }
            }
        }

    }
}