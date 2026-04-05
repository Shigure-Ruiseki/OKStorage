package ruiseki.okstorage.common.item.pickup;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IPickupUpgrade;
import ruiseki.okstorage.common.item.BasicUpgradeWrapper;

public class PickupUpgradeWrapper extends BasicUpgradeWrapper implements IPickupUpgrade {

    public PickupUpgradeWrapper(ItemStack upgrade, IStorageWrapper storage, Consumer<ItemStack> upgradeConsumer) {
        super(upgrade, storage, upgradeConsumer);
    }

    @Override
    public String getSettingLangKey() {
        return "gui.storage.pickup_settings";
    }

    @Override
    public boolean canPickup(ItemStack stack) {
        return checkFilter(stack);
    }
}
