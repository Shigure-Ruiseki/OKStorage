package ruiseki.okstorage.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;

import ruiseki.okstorage.common.block.storage.StorageWrapper;

public class StorageUpgradeRecipe extends ShapedOreRecipe {

    private final int storageSlots;
    private final int upgradeSlots;

    public StorageUpgradeRecipe(ItemStack result, int storageSlots, int upgradeSlots, Object... recipe) {
        super(result, recipe);
        this.storageSlots = storageSlots;
        this.upgradeSlots = upgradeSlots;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack result = super.getCraftingResult(inv);
        if (result == null) return null;

        NBTTagCompound storage = new NBTTagCompound();

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.hasTagCompound()
                && stack.getTagCompound()
                    .hasKey(StorageWrapper.STORAGE_NBT)) {

                storage = (NBTTagCompound) stack.getTagCompound()
                    .getCompoundTag(StorageWrapper.STORAGE_NBT)
                    .copy();

                break;
            }
        }

        storage.setInteger(StorageWrapper.STORAGE_SLOTS, storageSlots);
        storage.setInteger(StorageWrapper.UPGRADE_SLOTS, upgradeSlots);

        NBTTagCompound root = new NBTTagCompound();
        root.setTag(StorageWrapper.STORAGE_NBT, storage);

        result.setTagCompound(root);

        return result;
    }
}
