package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class PlayAction extends BaseAction {


    public PlayAction(Map<?, ?> source, Map<?, ?> context, Player target) {
        super(source, context, target);
    }

    @Override
    public void run() {
        switch (
                this.getArgument("type", String.class)
        ) {
            case "sound": {
                this.getTarget()
                        .playSound(
                                this.getTarget()
                                        .getLocation(),
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
                        this.getSource(), this.getContext(), this.getTarget()
                ).run();
                //
                break;
            }
            case "note": {
                throw new UnsupportedOperationException();
            }
            case "effect": {
                this.getTarget()
                        .playEffect(
                                this.getTarget()
                                        .getLocation(),
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
