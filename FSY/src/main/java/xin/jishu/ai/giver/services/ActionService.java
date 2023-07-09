package xin.jishu.ai.giver.services;

import org.bukkit.scheduler.BukkitRunnable;
import xin.jishu.ai.giver.EntryPoint;
import xin.jishu.ai.giver.sundries.actions.BaseAction;

import java.util.List;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class ActionService extends BaseService {

    private static final ActionService INSTANCE = new ActionService();

    private ActionService() {
    }

    public void execute(List<? extends BaseAction> actions) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (BaseAction action : actions) {
                    action.run();
                }
            }
        }.runTaskAsynchronously(EntryPoint.getInstance());
    }

    public static ActionService getInstance() {
        return ActionService.INSTANCE;
    }

}
