package ruiseki.okstorage.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okstorage.Reference;
import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.client.gui.syncHandler.DelegatedStackHandlerSH;
import ruiseki.okstorage.client.gui.widget.updateGroup.UpgradeSlotUpdateGroup;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.client.gui.widget.upgrade.MagnetUpgradeWidget;
import ruiseki.okstorage.common.item.wrapper.MagnetUpgradeWrapper;

public class ItemMagnetUpgrade extends ItemUpgrade<MagnetUpgradeWrapper> {

    public ItemMagnetUpgrade() {
        super("magnet_upgrade");
        setMaxStackSize(1);
        setTextureName(Reference.PREFIX_MOD + "magnet_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.magnet_upgrade"));
    }

    @Override
    public MagnetUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage) {
        return new MagnetUpgradeWrapper(stack, storage);
    }

    @Override
    public void updateWidgetDelegates(MagnetUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        DelegatedStackHandlerSH handler = group.get("common_filter_handler");
        if (handler == null) return;
        handler.setDelegatedStackHandler(wrapper::getFilterItems);
        handler.syncToServer(DelegatedStackHandlerSH.UPDATE_FILTERABLE);
    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, MagnetUpgradeWrapper wrapper, ItemStack stack,
        IStoragePanel<?> panel, String titleKey) {
        return new MagnetUpgradeWidget(slotIndex, wrapper, stack, panel, titleKey);
    }
}
