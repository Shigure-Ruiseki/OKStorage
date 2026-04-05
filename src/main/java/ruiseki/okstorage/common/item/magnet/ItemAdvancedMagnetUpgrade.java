package ruiseki.okstorage.common.item.magnet;

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
import ruiseki.okstorage.client.gui.widget.upgrade.AdvancedMagnetUpgradeWidget;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.common.item.ItemUpgrade;

public class ItemAdvancedMagnetUpgrade extends ItemUpgrade<AdvancedMagnetUpgradeWrapper> {

    public ItemAdvancedMagnetUpgrade() {
        super("advanced_magnet_upgrade");
        setMaxStackSize(1);
        setTextureName(Reference.PREFIX_MOD + "advanced_magnet_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.advanced_magnet_upgrade"));
    }

    @Override
    public AdvancedMagnetUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage,
        Consumer<ItemStack> upgradeConsumer) {
        return new AdvancedMagnetUpgradeWrapper(stack, storage, upgradeConsumer);
    }

    @Override
    public void updateWidgetDelegates(AdvancedMagnetUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        DelegatedStackHandlerSH handler = group.get("adv_common_filter_handler");
        if (handler == null) return;
        handler.setDelegatedStackHandler(wrapper::getFilterItems);
        handler.syncToServer(DelegatedStackHandlerSH.UPDATE_FILTERABLE);

        DelegatedStackHandlerSH oreDictHandler = group.get("ore_dict_handler");
        if (oreDictHandler == null) return;
        oreDictHandler.setDelegatedStackHandler(wrapper::getOreDictItem);
        oreDictHandler.syncToServer(DelegatedStackHandlerSH.UPDATE_ORE_DICT);
    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, AdvancedMagnetUpgradeWrapper wrapper, ItemStack stack,
        IStoragePanel<?> panel, String titleKey) {
        return new AdvancedMagnetUpgradeWidget(slotIndex, wrapper, stack, panel, titleKey);
    }
}
