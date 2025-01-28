package Rice.Chen.BrilliantNickNamePlugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NicknamePlaceholder extends PlaceholderExpansion {
    private final BrilliantNickNamePlugin plugin;

    public NicknamePlaceholder(BrilliantNickNamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nickname";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";
        
        if (params.equals("get")) {
            String nickname = plugin.getNickname(player.getUniqueId());
            return nickname != null ? nickname : player.getName();
        }
        
        return null;
    }
}