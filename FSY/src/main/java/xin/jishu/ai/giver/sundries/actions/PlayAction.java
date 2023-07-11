package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class PlayAction extends BaseAction {


    public PlayAction(Map<?, ?> source, Map<?, ?> context) {
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
                    .log(Level.WARNING, "There are no players in the server, no one will be played.");
        } else {
            for (Player player : players) {
                switch (
                        this.getArgument("type", String.class)
                ) {
                    case "sound": {
                        player.playSound(
                                player.getLocation(),
                                Sound.valueOf(
                                        this.getArgument("subtype", String.class)
                                ),
                                this.getArgument("volume", Double.class)
                                        .floatValue(),
                                this.getArgument("pitch", Double.class)
                                        .floatValue()
                        );
                        //
                        break;
                    }
                    case "music": {
                        // 直接将消息转发给客户端
                        new InputAction(
                                this.getSource(), this.getContext()
                        ).run();
                        //
                        break;
                    }
                    case "note": {
                        throw new UnsupportedOperationException();
                    }
                    case "effect": {
                        player.playEffect(
                                player.getLocation(),
                                Effect.valueOf(
                                        this.getArgument("subtype", String.class)
                                ),
                                null
                        );
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }

}
