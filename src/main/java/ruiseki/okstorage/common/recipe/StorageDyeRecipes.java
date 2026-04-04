package ruiseki.okstorage.common.recipe;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class StorageDyeRecipes {

    public StorageDyeRecipes() {}

    public void registerDyeRecipes(ItemStack storage, String accentOreName, String mainOreName, int accentColor,
        int mainColor) {

        // Main color
        GameRegistry.addRecipe(
            new StorageDyeRecipe(storage.copy(), mainColor, -1, "   ", " BM", "   ", 'B', storage, 'M', mainOreName));

        // Accent color
        GameRegistry.addRecipe(
            new StorageDyeRecipe(
                storage.copy(),
                -1,
                accentColor,
                "   ",
                " B ",
                " A ",
                'B',
                storage,
                'A',
                accentOreName));

        // Both colors
        GameRegistry.addRecipe(
            new StorageDyeRecipe(
                storage.copy(),
                mainColor,
                accentColor,
                "   ",
                " BM",
                " A ",
                'B',
                storage,
                'A',
                accentOreName,
                'M',
                mainOreName));
    }
}
