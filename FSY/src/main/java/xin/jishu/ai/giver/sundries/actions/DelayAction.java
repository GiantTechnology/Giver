package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Bukkit;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Map;
import java.util.logging.Level;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class DelayAction extends BaseAction {


    public DelayAction(Map<?, ?> source, Map<?, ?> context) {
        super(source, context);
    }

    @Override
    public void run() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Attempt to delay the primary thread.");
        } else {
            try {
                Thread.sleep(
                        this.getArgument("period", Integer.class)
                );
            } catch (Exception wrong) {
                EntryPoint.getInstance()
                        .getLogger()
                        .log(Level.SEVERE, "Oops!", wrong);
            }
        }
    }

}
