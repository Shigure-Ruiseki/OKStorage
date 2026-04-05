package ruiseki.okstorage.common.item.compacting;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okstorage.Reference;
import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.client.gui.syncHandler.DelegatedStackHandlerSH;
import ruiseki.okstorage.client.gui.widget.updateGroup.UpgradeSlotUpdateGroup;
import ruiseki.okstorage.client.gui.widget.upgrade.CompactingUpgradeWidget;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.common.item.ItemUpgrade;

public class ItemCompactingUpgrade extends ItemUpgrade<CompactingUpgradeWrapper> {

    public ItemCompactingUpgrade() {
        super("compacting_upgrade");
        setMaxStackSize(1);
        setTextureName(Reference.PREFIX_MOD + "compacting_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.compacting_upgrade"));
    }

    @Override
    public CompactingUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage,
        Consumer<ItemStack> upgradeConsumer) {
        return new CompactingUpgradeWrapper(stack, storage, upgradeConsumer);
    }

    @Override
    public void updateWidgetDelegates(CompactingUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        DelegatedStackHandlerSH handler = group.get("common_filter_handler");
        if (handler == null) return;
        handler.setDelegatedStackHandler(wrapper::getFilterItems);
        handler.syncToServer(DelegatedStackHandlerSH.UPDATE_FILTERABLE);
    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, CompactingUpgradeWrapper wrapper, ItemStack stack,
        IStoragePanel<?> panel, String titleKey) {
        return new CompactingUpgradeWidget(slotIndex, wrapper, stack, panel, titleKey);
    }
}
