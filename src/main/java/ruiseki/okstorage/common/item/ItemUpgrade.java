package ruiseki.okstorage.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okcore.item.ItemOK;
import ruiseki.okstorage.OKSCreativeTab;
import ruiseki.okstorage.Reference;
import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.upgrade.IUpgradeItem;
import ruiseki.okstorage.api.wrapper.IUpgradeWrapperFactory;
import ruiseki.okstorage.client.gui.widget.updateGroup.UpgradeSlotUpdateGroup;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.common.item.wrapper.UpgradeWrapperBase;

public class ItemUpgrade<T extends UpgradeWrapperBase> extends ItemOK
    implements IUpgradeWrapperFactory<T>, IUpgradeItem {

    public ItemUpgrade(String name) {
        super(name);
        setNoRepair();
        setTextureName(Reference.PREFIX_MOD + "upgrade_base");
        this.setCreativeTab(OKSCreativeTab.INSTANCE);
    }

    public ItemUpgrade() {
        this("upgrade_base");
    }

    public boolean hasTab() {
        return false;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.upgrade_base"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createWrapper(ItemStack stack, IStorageWrapper storage) {
        return (T) new UpgradeWrapperBase(stack, storage);
    }

    @Override
    public void updateWidgetDelegates(T wrapper, UpgradeSlotUpdateGroup group) {

    }

    @Override
    public ExpandedTabWidget getExpandedTabWidget(int slotIndex, T wrapper, ItemStack stack, IStoragePanel<?> panel,
        String titleKey) {
        return null;
    }
}
