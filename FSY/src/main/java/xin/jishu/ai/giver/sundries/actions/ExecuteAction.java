package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Bukkit;
import xin.jishu.ai.giver.EntryPoint;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class ExecuteAction extends BaseAction {


    public ExecuteAction(Map<?, ?> source, Map<?, ?> context) {
        super(source, context);
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
                    (String) commands.get(luckier)
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
