package ruiseki.okstorage.common.item.magnet;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.joml.Vector3d;

import ruiseki.okcore.datastructure.BlockPos;
import ruiseki.okcore.helper.ItemNBTHelpers;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IMagnetUpgrade;
import ruiseki.okstorage.common.item.pickup.PickupUpgradeWrapper;
import ruiseki.okstorage.config.ModConfig;

public class MagnetUpgradeWrapper extends PickupUpgradeWrapper implements IMagnetUpgrade {

    public MagnetUpgradeWrapper(ItemStack upgrade, IStorageWrapper storage, Consumer<ItemStack> upgradeConsumer) {
        super(upgrade, storage, upgradeConsumer);
    }

    @Override
    public String getSettingLangKey() {
        return "gui.storage.magnet_settings";
    }

    @Override
    public boolean isCollectItem() {
        return ItemNBTHelpers.getBoolean(upgrade, MAG_ITEM_TAG, true);
    }

    @Override
    public void setCollectItem(boolean enabled) {
        ItemNBTHelpers.setBoolean(upgrade, MAG_ITEM_TAG, enabled);
        save();
    }

    @Override
    public boolean isCollectExp() {
        return ItemNBTHelpers.getBoolean(upgrade, MAG_EXP_TAG, true);
    }

    @Override
    public void setCollectExp(boolean enabled) {
        ItemNBTHelpers.setBoolean(upgrade, MAG_EXP_TAG, enabled);
        save();
    }

    @Override
    public boolean canCollectItem(ItemStack stack) {
        return checkFilter(stack);
    }

    @Override
    public boolean tick(World world, BlockPos pos) {
        if (world.getWorldTime() % 2 != 0) return false;

        double centerX = pos.x + 0.5;
        double centerY = pos.y + 0.5;
        double centerZ = pos.z + 0.5;

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
            centerX - ModConfig.magnetRange,
            centerY - ModConfig.magnetRange,
            centerZ - ModConfig.magnetRange,
            centerX + ModConfig.magnetRange,
            centerY + ModConfig.magnetRange,
            centerZ + ModConfig.magnetRange);

        List<Entity> entities = getMagnetEntities(world, aabb);
        if (entities.isEmpty()) return false;

        int pulled = 0;

        for (Entity entity : entities) {
            if (pulled++ > 20) break;

            double dx = centerX;
            double dy = centerY + 0.25;
            double dz = centerZ;

            if (!world.isRemote && entity instanceof EntityItem itemEntity) {

                if (itemEntity.delayBeforeCanPickup > 0) continue;

                ItemStack stack = itemEntity.getEntityItem();
                if (stack == null || !canCollectItem(stack)) continue;

                double distSq = entity.getDistanceSq(dx, dy, dz);

                if (distSq < 2.25) { // ~1.5 block
                    ItemStack remaining = storage.insertItem(stack, false);

                    if (remaining == null || remaining.stackSize <= 0) {
                        entity.setDead();
                    } else {
                        itemEntity.setEntityItemStack(remaining);
                    }

                    continue;
                }
            }

            setEntityMotionFromVector(entity, new Vector3d(dx, dy, dz), 0.45F);
        }

        return true;
    }
}
