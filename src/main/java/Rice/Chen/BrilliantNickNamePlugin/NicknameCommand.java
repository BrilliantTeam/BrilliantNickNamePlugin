package Rice.Chen.BrilliantNickNamePlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class NicknameCommand implements CommandExecutor {
    private final BrilliantNickNamePlugin plugin;
    private static final int MAX_NICKNAME_LENGTH = 20;

    public NicknameCommand(BrilliantNickNamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7此指令只能由玩家使用！"));
            return true;
        }

        if (!sender.hasPermission("server.nickname")) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7您沒有權限使用此指令！"));
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelpMessage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                handleSetNickname(player, args);
                break;
            case "remove":
                handleRemoveNickname(player, args);
                break;
            default:
                sendHelpMessage(player);
                break;
        }

        return true;
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7暱稱指令說明："));
        player.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&e[/nickname set] <暱稱> &7- 設定暱稱"));
        player.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&e[/nickname remove] &7- 移除暱稱"));
        player.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&e[/nickname help] &7- 顯示此說明"));
    }

    private void handleSetNickname(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7請輸入想要設定的暱稱！"));
            return;
        }

        final Player target;
        final String nickname = args[1];

        if (args.length > 2 && sender.hasPermission("server.nickname.others")) {
            Player foundTarget = Bukkit.getPlayer(args[2]);
            if (foundTarget == null) {
                sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7找不到該玩家！"));
                return;
            }
            target = foundTarget;
        } else {
            target = sender;
        }

        String strippedNickname = nickname.replaceAll("&[0-9a-fk-or]", "");
        if (strippedNickname.length() > MAX_NICKNAME_LENGTH) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7暱稱最多只能 &c20 &7個字符！（不包含顏色代碼）"));
            return;
        }

        final String displayName = "*" + nickname;
        plugin.setNickname(target.getUniqueId(), displayName);
        
        if (BrilliantNickNamePlugin.isFolia()) {
            target.getScheduler().run(plugin, (task) -> 
                target.displayName(plugin.formatMessage(displayName)), null);
        } else {
            target.setDisplayName(displayName);
        }

        if (target == sender) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7您的暱稱已更新為：&r" + displayName));
        } else {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7已將 &a" + target.getName() + " &7的暱稱更新為：&r" + displayName));
            target.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7您的暱稱已被更新為：&r" + displayName));
        }
    }

    private void handleRemoveNickname(Player sender, String[] args) {
        final Player target;

        if (args.length > 1 && sender.hasPermission("server.nickname.others")) {
            Player foundTarget = Bukkit.getPlayer(args[1]);
            if (foundTarget == null) {
                sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7找不到該玩家！"));
                return;
            }
            target = foundTarget;
        } else {
            target = sender;
        }

        if (plugin.getNickname(target.getUniqueId()) == null) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7" + 
                (target == sender ? "您" : "該玩家") + "目前沒有設定暱稱！"));
            return;
        }

        plugin.setNickname(target.getUniqueId(), null);
        
        if (BrilliantNickNamePlugin.isFolia()) {
            final String playerName = target.getName();
            target.getScheduler().run(plugin, (task) -> 
                target.displayName(Component.text(playerName)), null);
        } else {
            target.setDisplayName(target.getName());
        }

        if (target == sender) {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7您的暱稱已移除。"));
        } else {
            sender.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7已移除 &c" + target.getName() + " &7的暱稱。"));
            target.sendMessage(plugin.formatMessage("&7｜&6系統&7｜&f飯娘：&7您的暱稱已移除。"));
        }
    }
}