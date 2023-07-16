package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class NotifyAction extends BaseAction {


    public NotifyAction(Map<?, ?> source, Map<?, ?> context, Player target) {
        super(source, context, target);
    }

    @Override
    public void run() {
        this.getTarget()
                .sendTitle(
                        this.replace(
                                this.getArgument("title", String.class)
                        ),
                        this.replace(
                                this.getArgument("subtitle", String.class)
                        ),
                        this.getArgument("fadeIn", Integer.class),
                        this.getArgument("stay", Integer.class),
                        this.getArgument("fadeOut", Integer.class)
                );
    }

}
