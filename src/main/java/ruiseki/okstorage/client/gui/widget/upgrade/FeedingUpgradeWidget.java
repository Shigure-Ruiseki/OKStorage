package ruiseki.okstorage.client.gui.widget.upgrade;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.common.item.feeding.FeedingUpgradeWrapper;

public class FeedingUpgradeWidget extends BasicExpandedTabWidget<FeedingUpgradeWrapper> {

    public FeedingUpgradeWidget(int slotIndex, FeedingUpgradeWrapper wrapper, ItemStack stack, IStoragePanel<?> panel,
        String titleKey) {
        super(slotIndex, wrapper, stack, titleKey, "feeding_filter");
    }
}
