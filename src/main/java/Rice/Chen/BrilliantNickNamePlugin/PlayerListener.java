package Rice.Chen.BrilliantNickNamePlugin;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;

public class PlayerListener implements Listener {
    private final BrilliantNickNamePlugin plugin;

    public PlayerListener(BrilliantNickNamePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        String nickname = plugin.getNickname(player.getUniqueId());
        
        if (nickname != null) {
            if (BrilliantNickNamePlugin.isFolia()) {
                player.getScheduler().run(plugin, (task) -> {
                    player.displayName(ColorUtils.translateColors(nickname));
                    if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                        PlaceholderAPI.setPlaceholders(player, "%nickname_get%");
                    }
                }, null);
            } else {
                Component displayName = ColorUtils.translateColors(nickname);
                player.displayName(displayName);
                
                if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    PlaceholderAPI.setPlaceholders(player, "%nickname_get%");
                }
            }
        }
        
        if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (BrilliantNickNamePlugin.isFolia()) {
                player.getScheduler().runDelayed(plugin, (task) -> {
                    PlaceholderAPI.setPlaceholders(player, "%nickname_get%");
                }, null, 20L);
            } else {
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                    PlaceholderAPI.setPlaceholders(player, "%nickname_get%");
                }, 20L);
            }
        }
    }
}