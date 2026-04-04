package ruiseki.okstorage.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;

import ruiseki.okstorage.common.block.StorageWrapper;

public class StorageUpgradeRecipe extends ShapedOreRecipe {

    private final int backpackSlots;
    private final int upgradeSlots;

    public StorageUpgradeRecipe(ItemStack result, int backpackSlots, int upgradeSlots, Object... recipe) {
        super(result, recipe);
        this.backpackSlots = backpackSlots;
        this.upgradeSlots = upgradeSlots;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack result = super.getCraftingResult(inv);
        if (result == null) return null;

        NBTTagCompound backpackNBT = new NBTTagCompound();

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.hasTagCompound()
                && stack.getTagCompound()
                    .hasKey(StorageWrapper.BACKPACK_NBT)) {

                backpackNBT = (NBTTagCompound) stack.getTagCompound()
                    .getCompoundTag(StorageWrapper.BACKPACK_NBT)
                    .copy();

                break;
            }
        }

        backpackNBT.setInteger(StorageWrapper.BACKPACK_SLOTS, backpackSlots);
        backpackNBT.setInteger(StorageWrapper.UPGRADE_SLOTS, upgradeSlots);

        NBTTagCompound root = new NBTTagCompound();
        root.setTag(StorageWrapper.BACKPACK_NBT, backpackNBT);

        result.setTagCompound(root);

        return result;
    }
}
