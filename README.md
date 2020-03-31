# TownyLocations
This simple plugin is for viewing the plots of a specific owner. And if you have the permission can teleport towards the center of those plots.

__Depends on TownyAdvanced
This plugin has been tested with versions TownyAdvanced:
- Towny-0.93.0.0 (1.8-1.12)
- Towny-0.96.0.0 (+1.13)
And it works correctly

__Support Vault

[Spigot page plugin](https://www.spigotmc.org/resources/townylocations.76778/)

## Compilation
You need add settings.yml with github token. [See more](https://github.com/TownyAdvanced/Towny/wiki/TownyAPI#getting-started-with-towny-and-your-ide)

---
## Commands
- /tloc help: Show help message
- /tloc self_plots [page]: Show plots list
- /tloc other_plots <Player> [page]: Show other player plots list
- /tloc tp [Player] <id>: Teleport to self plot or other player adding the plot id
- /tloc reload: Reload config
---
## Permissions
- townylocations.*: Give all permissions
- townylocations.help: See help message of plugin
- townylocations.self_plots: Show self plots list
- townylocations.other_plots: Show other player plots list
- townylocations.teleport_plots: Teleport self plots
- townylocations.other_teleport_plots: Teleport other player plots
- townylocations.reload: Reload configuration
## Config

- enable_custom_commands:
  - Default: true
  - If is true, players can use `self_plots_command` and `other_plots_command` config commands
- self_plots_command:
  - Default: /myplots
  - Is alias for /tloc self_plots
- other_plots_command:
  - Default: /theirsplots
  - Is alias for /tloc other_plots
- enabled_hover:
  - Default: true
  - If is true, into template add hover text
- enabled_teleport:
  - Default: true
  - If is true, enabled, if have permission, teleport when click to text plot information
- amount_for_page:
  - Default: 5
  - Amount plots information for page
- templace_plots:
  - Template for plots information. Have custom placeholders:
    - {number}: Id of plot
    - {town}: Name of the city in which it is located plot
    - {x} {y} {z}: Common Coordinates
