package ruiseki.okstorage.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import ruiseki.okcore.block.IBlock;
import ruiseki.okstorage.OKStorage;
import ruiseki.okstorage.common.block.barrel.BlockBarrel;
import ruiseki.okstorage.config.ModConfig;

public enum ModBlocks {

    // spotless: off

    BARREL(new BlockBarrel("barrel", ModConfig.basicStorageSlots, ModConfig.basicUpgradeSlots)),
    IRON_BARREL(new BlockBarrel("iron_barrel", ModConfig.ironStorageSlots, ModConfig.ironUpgradeSlots)),
    GOLD_BARREL(new BlockBarrel("gold_barrel", ModConfig.goldStorageSlots, ModConfig.goldUpgradeSlots)),
    DIAMOND_BARREL(new BlockBarrel("diamond_barrel", ModConfig.diamondStorageSlots, ModConfig.diamondUpgradeSlots)),
    OBSIDIAN_BARREL(new BlockBarrel("obsidian_barrel", ModConfig.obsidianStorageSlots, ModConfig.obsidianUpgradeSlots)),

    ;

    // spotless: on

    public static final ModBlocks[] VALUES = values();

    public static void preInit() {
        for (ModBlocks block : VALUES) {
            if (block.block == null) {
                continue;
            }
            try {
                block.block.init();
                OKStorage.okLog(Level.INFO, "Successfully initialized " + block.name());
            } catch (Exception e) {
                OKStorage.okLog(Level.ERROR, "Failed to initialize block: +" + block.name());
            }
        }
    }

    private final IBlock block;

    ModBlocks(IBlock block) {
        this.block = block;
    }

    public Block getBlock() {
        return block.getBlock();
    }

    public Item getItem() {
        return block != null ? Item.getItemFromBlock(getBlock()) : null;
    }

    public ItemStack newItemStack() {
        return newItemStack(1);
    }

    public ItemStack newItemStack(int count) {
        return newItemStack(count, 0);
    }

    public ItemStack newItemStack(int count, int meta) {
        return block != null ? new ItemStack(this.getBlock(), count, meta) : null;
    }
}
