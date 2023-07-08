package xin.jishu.ai.giver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionPlaceholder {

    public String replace(String source) {
        return source;
    }


}
