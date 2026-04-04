package ruiseki.okstorage.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;

import ruiseki.okstorage.common.block.StorageWrapper;

public class StorageDyeRecipe extends ShapedOreRecipe {

    private final int mainColor;
    private final int accentColor;

    public StorageDyeRecipe(ItemStack result, int mainColor, int accentColor, Object... recipe) {
        super(result, recipe);
        this.mainColor = mainColor;
        this.accentColor = accentColor;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        ItemStack result = super.getCraftingResult(inv);
        if (result == null) return null;

        NBTTagCompound root = result.hasTagCompound() ? (NBTTagCompound) result.getTagCompound()
            .copy() : new NBTTagCompound();

        NBTTagCompound storageNBT = root.hasKey(StorageWrapper.STORAGE_NBT)
            ? root.getCompoundTag(StorageWrapper.STORAGE_NBT)
            : new NBTTagCompound();

        for (int i = 0; i < inv.getSizeInventory(); i++) {

            ItemStack stack = inv.getStackInSlot(i);

            if (stack != null && stack.hasTagCompound()
                && stack.getTagCompound()
                    .hasKey(StorageWrapper.STORAGE_NBT)) {

                storageNBT = (NBTTagCompound) stack.getTagCompound()
                    .getCompoundTag(StorageWrapper.STORAGE_NBT)
                    .copy();
                break;
            }
        }

        if (mainColor >= 0) {
            storageNBT.setInteger(StorageWrapper.MAIN_COLOR, mainColor);
        }

        if (accentColor >= 0) {
            storageNBT.setInteger(StorageWrapper.ACCENT_COLOR, accentColor);
        }

        root.setTag(StorageWrapper.STORAGE_NBT, storageNBT);
        result.setTagCompound(root);

        return result;
    }
}
