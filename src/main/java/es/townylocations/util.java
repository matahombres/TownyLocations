/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.townylocations;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author admin
 */
public class util {
    
    private static final Permission perm=TownyLocations.getPermissions();
    //Check perm, any command sender
    public static boolean checkperm(CommandSender sender,String permission){
        if(TownyLocations.enable_vault){
            return perm.has(sender, permission);
        }else{
            return sender.hasPermission(permission);
        }
    }
    //Check perm, only with player
    public static boolean checkperm(Player player,String permission){
        if(TownyLocations.enable_vault){
            return perm.has(player, permission);
        }else{
            return player.hasPermission(permission);
        }
    }
    //Check if string is a number
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    //Filter by Start
    public static List<String> filterByStart(List<String> list, String startingWith) {
        if (list == null || startingWith == null) {
                return Collections.emptyList();
        }
        return list.stream().filter(name -> name.toLowerCase().startsWith(startingWith.toLowerCase())).collect(Collectors.toList());
    }
    //Return false if is <=0.94.0.1 version
    public static boolean isOldVersion(String version){
        String number=version.trim().split(" ")[0];
        String numbersVersion[]=number.split("\\.");
        int mainVersion=Integer.parseInt(numbersVersion[1]);
        if(mainVersion==94){
            int subMainVersion=Integer.parseInt(numbersVersion[2]);
            int subVersion=Integer.parseInt(numbersVersion[3]);
            if(subMainVersion==0){
                if(subVersion==0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else if(mainVersion>94){
            return false;
        }else{
            return true;
        }
    }
    //Add support old versions
    public static Resident getResident(String player_name) throws NotRegisteredException{
        Resident res;
        if(isOldVersion(TownyLocations.townyVersion())){
            //Version >=0.94.0.0
            //Add all router because Towny have 2 TownyUniverse
            res=com.palmergames.bukkit.towny.object.TownyUniverse.getDataSource().getResident(player_name);
        }else{
            //Version <=0.94.0.1
            res=TownyAPI.getInstance().getDataSource().getResident(player_name);
        }
        return res;
    }
    //Add support old versions
    public static List<Resident> getResidents(){
        List<Resident> res;
        if(isOldVersion(TownyLocations.townyVersion())){
            //Version >=0.94.0.0
            //Add all router because Towny have 2 TownyUniverse
            res=com.palmergames.bukkit.towny.object.TownyUniverse.getDataSource().getResidents();
        }else{
            //Version <=0.94.0.1
            //Add all router because Towny have 2 TownyUniverse
            res=com.palmergames.bukkit.towny.TownyUniverse.getInstance().getDataSource().getResidents();
        }
        return res;
    }
}
