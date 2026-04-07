package ruiseki.okstorage.client.gui.slot;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import ruiseki.okstorage.api.IStorageWrapper;

public class ModularStorageSlot extends ModularSlot {

    protected final IStorageWrapper wrapper;

    public ModularStorageSlot(IStorageWrapper wrapper, int index) {
        super(wrapper, index);
        this.wrapper = wrapper;
    }

    public ItemStack getMemoryStack() {
        return wrapper.getMemoryStack(getSlotIndex());
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getMaxStackSize() * wrapper.applyStackLimitModifiers();
    }

    @Override
    public int getSlotStackLimit() {
        return 64 * wrapper.applySlotLimitModifiers();
    }
}
