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

        NBTTagCompound backpackNBT = root.hasKey(StorageWrapper.BACKPACK_NBT)
            ? root.getCompoundTag(StorageWrapper.BACKPACK_NBT)
            : new NBTTagCompound();

        // copy NBT từ input backpack
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

        // set màu vào BackpackNBT
        if (mainColor >= 0) {
            backpackNBT.setInteger(StorageWrapper.MAIN_COLOR, mainColor);
        }

        if (accentColor >= 0) {
            backpackNBT.setInteger(StorageWrapper.ACCENT_COLOR, accentColor);
        }

        root.setTag(StorageWrapper.BACKPACK_NBT, backpackNBT);
        result.setTagCompound(root);

        return result;
    }
}
