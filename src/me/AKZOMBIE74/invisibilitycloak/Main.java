package me.AKZOMBIE74.invisibilitycloak;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    //Initialize cloak itemstack
    public static ItemStack a;
    //Initialize instance
    private static Main instance;
    //Initializes string for server version
    public static String b;

    String VERSION, CURRENT_VERSION, CHANGELOG;

    private boolean shouldUpdate=false;


    public void onEnable() {
        instance = this;

        //Initializes string for server version
        String[] var2;
        //Create default config
        createConfig();

        //Register interact event
        Bukkit.getServer().getPluginManager().registerEvents(new InvisibilityUse(), this);
        //Register command
        this.getCommand("ic").setExecutor(new InvisiCommand());

        CURRENT_VERSION = getInstance().getDescription().getVersion();

        //Gets server version
        b = (var2 = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")).length == 4?var2[3] + ".":"";

        //Creates Itemstack
        a = new ItemStack(Material.valueOf(getConfig().getString("invisibility-cloak-material")), 1);
        //Gets the meta of itemstack
        ItemMeta cloakMeta = a.getItemMeta();
        //Set display name of itemstack
        cloakMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("invisibility-cloak-name")));
        //Get lore from config and give it to the itemstack
        cloakMeta.setLore(this.getConfig().getStringList("invisibility-cloak-lore"));
        //Give the customized itemmeta to the itemstack
        a.setItemMeta(cloakMeta);
        //Create new recipe
        ShapedRecipe shapedRecipe = new ShapedRecipe(a).shape("!@#", "$%^", "&*(");
        //Set ingredients
        shapedRecipe.setIngredient('!',
                Material.getMaterial(this.getConfig().getString("recipe.creation.top-row.left.item")));
        shapedRecipe.setIngredient('@',
                Material.getMaterial(this.getConfig().getString("recipe.creation.top-row.center.item")));
        shapedRecipe.setIngredient('#',
                Material.getMaterial(this.getConfig().getString("recipe.creation.top-row.right.item")));
        shapedRecipe.setIngredient('$',
                Material.getMaterial(this.getConfig().getString("recipe.creation.center-row.left.item")));
        shapedRecipe.setIngredient('%',
                Material.getMaterial(this.getConfig().getString("recipe.creation.center-row.center.item")));
        shapedRecipe.setIngredient('^',
                Material.getMaterial(this.getConfig().getString("recipe.creation.center-row.right.item")));
        shapedRecipe.setIngredient('&',
                Material.getMaterial(this.getConfig().getString("recipe.creation.bottom-row.left.item")));
        shapedRecipe.setIngredient('*',
                Material.getMaterial(this.getConfig().getString("recipe.creation.bottom-row.center.item")));
        shapedRecipe.setIngredient('(',
                Material.getMaterial(this.getConfig().getString("recipe.creation.bottom-row.right.item")));
        Bukkit.getServer().addRecipe(shapedRecipe);

        //Notify user the plugin is ready
        this.getLogger().info("Invisibility Cloak has been Enabled!");
    }

    public void onDisable() {
        //Remove recipes
        Bukkit.getServer().clearRecipes();
        //Notify user the plugin is disabled
        this.getLogger().info("Invisibility Cloak has been Disabled!");
        //Set instance to null
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

    private void createConfig() {
        try {
            if(!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }

            if(!(new File(this.getDataFolder(), "config.yml")).exists()) {
                this.getLogger().info("Config.yml not found, creating!");
                this.saveDefaultConfig();
                String[] var1 = new String[]{"Right Click to", "put on/take off your", "Invisibility Cloak!"};
                this.getConfig().set("enabled", true);
                this.getConfig().set("cooldown-for-putting-on", 0.0);
                this.getConfig().set("cooldown-for-taking-off", 0.0);
                this.getConfig().set("invisibility-cloak-lore", var1);
                this.getConfig().set("invisibility-cloak-name", "&cInvisibility Cloak");
                this.getConfig().set("invisibility-cloak-material", "LEATHER");
                this.getConfig().set("put-on-messages", new String[]{"&cYou are wearing the Invisibility Cloak...",
                        "&cBe very sneaky"});
                this.getConfig().set("remove-messages", new String[]{"&cYou removed the Invisibility Cloak...",
                        "&cMake sure No one saw you!"});
                this.getConfig().set("effects-while-wearing", new String[]{"INVISIBILITY"});
                this.getConfig().createSection("recipe");
                this.getConfig().set("recipe.creation.top-row.left.item", "LEATHER");
                this.getConfig().set("recipe.creation.top-row.center.item", "LEATHER");
                this.getConfig().set("recipe.creation.top-row.right.item", "LEATHER");
                this.getConfig().set("recipe.creation.center-row.left.item", "LEATHER");
                this.getConfig().set("recipe.creation.center-row.center.item", "EMERALD_ORE");
                this.getConfig().set("recipe.creation.center-row.right.item", "LEATHER");
                this.getConfig().set("recipe.creation.bottom-row.left.item", "LEATHER");
                this.getConfig().set("recipe.creation.bottom-row.center.item", "LEATHER");
                this.getConfig().set("recipe.creation.bottom-row.right.item", "LEATHER");
                this.getConfig().set("duration-forever", true);
                this.getConfig().set("duration", 10);
                this.getConfig().set("show-update-message", true);
                this.saveConfig();
            } else {
                this.getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public static void helpMessage(Player p) {
        p.sendMessage(ChatColor.GOLD + "Do /ic me - to give yourself the Invisibility Cloak.");
        p.sendMessage(ChatColor.GOLD + "Do /ic give <player name> - to give the targeted player the Invisibility Cloak.");
    }

    public void updateMessage(Player p){
        if (shouldUpdate) {
            p.sendMessage(ChatColor.DARK_RED+"[Invisibility Cloak] It is recommended you update to version "+VERSION);
            p.sendMessage(ChatColor.YELLOW+CHANGELOG);
        }
    }

    private String connectToVersion(String server){
        URL uri;
        try {
            uri = new URL(server);
            URLConnection ec = uri.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    ec.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);

            in.close();
            return a.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Compares two version strings.
     *
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     *         The result is a positive integer if str1 is _numerically_ greater than str2.
     *         The result is zero if the strings are _numerically_ equal.
     */
    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    public void checkForUpdates(){
        if (getConfig().getBoolean("show-update-message")) {
            String VersionAndChangelog = connectToVersion(
                    "https://private-8f513b-myspigotpluginupdates.apiary-mock.com/questions").split(",")[3]
                    .replaceAll("\"", ""); //Invisibility Cloak:Version:Changelog
            VERSION = VersionAndChangelog.split(":")[1]
                    .replaceAll(" ", "");//Version
            CHANGELOG = VersionAndChangelog.split(":")[2];//Changelog
            //Set boolean variable
            shouldUpdate = versionCompare(CURRENT_VERSION, VERSION) < 0;
        }
    }
}