package ruiseki.okstorage.client.gui.handler;

import net.minecraft.item.ItemStack;

import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.upgrade.IUpgradeItem;

public class UpgradeItemStackHandler extends BaseItemStackHandler {

    private final IStorageWrapper storage;

    public UpgradeItemStackHandler(int size, IStorageWrapper storage) {
        super(size);
        this.storage = storage;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack == null || stack.getItem() instanceof IUpgradeItem;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }
}
