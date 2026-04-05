package ruiseki.okstorage.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import ruiseki.okcore.item.IItem;
import ruiseki.okstorage.OKStorage;
import ruiseki.okstorage.common.item.ItemUpgrade;
import ruiseki.okstorage.common.item.compacting.ItemAdvancedCompactingUpgrade;
import ruiseki.okstorage.common.item.compacting.ItemCompactingUpgrade;
import ruiseki.okstorage.common.item.crafting.ItemCraftingUpgrade;
import ruiseki.okstorage.common.item.feeding.ItemAdvancedFeedingUpgrade;
import ruiseki.okstorage.common.item.feeding.ItemFeedingUpgrade;
import ruiseki.okstorage.common.item.filter.ItemAdvancedFilterUpgrade;
import ruiseki.okstorage.common.item.filter.ItemFilterUpgrade;
import ruiseki.okstorage.common.item.jukebox.ItemAdvancedJukeboxUpgrade;
import ruiseki.okstorage.common.item.jukebox.ItemJukeboxUpgrade;
import ruiseki.okstorage.common.item.magnet.ItemAdvancedMagnetUpgrade;
import ruiseki.okstorage.common.item.magnet.ItemMagnetUpgrade;
import ruiseki.okstorage.common.item.pickup.ItemAdvancedPickupUpgrade;
import ruiseki.okstorage.common.item.pickup.ItemPickupUpgrade;
import ruiseki.okstorage.common.item.smelter.ItemAutoBlastingUpgrade;
import ruiseki.okstorage.common.item.smelter.ItemAutoSmeltingUpgrade;
import ruiseki.okstorage.common.item.smelter.ItemAutoSmokingUpgrade;
import ruiseki.okstorage.common.item.smelter.ItemBlastingUpgrade;
import ruiseki.okstorage.common.item.smelter.ItemSmeltingUpgrade;
import ruiseki.okstorage.common.item.smelter.ItemSmokingUpgrade;
import ruiseki.okstorage.common.item.stack.ItemStackUpgrade;
import ruiseki.okstorage.common.item.voiding.ItemAdvancedVoidUpgrade;
import ruiseki.okstorage.common.item.voiding.ItemVoidUpgrade;
import ruiseki.okstorage.compat.Mods;

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
    JUKEBOX_UPGRADE(new ItemJukeboxUpgrade()),
    ADVANCED_JUKEBOX_UPGRADE(new ItemAdvancedJukeboxUpgrade()),
    SMELTING_UPGRADE(new ItemSmeltingUpgrade()),
    AUTO_SMELTING_UPGRADE(new ItemAutoSmeltingUpgrade()),
    SMOKING_UPGRADE(new ItemSmokingUpgrade(), Mods.EtFuturum),
    AUTO_SMOKING_UPGRADE(new ItemAutoSmokingUpgrade(), Mods.EtFuturum),
    BLASTING_UPGRADE(new ItemBlastingUpgrade(), Mods.EtFuturum),
    AUTO_BLASTING_UPGRADE(new ItemAutoBlastingUpgrade(), Mods.EtFuturum),

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
    private final Mods requiredMod;

    ModItems(IItem item) {
        this(item, null);
    }

    ModItems(IItem item, Mods requiredMod) {
        this.item = item;
        this.requiredMod = requiredMod;
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
