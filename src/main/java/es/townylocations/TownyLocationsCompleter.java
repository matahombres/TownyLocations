/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.townylocations;

import com.palmergames.bukkit.towny.object.Resident;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


/**
 *
 * @author admin
 */
public class TownyLocationsCompleter implements TabCompleter {
    private final List<String> MAIN_COMMANDS=Arrays.asList(
        "help",
        "tp",
        "self_plots",
        "other_plots",
        "reload"
    );
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if(args.length==1){
            l=util.filterByStart(MAIN_COMMANDS, args[0]);
        }else{
            switch(args[0].toLowerCase()){
                case "tp":
                    if(args.length==1){
                        l.add("<index>");
                    }
                break;
                case "self_plots":
                    if(args.length==2){
                        l.add("[<page>]");
                    }
                break;
                case "other_plots":
                    if(args.length==2){
                        List<Resident> residents=util.getResidents();
                        List<String> residentsToString=new ArrayList<>();
                        for (Resident res : residents) {
                            residentsToString.add(res.getName());
                        }
                        l=util.filterByStart(residentsToString, args[1]);
                    }else if(args.length==3){
                        l.add("[<page>]");
                    }
                break;
            }
        }
        return l;
    }
    
}
