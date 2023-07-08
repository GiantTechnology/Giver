package xin.jishu.ai.giver.utilities;

import org.bukkit.Bukkit;
import xin.jishu.ai.giver.entities.ActionContext;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class NotificationUtilities {

    public static void show(ActionContext context) {
        Bukkit.getOnlinePlayers(
                // ~
        ).forEach(player ->
                player.sendTitle("", "", 10, 20, 20)
        );
    }

}
