package Rice.Chen.BrilliantNickNamePlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern LEGACY_COLOR_PATTERN = Pattern.compile("&[0-9a-fk-or]");
    
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("(?:&)?#([0-9a-fA-F]{6})");

    public static int getStrippedLength(String text) {
        String stripped = text;
        
        stripped = stripped.replaceAll("&[0-9a-fk-or]", "");
        
        stripped = stripped.replaceAll("(?:&)?#[0-9a-fA-F]{6}", "");
        
        return stripped.length();
    }

    public static Component translateColors(String text) {
        Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(text);
        StringBuffer hexResult = new StringBuffer();
        
        while (hexMatcher.find()) {
            String hexColor = hexMatcher.group(1);
            StringBuilder converted = new StringBuilder("&x");
            for (char c : hexColor.toCharArray()) {
                converted.append("&").append(c);
            }
            hexMatcher.appendReplacement(hexResult, converted.toString());
        }
        hexMatcher.appendTail(hexResult);
        
        return LegacyComponentSerializer.legacyAmpersand().deserialize(hexResult.toString());
    }

    public static boolean containsValidColorCodes(String text) {
        return LEGACY_COLOR_PATTERN.matcher(text).find() || 
               HEX_COLOR_PATTERN.matcher(text).find();
    }

    public static String getLastColors(String text) {
        StringBuilder result = new StringBuilder();
        String lastHex = null;
        String lastLegacy = null;
        
        Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(text);
        while (hexMatcher.find()) {
            lastHex = hexMatcher.group(0);
        }
        
        Matcher legacyMatcher = LEGACY_COLOR_PATTERN.matcher(text);
        while (legacyMatcher.find()) {
            lastLegacy = legacyMatcher.group(0);
        }
        
        if (lastHex != null) {
            result.append(lastHex);
        }

        if (lastLegacy != null && 
            (lastHex == null || text.lastIndexOf(lastLegacy) > text.lastIndexOf(lastHex))) {
            result.append(lastLegacy);
        }
        
        return result.toString();
    }
}