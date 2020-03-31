/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.townylocations;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author admin
 */
public class TownyLocations  extends JavaPlugin {
    private static TownyLocations plugin;
    public static boolean enable_vault;
    private static Permission perms = null;
    private static String townyVersion;
    public static TownyLocations getPlugin(){
        return plugin;
    }
    public static Permission getPermissions() {
        return perms;
    }
    public static String townyVersion(){
        return townyVersion;
    }
    @Override
    public void onEnable() {
        plugin=this;
        townyVersion=com.palmergames.bukkit.towny.Towny.getPlugin().getVersion();
        getServer().getConsoleSender().sendMessage("["+ChatColor.RED+"Towny"+ChatColor.WHITE+"Locations"+ChatColor.GRAY+"] Enabled TownyLocations");
        ConfigManager.saveDefaults();
        ConfigManager.createLang();
        enable_vault=setupPermissions();
        this.getCommand("townylocations").setExecutor(new TownyLocationsCommands());
        this.getCommand("townylocations").setTabCompleter(new TownyLocationsCompleter());
        getServer().getPluginManager().registerEvents(new CommandPreprocess(), this);
    }
    private boolean setupPermissions() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("["+ChatColor.RED+"Towny"+ChatColor.WHITE+"Locations"+ChatColor.GRAY+"] Disabled TownyLocations");
    }
}
