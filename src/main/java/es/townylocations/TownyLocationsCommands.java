/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.townylocations;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author admin
 */
public class TownyLocationsCommands implements CommandExecutor{
    public String prefix;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        prefix=ConfigManager.defaultreplaceColors(ConfigManager.configString("prefix"));
        //Coger versión: com.palmergames.bukkit.towny.Towny.getPlugin().getVersion();
        //Coger residentes: TownyUniverse.getDataSource.getResidents();
        //
        if(args.length<=0){
            helpCommand(sender);
        }else{
            switch(args[0].toLowerCase()){
                case "help":
                    helpCommand(sender);
                break;
                case "self_plots":
                    if(args.length>1){
                        if(util.isNumeric(args[1])){
                            self_plots(sender,Integer.parseInt(args[1]));
                        }else{
                            sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use")+" "+ConfigManager.langString("correct_use_commands.self_plots"));
                        }
                    }else{
                        //First page
                        self_plots(sender,1);
                    }
                break;
                case "other_plots":
                    if(args.length<2){
                        sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use")+" "+ConfigManager.langString("correct_use_commands.other_plots"));
                        return false;
                    }
                    String name=args[1];
                    if(args.length==2){
                        //First page
                        other_plots(sender,name,1);
                    }else if(args.length>2){
                        if(util.isNumeric(args[2])){
                            other_plots(sender,name,Integer.parseInt(args[2]));
                        }else{
                            sender.sendMessage(prefix+ConfigManager.langString("errors.no_numeric"));
                        }
                    }else{
                        sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use")+" "+ConfigManager.langString("correct_use_commands.other_plots"));
                    }
                break;
                case "tp":
                    if(!(sender instanceof Player)){
                        sender.sendMessage(prefix+ConfigManager.langString("errors.not_console"));
                        return false;
                    }
                    if(args.length<2){
                        sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use")+" "+ConfigManager.langString("correct_use_commands.tp"));
                        return false;
                    }
                    Player player=(Player) sender;
                    String first=args[1];
                    if(util.isNumeric(first)){
                        if(checkPerm(sender,"townylocations.teleport_plots")){
                            teleport(sender,player.getName(),Integer.parseInt(first)+1);
                        }
                    }else{
                        if(args.length<2){
                            sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use"+" "+ConfigManager.langString("correct_use_commands.tp")));
                            return false;
                        }
                        if(util.isNumeric(args[2])){
                            Player s=(Player) sender;
                            if(s.getName().equals(first)){
                                if(checkPerm(sender,"townylocations.teleport_plots")){
                                    teleport(sender,player.getName(),Integer.parseInt(args[2]));
                                }
                            }else{
                                if(checkPerm(sender,"townylocations.other_teleport_plots")){
                                    teleport(sender,args[1],Integer.parseInt(args[2]));
                                }
                            }
                        }else{
                            sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use"+" "+ConfigManager.langString("correct_use_commands.tp")));
                        }
                    }
                break;
                case "reload":
                    if(checkPerm(sender, "townylocations.reload")){
                        ConfigManager.reload();
                        sender.sendMessage(prefix+ConfigManager.langString("commands.reload"));
                    }
                break;
                default:
                    helpCommand(sender);
                break;
            }
        }
        return false;
    }
    public void teleport(CommandSender sender,String player_name,int index){
        try{
            Resident resident=util.getResident(player_name);
            List<TownBlock> plots=resident.getTownBlocks();
            if((plots.size()-1)<index){
                sender.sendMessage(prefix+ConfigManager.langString("errors.not_exist_plot"));
                return;
            }
            TownBlock plot=plots.get(index);
            //Get aprox center chunk (xyz system)
            int x=plot.getX()*16;
            int y=plot.getZ()*16;
            if(x<0){
                x+=8;
            }else{
                x-=8;
            }
            if(y<0){
                y+=8;
            }else{
                y-=8;
            }
            World world=Bukkit.getWorld(plot.getWorld().toString());
            Block block=world.getHighestBlockAt(x, y);
            Location loc=new Location(block.getWorld(),block.getX(),block.getY()+1,block.getZ());
            Player player=(Player) sender;
            player.teleport(loc);
        } catch (NotRegisteredException ex) {
            sender.sendMessage(prefix+ConfigManager.langString("errors.player_not_found").replace("{name}", player_name));
        }
    }
    public void self_plots(CommandSender sender,int pages){
        int mode=0;
        if(checkPerm(sender, "townylocations.self_plots")){
            if(sender instanceof Player){
                Player s=(Player) sender;
                String name=s.getName();
                listBlocks(sender,name,pages,mode);
            }else{
                //Only into console
                sender.sendMessage(prefix+ConfigManager.langString("errors.correct_use")+" "+ConfigManager.langString("correct_use_commands.other_plots"));
            }
        }
    }
    public void other_plots(CommandSender sender,String name,int pages){
        int mode=1;
        if(checkPerm(sender,"townylocations.other_plots")){
            listBlocks(sender,name,pages,mode);
        }
    }
    public void listBlocks(CommandSender sender,String player_name,int pages,int mode){
        try {
            Resident resident=util.getResident(player_name);
            List<TownBlock> plots=resident.getTownBlocks();
            
            if(pages<=0){
                pages=1;
            }
            int index=0;
            boolean enableHover=ConfigManager.configBoolean("enabled_hover");
            boolean enableTp=ConfigManager.configBoolean("enabled_teleport");
            int amountPage=ConfigManager.configInt("amount_for_page");
            int max_pages=(int)plots.size()/amountPage;
            int min_pages=1;
            index=(pages-1)*amountPage;
            if(plots.size()<amountPage){
                index=max_pages;
            }
            List<String> header=ConfigManager.langList("commands.plots_header");
            for (String h : header) {
                sender.sendMessage(h.replace("{player}", player_name));
            }
            int breakLine=1;
            for (int i=index;i<plots.size();i++) {
                TownBlock plot=plots.get(i);
                //Get aprox center chunk (xyz system)
                int x=plot.getX()*16;
                int y=plot.getZ()*16;
                if(x<0){
                    x+=8;
                }else{
                    x-=8;
                }
                if(y<0){
                    y+=8;
                }else{
                    y-=8;
                }
                World world=Bukkit.getWorld(plot.getWorld().toString());
                Block block=world.getHighestBlockAt(x, y);
                Location loc=new Location(block.getWorld(),block.getX(),block.getY(),block.getZ());
                
                String textHover=ConfigManager.defaultreplaceColors(ConfigManager.configString("text_hover"));
                List<String> template=ConfigManager.configList("templace_plots");
                template=travelList(template,plot,loc,i);
                textHover=replacePlaceholders(textHover,plot,loc,i);
                if(sender instanceof Player){
                    Player p=(Player) sender;
                    for (String t : template) {
                        BaseComponent msg=new TextComponent(t);
                        if(enableHover){
                            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(textHover).create()));
                        }
                        if(enableTp){
                            msg.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tloc tp "+player_name+" "+i));
                        }
                        p.spigot().sendMessage(msg);
                    }
                }else{
                    //Console not support TextComponent
                    for (String t : template) {
                        sender.sendMessage(t);
                    }
                }
                if(breakLine>=amountPage){
                    break;
                }
                breakLine++;
            }
            if(sender instanceof Player){
                Player p=(Player) sender;
                TextComponent general=new TextComponent(ConfigManager.langString("commands.left_separation_links_page"));
                TextComponent previus,next;
                //Previus page link
                if(pages>min_pages){
                    previus = new TextComponent(ConfigManager.langString("commands.previus_page"));
                    if(mode==0){
                        previus.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tloc self_plots "+(pages-1)));
                    }else{
                        previus.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tloc other_plots "+player_name+" "+(pages-1)));
                    }
                }else{
                    previus = new TextComponent(ConfigManager.langString("commands.disabled_previus_page"));
                }
                previus.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ConfigManager.langString("commands.previus_hover")).create()));
                //Next page link
                if(pages<max_pages){
                    next = new TextComponent(ConfigManager.langString("commands.next_page"));
                    if(mode==0){
                        next.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tloc self_plots "+(pages+1)));
                    }else{
                        next.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/tloc other_plots "+player_name+" "+(pages+1)));
                    }
                }else{
                    next = new TextComponent(ConfigManager.langString("commands.disabled_next_page"));
                }
                next.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(ConfigManager.langString("commands.next_hover")).create()));
                //Show page links
                sender.sendMessage(""); //Break line required for links page
                general.addExtra(previus);
                general.addExtra(new TextComponent(ConfigManager.langString("commands.center_separation_links_page")));
                general.addExtra(next);
                p.spigot().sendMessage(general);
            }else{
                sender.sendMessage(ConfigManager.langString("commands.previus_page")+ConfigManager.langString("commands.next_page"));
            }
        } catch (NotRegisteredException ex) {
            sender.sendMessage(prefix+ConfigManager.langString("errors.player_not_found").replace("{name}", player_name));
        }
    }
    public String replacePlaceholders(String l,TownBlock plot,Location loc,int number) {
        String ret=l;
        try {
            ret=ret.replace("{number}", String.valueOf(number+1));
            ret=ret.replace("{x}", String.valueOf(loc.getBlockX()));
            ret=ret.replace("{y}", String.valueOf(loc.getBlockY()));
            ret=ret.replace("{z}", String.valueOf(loc.getBlockZ()));
            ret=ret.replace("{town}", plot.getTown().getName());
        } catch (NotRegisteredException ex) {
            Logger.getLogger("Plot not register into town");
            System.err.println(ex);
        }
        return ret;
    }
    public List<String> travelList(List<String> list,TownBlock plot,Location loc,int number){
        List<String> list2=new ArrayList<>();
        list.forEach((l) -> {
            list2.add(replacePlaceholders(l,plot,loc,number));
        });
        return list2;
    }
    public void helpCommand(CommandSender p){
        if(checkPerm(p, "townylocations.help")){
            List<String> help=ConfigManager.langList("commands.help");
            help.forEach((s) -> {
                p.sendMessage(s);
            });
        }
    }
    public boolean checkPerm(CommandSender sender,String s){
        if(util.checkperm(sender, s)||util.checkperm(sender, "townylocations.*")||!(sender instanceof Player)){
            return true;
        }else{
            sender.sendMessage(prefix+ConfigManager.langString("commands.not_permission"));
        }
        return false;
    }
    public boolean checkPerm(CommandSender sender,String s,boolean mensaje){
        if(util.checkperm(sender, s)||util.checkperm(sender, "townylocations.*")||!(sender instanceof Player)){
            return true;
        }else{
            if(mensaje){
                sender.sendMessage(prefix+ConfigManager.langString("commands.not_permission"));
            }
        }
        return false;
    }
    
}
