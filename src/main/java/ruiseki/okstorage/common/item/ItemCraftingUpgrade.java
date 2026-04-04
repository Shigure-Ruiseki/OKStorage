package ruiseki.okstorage.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okstorage.Reference;
import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.client.gui.syncHandler.DelegatedCraftingStackHandlerSH;
import ruiseki.okstorage.client.gui.widget.updateGroup.UpgradeSlotUpdateGroup;
import ruiseki.okstorage.client.gui.widget.upgrade.CraftingUpgradeWidget;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.common.item.wrapper.CraftingUpgradeWrapper;

public class ItemCraftingUpgrade extends ItemUpgrade<CraftingUpgradeWrapper> {

    public ItemCraftingUpgrade() {
        super("crafting_upgrade");
        setMaxStackSize(1);
        setTextureName(Reference.PREFIX_MOD + "crafting_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.crafting_upgrade"));
    }

    @Override
    public CraftingUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage) {
        return new CraftingUpgradeWrapper(stack, storage);
    }

    @Override
    public void updateWidgetDelegates(CraftingUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        DelegatedCraftingStackHandlerSH handler = group.get("crafting_handler");
        if (handler == null) return;
        handler.setDelegatedStackHandler(wrapper::getStorage);
        handler.syncToServer(DelegatedCraftingStackHandlerSH.UPDATE_CRAFTING);
    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, CraftingUpgradeWrapper wrapper, ItemStack stack,
        IStoragePanel<?> panel, String titleKey) {
        return new CraftingUpgradeWidget(slotIndex, wrapper, stack, panel, titleKey);
    }
}
