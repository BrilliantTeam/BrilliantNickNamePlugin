package Rice.Chen.BrilliantNickNamePlugin;

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
        String nickname = plugin.getNickname(event.getPlayer().getUniqueId());
        if (nickname != null) {
            if (BrilliantNickNamePlugin.isFolia()) {
                event.getPlayer().getScheduler().run(plugin, (task) -> 
                    event.getPlayer().displayName(plugin.formatMessage(nickname)), null);
            } else {
                event.getPlayer().setDisplayName(nickname);
            }
        }
    }
}