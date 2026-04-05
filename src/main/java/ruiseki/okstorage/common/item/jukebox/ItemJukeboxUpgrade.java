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
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.client.gui.widget.upgrade.JukeboxUpgradeWidget;
import ruiseki.okstorage.common.item.ItemUpgrade;

public class ItemJukeboxUpgrade extends ItemUpgrade<JukeboxUpgradeWrapper> {

    public ItemJukeboxUpgrade() {
        super("jukebox_upgrade");
        setMaxStackSize(1);
        setTextureName(Reference.PREFIX_MOD + "jukebox_upgrade");
    }

    @Override
    public boolean hasTab() {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.jukebox_upgrade"));
    }

    @Override
    public JukeboxUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage,
        Consumer<ItemStack> upgradeConsumer) {
        return new JukeboxUpgradeWrapper(stack, storage, upgradeConsumer);
    }

    @Override
    public void updateWidgetDelegates(JukeboxUpgradeWrapper wrapper, UpgradeSlotUpdateGroup group) {
        DelegatedStackHandlerSH handler = group.get("jukebox_handler");
        if (handler == null) return;
        handler.setDelegatedStackHandler(wrapper::getStorage);
        handler.syncToServer(DelegatedStackHandlerSH.UPDATE_STORAGE);
    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, JukeboxUpgradeWrapper wrapper, ItemStack stack,
        IStoragePanel<?> panel, String titleKey) {
        return new JukeboxUpgradeWidget(slotIndex, wrapper, stack, panel, titleKey);
    }
}
