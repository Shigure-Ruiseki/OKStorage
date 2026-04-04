package ruiseki.okstorage.api;

import net.minecraft.inventory.Container;

import ruiseki.okstorage.client.gui.handler.IndexedInventoryCraftingWrapper;
import ruiseki.okstorage.client.gui.slot.IndexedModularCraftingSlot;

public interface IStorageContainer<T extends Container> {

    T getContainer();

    void registerCraftingSlot(int slotIndex, IndexedModularCraftingSlot craftingSlot);

    void registerInventoryCrafting(int slotIndex, IndexedInventoryCraftingWrapper inventoryCrafting);

}
