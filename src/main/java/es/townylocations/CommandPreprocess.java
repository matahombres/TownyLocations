/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.townylocations;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author admin
 */
public class CommandPreprocess implements Listener{
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        if(event.isCancelled()){
            return;
        }
        if(!ConfigManager.configBoolean("enable_custom_commands")){
            return;
        }
        Player player=event.getPlayer();
        String typedMessage=event.getMessage().trim();
        
        String myPlots=ConfigManager.configString("self_plots_command").trim();
        if(!myPlots.startsWith("/")){
            myPlots="/"+myPlots;
        }
        String otherPlots=ConfigManager.configString("self_plots_command").trim();
        if(!otherPlots.startsWith("/")){
            otherPlots="/"+otherPlots;
        }
        if(typedMessage.startsWith(myPlots)){
            player.performCommand("townylocations self_plots"+typedMessage.replace(myPlots, ""));
            event.setCancelled(true);
        }else if(typedMessage.startsWith(otherPlots)){
            player.performCommand("townylocations other_plots"+typedMessage.replace(otherPlots, ""));
            event.setCancelled(true);
        }
    }
}
