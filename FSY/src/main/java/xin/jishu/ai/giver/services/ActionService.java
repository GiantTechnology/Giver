package xin.jishu.ai.giver.services;

import xin.jishu.ai.giver.entities.Action;

import java.util.List;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class ActionService extends BaseService {

    private static final ActionService INSTANCE = new ActionService();

    private ActionService() {
    }

    public void execute(List<Action> actions) {
        System.out.println(actions);
    }

    public static ActionService getInstance() {
        return ActionService.INSTANCE;
    }

}
