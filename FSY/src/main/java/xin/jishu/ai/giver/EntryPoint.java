package xin.jishu.ai.giver;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xin.jishu.ai.giver.commands.ConfigurationCommand;
import xin.jishu.ai.giver.commands.GiftCommand;
import xin.jishu.ai.giver.commands.PrimaryCommand;
import xin.jishu.ai.giver.listeners.InteractionListener;
import xin.jishu.ai.giver.listeners.MessageListener;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public final class EntryPoint extends JavaPlugin {

    private static EntryPoint instance = null;
    public static final String WAY = "giver:3e742e9509314f";

    @Override
    public void onLoad() {
        // Plugin initialize logic
        EntryPoint.instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.initialize();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initialize() {
        // 保存默认配置文件
        this.saveDefaultConfig();
        // 注册消息管道
        Bukkit.getMessenger()
                .registerIncomingPluginChannel(
                        this, EntryPoint.WAY, MessageListener.getInstance()
                );

        Bukkit.getMessenger()
                .registerOutgoingPluginChannel(this, EntryPoint.WAY);
        // 注册消息监听器
//        this.getServer()
//                .getPluginManager()
//                .registerEvents(MessageListener.getInstance(), this);
        // 注册指令
        BukkitCommandManager operator = new BukkitCommandManager(this);

        operator.enableUnstableAPI("help");

        operator.registerCommand(GiftCommand.getInstance());
        operator.registerCommand(PrimaryCommand.getInstance());
        operator.registerCommand(ConfigurationCommand.getInstance());
        //
        InteractionListener.getInstance()
                .getConnector()
                .connect();
    }

    public static EntryPoint getInstance() {
        return EntryPoint.instance;
    }

}
