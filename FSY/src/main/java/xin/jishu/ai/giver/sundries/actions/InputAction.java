package xin.jishu.ai.giver.sundries.actions;

import com.alibaba.fastjson2.JSON;
import org.bukkit.entity.Player;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class InputAction extends BaseAction {


    public InputAction(Map<?, ?> source, Map<?, ?> context) {
        super(source, context);
    }

    @Override
    public void run() {
        Collection<? extends Player> players = EntryPoint.getInstance()
                .getServer()
                .getOnlinePlayers();

        if (players.isEmpty()) {
            EntryPoint.getInstance()
                    .getLogger()
                    .log(Level.WARNING, "There are no players in the server, no one will be controlled.");
        } else {
            for (Player player : players) {
                player.sendPluginMessage(
                        EntryPoint.getInstance(),
                        EntryPoint.WAY,
                        JSON.toJSONBytes(this.getSource())
                );
            }
        }
    }

}
