package ruiseki.okstorage.common.item.jukebox;

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
import ruiseki.okstorage.client.gui.widget.upgrade.AdvancedJukeboxUpgradeWidget;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.common.item.ItemUpgrade;

public class ItemAdvancedJukeboxUpgrade extends ItemUpgrade<AdvancedJukeboxUpgradeWrapper> {

    public ItemAdvancedJukeboxUpgrade() {
        super("advanced_jukebox_upgrade");
        setMaxStackSize(1);
        setTextureName(Reference.PREFIX_MOD + "advanced_jukebox_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.advanced_jukebox_upgrade"));
        list.add(LangHelpers.localize("tooltip.storage.advanced_jukebox_upgrade.1"));
    }

    @Override
    public AdvancedJukeboxUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage,
        Consumer<ItemStack> upgradeConsumer) {
        return new AdvancedJukeboxUpgradeWrapper(stack, storage, upgradeConsumer);
    }

    @Override
    public void updateWidgetDelegates(AdvancedJukeboxUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        DelegatedStackHandlerSH handler = group.get("adv_jukebox_handler");
        if (handler == null) return;
        handler.setDelegatedStackHandler(wrapper::getStorage);
        handler.syncToServer(DelegatedStackHandlerSH.UPDATE_STORAGE);
    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, AdvancedJukeboxUpgradeWrapper wrapper, ItemStack stack,
        IStoragePanel<?> panel, String titleKey) {
        return new AdvancedJukeboxUpgradeWidget(slotIndex, wrapper, stack, panel, titleKey);
    }
}
