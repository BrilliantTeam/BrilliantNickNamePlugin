package Rice.Chen.BrilliantNickNamePlugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NicknamePlaceholder extends PlaceholderExpansion {
    private final BrilliantNickNamePlugin plugin;
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("(?:&)?#([0-9a-fA-F]{6})");
    private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("&([0-9a-fk-orx])");

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
        
        if (params.equals("plain")) {
            String nickname = plugin.getNickname(player.getUniqueId());
            if (nickname == null) return player.getName();
            
            String plainNickname = stripColorCodes(nickname);
            return plainNickname;
        }
        
        if (params.equals("vanilla")) {
            String nickname = plugin.getNickname(player.getUniqueId());
            if (nickname == null) return player.getName();
            
            String vanillaNickname = convertToVanillaColorCodes(nickname);
            return vanillaNickname;
        }
        
        return null;
    }
    
    private String stripColorCodes(String text) {
        if (text == null) return "";
        
        String result = text.replaceAll("&[0-9a-fk-or]", "");
        
        result = result.replaceAll("(?:&)?#[0-9a-fA-F]{6}", "");
        
        return result;
    }
    
    private String convertToVanillaColorCodes(String text) {
        if (text == null) return "";
        
        String result = text.replace("&#", "#");
        
        Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(result);
        StringBuffer hexResult = new StringBuffer();
        
        while (hexMatcher.find()) {
            String hexColor = hexMatcher.group(1);
            StringBuilder converted = new StringBuilder("§x");
            for (char c : hexColor.toCharArray()) {
                converted.append("§").append(c);
            }
            hexMatcher.appendReplacement(hexResult, converted.toString());
        }
        hexMatcher.appendTail(hexResult);
        result = hexResult.toString();
        
        Matcher legacyMatcher = LEGACY_COLOR_PATTERN.matcher(result);
        StringBuffer legacyResult = new StringBuffer();
        
        while (legacyMatcher.find()) {
            String colorCode = legacyMatcher.group(1);
            legacyMatcher.appendReplacement(legacyResult, "§" + colorCode);
        }
        legacyMatcher.appendTail(legacyResult);
        result = legacyResult.toString();
        
        return result;
    }
}