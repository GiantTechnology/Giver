package xin.jishu.ai.giver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author sxsx欧克 <wo@jishu.xin>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Spawner {

        private Location at;
        private ActionContext context;
        // Todo: 从 ActionPlaceholder 中提取
        private String entityType;
        private String entityName;

        public void spawn() {
            if (this.at.getWorld() == null) {
                // Todo:
            } else {
                EntityType entityTypeInstance = EntityType.fromName(this.entityType);

                if (entityTypeInstance == null) {
                    // Todo:
                } else {
                    // 生成实体
                    Entity entityInstance = this.at.getWorld()
                            .spawn(this.at, entityTypeInstance.getEntityClass());
                    // 修改实体的属性
                    entityInstance.setCustomName("");

                    if(entityInstance instanceof LivingEntity) {
                        LivingEntity livingEntityInstance = (LivingEntity) entityInstance;

                        for (Attribute attribute : Attribute.values()) {
                            AttributeInstance instance = livingEntityInstance.getAttribute(attribute)
                                    ;
                            instance.setBaseValue(
                                    instance.getBaseValue() * this.context.getDifficulty()
                            );
                        }
                    }
                }
            }
        }


}
