package xin.jishu.ai.giver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private Map<?, ?> source;

    public String getName() {
        return (String) source.get("action");
    }

    public Map<?, ?> getArguments() {
        return (Map<?, ?>) source.get("arguments");
    }

    public static Action from(Object source) {
        return new Action((Map<?, ?>) source);
    }
}
