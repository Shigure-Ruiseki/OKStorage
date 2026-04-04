package ruiseki.okstorage.common.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import ruiseki.okcore.color.EnumDye;
import ruiseki.okcore.init.IInitListener;
import ruiseki.okstorage.common.init.ModBlocks;
import ruiseki.okstorage.common.init.ModItems;
import ruiseki.okstorage.compat.Mods;

public class ModRecipes implements IInitListener {

    @Override
    public void onInit(Step step) {
        if (step == Step.POSTINIT) {
            blockRecipes();
            itemRecipes();
        }
    }

    public static void blockRecipes() {

        // Leather Backpack
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModBlocks.IRON_BARREL.newItemStack(),
                "SLS",
                "SCS",
                "LLL",
                'S',
                Items.string,
                'L',
                "itemLeather",
                'C',
                Blocks.chest));

        // Dye Recipes
        StorageDyeRecipes recipes = new StorageDyeRecipes();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                String accentOre = EnumDye.DYE_ORE_NAMES[i];
                String mainOre = EnumDye.DYE_ORE_NAMES[j];

                int accentColor = EnumDye.fromIndex(i)
                    .getColor();
                int mainColor = EnumDye.fromIndex(j)
                    .getColor();

                recipes.registerDyeRecipes(
                    ModBlocks.IRON_BARREL.newItemStack(),
                    accentOre,
                    mainOre,
                    accentColor,
                    mainColor);
            }
        }
    }

    public static void itemRecipes() {

        // Upgrade Base
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.BASE_UPGRADE.getItem(),
                "SIS",
                "ILI",
                "SIS",
                'S',
                new ItemStack(Items.string, 1, 0),
                'I',
                "ingotIron",
                'L',
                new ItemStack(Items.leather, 1, 0)));

        // Stack Upgrade Tier 1
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.STACK_UPGRADE.newItemStack(1, 0),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockIron",
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Stack Upgrade Tier 2
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.STACK_UPGRADE.newItemStack(1, 1),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockGold",
                'U',
                ModItems.STACK_UPGRADE.newItemStack(1, 0)));

        // Stack Upgrade Tier 3
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.STACK_UPGRADE.newItemStack(1, 2),
                "BBB",
                "BUB",
                "BBB",
                'B',
                "blockDiamond",
                'U',
                ModItems.STACK_UPGRADE.newItemStack(1, 1)));

        // Stack Upgrade Tier 4
        if (!Mods.EtFuturum.isLoaded()) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModItems.STACK_UPGRADE.newItemStack(1, 3),
                    "BBB",
                    "BUB",
                    "BBB",
                    'B',
                    "itemNetherStar",
                    'U',
                    ModItems.STACK_UPGRADE.newItemStack(1, 2)));
        } else {

            // Stack Upgrade Tier 4
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    ModItems.STACK_UPGRADE.newItemStack(1, 3),
                    "BBB",
                    "BUB",
                    "BBB",
                    'B',
                    "blockNetherite",
                    'U',
                    ModItems.STACK_UPGRADE.newItemStack(1, 2)));
        }

        // Stack Upgrade Tier Omega
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.STACK_UPGRADE.newItemStack(1, 4),
                "BBB",
                "BBB",
                "BBB",
                'B',
                ModItems.STACK_UPGRADE.newItemStack(1, 3)));

        // Crafting Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.CRAFTING_UPGRADE.getItem(),
                " c ",
                "IUI",
                " C ",
                'c',
                new ItemStack(Blocks.crafting_table, 1, 0),
                'C',
                new ItemStack(Blocks.chest, 1, 0),
                'I',
                "ingotIron",
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Magnet Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.MAGNET_UPGRADE.getItem(),
                "EIE",
                "IUI",
                "R L",
                'E',
                "pearlEnder",
                'R',
                "dustRedstone",
                'L',
                "gemLapis",
                'I',
                "ingotIron",
                'U',
                ModItems.PICKUP_UPGRADE.getItem()));

        // Advanced Magnet Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_MAGNET_UPGRADE.getItem(),
                "EIE",
                "IUI",
                "R L",
                'E',
                "pearlEnder",
                'R',
                "dustRedstone",
                'L',
                "gemLapis",
                'I',
                "ingotIron",
                'U',
                ModItems.ADVANCED_PICKUP_UPGRADE.getItem()));

        // Advanced Magnet Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_MAGNET_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                "gemDiamond",
                'R',
                "dustRedstone",
                'G',
                "ingotGold",
                'U',
                ModItems.ADVANCED_PICKUP_UPGRADE.getItem()));

        // Void Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.VOID_UPGRADE.newItemStack(),
                " E ",
                "OUO",
                "ROR",
                'E',
                "pearlEnder",
                'R',
                "dustRedstone",
                'O',
                "blockObsidian",
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Advanced Void Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_VOID_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                "gemDiamond",
                'R',
                "dustRedstone",
                'G',
                "ingotGold",
                'U',
                ModItems.VOID_UPGRADE.getItem()));

        // Feeding Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.FEEDING_UPGRADE.getItem(),
                " C ",
                "AUM",
                " E ",
                'E',
                "pearlEnder",
                'C',
                new ItemStack(Items.golden_carrot, 1, 0),
                'A',
                new ItemStack(Items.golden_apple, 1, 0),
                'M',
                new ItemStack(Items.speckled_melon, 1, 0),
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Advanced Feeding Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_FEEDING_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                "gemDiamond",
                'R',
                "dustRedstone",
                'G',
                "ingotGold",
                'U',
                ModItems.FEEDING_UPGRADE.getItem()));

        // Pickup Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.PICKUP_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                Blocks.sticky_piston,
                'R',
                "dustRedstone",
                'G',
                Items.string,
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Advanced Pickup Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_PICKUP_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                "gemDiamond",
                'R',
                "dustRedstone",
                'G',
                "ingotGold",
                'U',
                ModItems.PICKUP_UPGRADE.getItem()));

        // Filter Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.FILTER_UPGRADE.getItem(),
                "RSR",
                "SUS",
                "RSR",
                'R',
                "dustRedstone",
                'S',
                Items.string,
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Advanced Filter Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_FILTER_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                "gemDiamond",
                'R',
                "dustRedstone",
                'G',
                "ingotGold",
                'U',
                ModItems.FILTER_UPGRADE.getItem()));

        // Compacting Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.COMPACTING_UPGRADE.getItem(),
                "IPI",
                "PUP",
                "RPR",
                'P',
                Blocks.piston,
                'R',
                "dustRedstone",
                'I',
                "ingotIron",
                'U',
                ModItems.BASE_UPGRADE.getItem()));

        // Advanced Compacting Upgrade
        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                ModItems.ADVANCED_COMPACTING_UPGRADE.getItem(),
                " D ",
                "GUG",
                "RRR",
                'D',
                "gemDiamond",
                'R',
                "dustRedstone",
                'G',
                "ingotGold",
                'U',
                ModItems.COMPACTING_UPGRADE.getItem()));
    }

}
