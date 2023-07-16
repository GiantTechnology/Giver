package xin.jishu.ai.giver.sundries.actions;

import com.alibaba.fastjson2.JSON;
import org.bukkit.entity.Player;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Map;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class InputAction extends BaseAction {


    public InputAction(Map<?, ?> source, Map<?, ?> context, Player target) {
        super(source, context, target);
    }

    @Override
    public void run() {
        this.getTarget()
                .sendPluginMessage(
                        EntryPoint.getInstance(),
                        EntryPoint.WAY,
                        JSON.toJSONBytes(this.getSource())
                );
    }

}
