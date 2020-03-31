/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.townylocations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author admin
 */
public class ConfigManager {
    
    private static Plugin plugin=TownyLocations.getPlugin();
    private static FileConfiguration config;
    private static File l=null;
    private static FileConfiguration lang;
    public static FileConfiguration getConfig() {
        return config;
    }
    public static void saveDefaults(){
        config=plugin.getConfig();
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
    }
    public static void createLang(){
        l=new File(plugin.getDataFolder(), "lang.yml");
        if(!l.exists()){
            plugin.saveResource("lang.yml", false);
        }
        lang=YamlConfiguration.loadConfiguration(l);
    }
    public static FileConfiguration getLang(){
        return lang;
    }
    //Reload config
    public static void reload(){
        saveDefaults();
        createLang();
    }
    //Lang file get
    public static String langString(String r){
        return defaultreplaceColors(lang.getString(r));
    }
    public static List<String> langList(String r){
        List<String> list2=lang.getStringList(r);
        List<String> list=new ArrayList<>();
        list2.forEach((l) -> {
            list.add(defaultreplaceColors(l));
        });
        return list;
    }
    //Get variables into files config
    public static int configInt(String r){
        return plugin.getConfig().getInt(r);
    }
    public static boolean configBoolean(String r){
        return plugin.getConfig().getBoolean(r);
    }
    public static String configString(String r){
        return plugin.getConfig().getString(r);
    }
    public static List<String> configList(String r){
        return listreturn(r);
    }
    
    
    //Global for the change colors
    public static String defaultreplaceColors(String str){
        String message = ChatColor.translateAlternateColorCodes('&', str);
        return message;
    }
    
    public static List<String> listreturn(String route){
        List<String> list2=plugin.getConfig().getStringList(route);
        List<String> list=new ArrayList<>();
        list2.forEach((l) -> {
            list.add(defaultreplaceColors(l));
        });
        return list;
    }
    
}
