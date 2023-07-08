package xin.jishu.ai.giver.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@CommandAlias("giver")
@CommandPermission("giver.primary")
@Description("-")
public class PrimaryCommand extends AbstractCommand {

    private static final PrimaryCommand INSTANCE = new PrimaryCommand();

    private PrimaryCommand() {

    }

    public static PrimaryCommand getInstance() {
        return PrimaryCommand.INSTANCE;
    }

}
