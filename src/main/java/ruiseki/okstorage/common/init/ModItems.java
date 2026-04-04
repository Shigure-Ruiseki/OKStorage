package ruiseki.okstorage.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import ruiseki.okcore.item.IItem;
import ruiseki.okstorage.OKStorage;
import ruiseki.okstorage.common.item.ItemAdvancedCompactingUpgrade;
import ruiseki.okstorage.common.item.ItemAdvancedFeedingUpgrade;
import ruiseki.okstorage.common.item.ItemAdvancedFilterUpgrade;
import ruiseki.okstorage.common.item.ItemAdvancedMagnetUpgrade;
import ruiseki.okstorage.common.item.ItemAdvancedPickupUpgrade;
import ruiseki.okstorage.common.item.ItemAdvancedVoidUpgrade;
import ruiseki.okstorage.common.item.ItemCompactingUpgrade;
import ruiseki.okstorage.common.item.ItemCraftingUpgrade;
import ruiseki.okstorage.common.item.ItemFeedingUpgrade;
import ruiseki.okstorage.common.item.ItemFilterUpgrade;
import ruiseki.okstorage.common.item.ItemMagnetUpgrade;
import ruiseki.okstorage.common.item.ItemPickupUpgrade;
import ruiseki.okstorage.common.item.ItemStackUpgrade;
import ruiseki.okstorage.common.item.ItemUpgrade;
import ruiseki.okstorage.common.item.ItemVoidUpgrade;

public enum ModItems {

    // spotless: off

    BASE_UPGRADE(new ItemUpgrade<>()),
    STACK_UPGRADE(new ItemStackUpgrade()),
    CRAFTING_UPGRADE(new ItemCraftingUpgrade()),
    MAGNET_UPGRADE(new ItemMagnetUpgrade()),
    ADVANCED_MAGNET_UPGRADE(new ItemAdvancedMagnetUpgrade()),
    FEEDING_UPGRADE(new ItemFeedingUpgrade()),
    ADVANCED_FEEDING_UPGRADE(new ItemAdvancedFeedingUpgrade()),
    PICKUP_UPGRADE(new ItemPickupUpgrade()),
    ADVANCED_PICKUP_UPGRADE(new ItemAdvancedPickupUpgrade()),
    VOID_UPGRADE(new ItemVoidUpgrade()),
    ADVANCED_VOID_UPGRADE(new ItemAdvancedVoidUpgrade()),
    FILTER_UPGRADE(new ItemFilterUpgrade()),
    ADVANCED_FILTER_UPGRADE(new ItemAdvancedFilterUpgrade()),
    COMPACTING_UPGRADE(new ItemCompactingUpgrade()),
    ADVANCED_COMPACTING_UPGRADE(new ItemAdvancedCompactingUpgrade()),

    //
    ;
    // spotless: on

    public static final ModItems[] VALUES = values();

    public static void preInit() {
        for (ModItems item : VALUES) {
            try {
                item.item.init();
                OKStorage.okLog(Level.INFO, "Successfully initialized " + item.name());
            } catch (Exception e) {
                OKStorage.okLog(Level.ERROR, "Failed to initialize item: +" + item.name());
            }
        }
    }

    private final IItem item;

    ModItems(IItem item) {
        this.item = item;
    }

    public Item getItem() {
        return item.getItem();
    }

    public String getName() {
        return item.getItem()
            .getUnlocalizedName()
            .replace("item.", "");
    }

    public ItemStack newItemStack() {
        return newItemStack(1);
    }

    public ItemStack newItemStack(int count) {
        return newItemStack(count, 0);
    }

    public ItemStack newItemStack(int count, int meta) {
        return new ItemStack(this.getItem(), count, meta);
    }

}
