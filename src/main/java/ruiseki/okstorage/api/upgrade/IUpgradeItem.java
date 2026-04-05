package ruiseki.okstorage.api.upgrade;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IUpgradeWrapper;
import ruiseki.okstorage.client.gui.widget.updateGroup.UpgradeSlotUpdateGroup;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;

public interface IUpgradeItem<W extends IUpgradeWrapper> {

    W createWrapper(ItemStack stack, IStorageWrapper storage, Consumer<ItemStack> upgradeConsumer);

    void updateWidgetDelegates(W wrapper, UpgradeSlotUpdateGroup group);

    ExpandedTabWidget getExpandedTabWidget(int slotIndex, W wrapper, ItemStack stack, IStoragePanel<?> panel,
        String titleKey);
}
