package ruiseki.okstorage.common.item.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import ruiseki.okcore.helper.ItemNBTHelpers;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IBasicFilterable;
import ruiseki.okstorage.api.wrapper.ICraftingUpgrade;
import ruiseki.okstorage.client.gui.handler.BaseItemStackHandler;

public class CraftingUpgradeWrapper extends UpgradeWrapperBase implements ICraftingUpgrade {

    protected BaseItemStackHandler handler;

    public CraftingUpgradeWrapper(ItemStack upgrade, IStorageWrapper storage) {
        super(upgrade, storage);
        handler = new BaseItemStackHandler(10) {

            @Override
            protected void onContentsChanged(int slot) {
                NBTTagCompound tag = ItemNBTHelpers.getNBT(upgrade);
                tag.setTag(ICraftingUpgrade.STORAGE_TAG, this.serializeNBT());
            }
        };
        NBTTagCompound handlerTag = ItemNBTHelpers.getCompound(upgrade, STORAGE_TAG, false);
        if (handlerTag != null) handler.deserializeNBT(handlerTag);
    }

    @Override
    public String getSettingLangKey() {
        return "gui.storage.crafting_settings";
    }

    @Override
    public BaseItemStackHandler getStorage() {
        return handler;
    }

    @Override
    public CraftingDestination getCraftingDes() {
        int ordinal = ItemNBTHelpers
            .getInt(upgrade, CRAFTING_DEST_TAG, IBasicFilterable.FilterType.WHITELIST.ordinal());
        CraftingDestination[] types = CraftingDestination.values();
        if (ordinal < 0 || ordinal >= types.length) return CraftingDestination.STORAGE;
        return types[ordinal];
    }

    @Override
    public void setCraftingDes(CraftingDestination type) {
        if (type == null) type = CraftingDestination.STORAGE;
        ItemNBTHelpers.setInt(upgrade, CRAFTING_DEST_TAG, type.ordinal());
        markDirty();
    }

    @Override
    public boolean isUseStorage() {
        return ItemNBTHelpers.getBoolean(upgrade, USE_STORAGE_TAG, false);
    }

    @Override
    public void setUseStorage(boolean used) {
        ItemNBTHelpers.setBoolean(upgrade, USE_STORAGE_TAG, used);
        markDirty();
    }

}
