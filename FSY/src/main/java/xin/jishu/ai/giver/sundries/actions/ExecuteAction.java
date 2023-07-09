package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Bukkit;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Map;

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
            Bukkit.dispatchCommand(
                    EntryPoint.getInstance()
                            .getServer()
                            .getConsoleSender(),
                    this.getArgument("content", String.class)
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
