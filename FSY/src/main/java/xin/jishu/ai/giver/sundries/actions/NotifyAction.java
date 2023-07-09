package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.entity.Player;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class NotifyAction extends BaseAction {


    public NotifyAction(Map<?, ?> source, Map<?, ?> context) {
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
                    .log(Level.WARNING, "There are no players in the server, no one will be notified.");
        } else {
            players.forEach(player ->
                    player.sendTitle(
                            this.getArgument("title", String.class),
                            this.getArgument("subtitle", String.class),
                            this.getArgument("fadeIn", Integer.class),
                            this.getArgument("stay", Integer.class),
                            this.getArgument("fadeOut", Integer.class)
                    )
            );
        }
    }

}
