package ruiseki.okstorage.common.item.wrapper;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IPickupUpgrade;

public class AdvancedPickupUpgradeWrapper extends AdvancedUpgradeWrapper implements IPickupUpgrade {

    public AdvancedPickupUpgradeWrapper(ItemStack upgrade, IStorageWrapper storage) {
        super(upgrade, storage);
    }

    @Override
    public String getSettingLangKey() {
        return "gui.backpack.advanced_pickup_settings";
    }

    @Override
    public boolean canPickup(ItemStack stack) {
        return checkFilter(stack);
    }
}
