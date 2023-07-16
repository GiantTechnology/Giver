package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xin.jishu.ai.giver.EntryPoint;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class ExecuteAction extends BaseAction {


    public ExecuteAction(Map<?, ?> source, Map<?, ?> context, Player target) {
        super(source, context, target);
    }

    @Override
    public void run() {
        if (Bukkit.isPrimaryThread()) {
            List<?> commands = this.getArgument("content", List.class);
            int luckier = ThreadLocalRandom.current()
                    .nextInt(
                            commands.size()
                    );

            Bukkit.dispatchCommand(
                    EntryPoint.getInstance()
                            .getServer()
                            .getConsoleSender(),
                    this.replace(
                            (String) commands.get(luckier)
                    )
            );
        } else {
            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(
                            EntryPoint.getInstance(),
                            this
                    );
        }
    }

}
