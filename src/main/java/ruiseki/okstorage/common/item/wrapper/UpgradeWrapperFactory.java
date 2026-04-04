package ruiseki.okstorage.common.item.wrapper;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IUpgradeWrapper;
import ruiseki.okstorage.api.wrapper.IUpgradeWrapperFactory;
import ruiseki.okstorage.client.gui.widget.updateGroup.UpgradeSlotUpdateGroup;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;

public class UpgradeWrapperFactory {

    @SuppressWarnings("unchecked")
    public static <W extends IUpgradeWrapper> W createWrapper(ItemStack stack, IStorageWrapper storage) {
        if (stack == null || stack.getItem() == null) return null;
        if (!(stack.getItem() instanceof IUpgradeWrapperFactory<?>factory)) return null;
        return (W) factory.createWrapper(stack, storage);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void updateWidgetDelegates(ItemStack stack, IUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        if (stack == null || stack.getItem() == null || wrapper == null) return;
        if (!(stack.getItem() instanceof IUpgradeWrapperFactory factory)) return;
        factory.updateWidgetDelegates(wrapper, group);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ExpandedTabWidget getExpandedTabWidget(ItemStack stack, int slotIndex, IUpgradeWrapper wrapper,
        IStoragePanel<?> panel, String titleKey) {
        if (stack == null || stack.getItem() == null || wrapper == null) return null;
        if (!(stack.getItem() instanceof IUpgradeWrapperFactory factory)) return null;
        return factory.getExpandedTabWidget(slotIndex, wrapper, stack, panel, titleKey);
    }

}
