package Rice.Chen.BrilliantNickNamePlugin;

import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class BrilliantNickNamePlugin extends JavaPlugin {
    private FileConfiguration nicknamesConfig;
    private File nicknamesFile;
    private static boolean isFolia;

    @Override
    public void onEnable() {
        isFolia = isFolia();
        
        loadNicknamesConfig();
        
        getCommand("nickname").setExecutor(new NicknameCommand(this));
        getCommand("nickname").setTabCompleter(new NicknameTabCompleter());
        
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new NicknamePlaceholder(this).register();
        }
    }

    @Override
    public void onDisable() {
        saveNicknamesConfig();
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public void loadNicknamesConfig() {
        if (nicknamesFile == null) {
            nicknamesFile = new File(getDataFolder(), "nicknames.yml");
        }
        if (!nicknamesFile.exists()) {
            nicknamesFile.getParentFile().mkdirs();
            saveResource("nicknames.yml", false);
        }
        nicknamesConfig = YamlConfiguration.loadConfiguration(nicknamesFile);
    }

    public void saveNicknamesConfig() {
        if (nicknamesConfig == null || nicknamesFile == null) return;
        try {
            nicknamesConfig.save(nicknamesFile);
        } catch (IOException e) {
            getLogger().severe("Could not save nicknames.yml!");
        }
    }

    public FileConfiguration getNicknamesConfig() {
        return nicknamesConfig;
    }

    public Component formatMessage(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    public void setNickname(UUID uuid, String nickname) {
        if (nickname == null) {
            nicknamesConfig.set(uuid.toString(), null);
        } else {
            nicknamesConfig.set(uuid.toString(), nickname);
        }
        saveNicknamesConfig();
    }

    public String getNickname(UUID uuid) {
        return nicknamesConfig.getString(uuid.toString());
    }
}