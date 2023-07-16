package xin.jishu.ai.giver.sundries.actions;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import xin.jishu.ai.giver.EntryPoint;

import java.util.Map;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
public class SpawnAction extends BaseAction {

    public SpawnAction(Map<?, ?> source, Map<?, ?> context, Player target) {
        super(source, context, target);
    }

    @Override
    public void run() {
        if (Bukkit.isPrimaryThread()) {

            int amount = EntryPoint.getInstance()
                    .getConfig()
                    .getInt("entities.environment.magnification");
            amount *= this.getArgument("amount", Integer.class);


            switch (
                    this.getArgument("type", String.class)
            ) {
                case "entity": {
                    for (
                            int i = 0; i < amount; i++
                    ) {
                        // 生成实体
                        Entity who = this.getTarget()
                                .getWorld()
                                .spawnEntity(
                                        this.getTarget()
                                                .getLocation(),
                                        EntityType.valueOf(
                                                this.getArgument("subtype", String.class)
                                        )
                                );
                        // 修改实体的参数
                        // > 修改名称
                        String name = this.getArgument("name", String.class);

                        if (name != null) {
                            who.setCustomName(
                                    this.replace(name)
                            );
                        }
                        // > 根据难度设置属性
                        double difficulty = EntryPoint.getInstance()
                                .getConfig()
                                .getDouble("entities.environment.difficulty");

                        if (difficulty != 1.0) {
                            if (who instanceof LivingEntity) {
                                LivingEntity target = (LivingEntity) who;

                                for (Attribute attribute : Attribute.values()) {
                                    AttributeInstance instance = target.getAttribute(attribute);

                                    if (instance != null) {
                                        instance.setBaseValue(
                                                instance.getBaseValue() * difficulty
                                        );
                                    }
                                }
                            }
                        }

                    }
                    //
                    break;
                }
                case "particle": {
                    this.getTarget()
                            .getWorld()
                            .spawnParticle(
                                    Particle.valueOf(
                                            this.getArgument("subtype", String.class)
                                    ),
                                    this.getTarget()
                                            .getLocation(),
                                    amount
                            );
                    break;
                }
                default: {
                    break;
                }
            }


        } else {
            Bukkit.getScheduler()
                    .scheduleSyncDelayedTask(
                            EntryPoint.getInstance(),
                            this
                    );
        }
    }

}
