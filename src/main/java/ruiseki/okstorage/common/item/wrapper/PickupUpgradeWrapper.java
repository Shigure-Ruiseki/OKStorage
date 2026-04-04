package ruiseki.okstorage.common.item.wrapper;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IPickupUpgrade;

public class PickupUpgradeWrapper extends BasicUpgradeWrapper implements IPickupUpgrade {

    public PickupUpgradeWrapper(ItemStack upgrade, IStorageWrapper storage) {
        super(upgrade, storage);
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
