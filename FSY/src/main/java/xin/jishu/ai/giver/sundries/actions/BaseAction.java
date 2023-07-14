package xin.jishu.ai.giver.sundries.actions;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import xin.jishu.ai.giver.EntryPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Data
public abstract class BaseAction implements Runnable {
    private final Map<?, ?> source;
    private final Map<?, ?> context;
    private final Map<Object, Object> arguments;

    public BaseAction(Map<?, ?> source, Map<?, ?> context) {
        this.source = source;
        this.context = context;
        //
        this.arguments = new HashMap<>();

        this.arguments.putAll(
                (Map<?, ?>) this.source.get("arguments")
        );
        this.arguments.putAll(this.context);
    }

    public String getName() {
        return (String) this.source.get("action");
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgument(String key, Class<T> type) {
        Object value = this.arguments.get(key);

        if (value == null) {
            return null;
        } else {
            return (T) value;
        }
    }

    public static BaseAction from(Object x, Object y) {
        try {
            Map<?, ?> source = (Map<?, ?>) x;
            Map<?, ?> context = (Map<?, ?>) y;
            // 构建 Action 的 Reference 用于后续实例化
            String name = String.format(
                    "%s.%sAction",
                    BaseAction.class.getPackage()
                            .getName(),
                    StringUtils.capitalize(
                            (String) source.get("action")
                    )
            );
            // 实例化 Action
            return Reflect.onClass(name)
                    .create(source, context)
                    .get();
        } catch (Exception wrong) {
            EntryPoint.getInstance()
                    .getLogger()
                    .log(Level.SEVERE, "Oops!", wrong);

            return null;
        }
    }
}
