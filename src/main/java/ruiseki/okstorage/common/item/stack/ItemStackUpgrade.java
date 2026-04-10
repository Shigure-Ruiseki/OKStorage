package ruiseki.okstorage.common.item.stack;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okstorage.Reference;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.upgrade.IUpgradeItem;
import ruiseki.okstorage.api.upgrade.UpgradeSlotChangeResult;
import ruiseki.okstorage.common.item.ItemUpgrade;
import ruiseki.okstorage.config.ModConfig;

public class ItemStackUpgrade extends ItemUpgrade<StackUpgradeWrapper> {

    @SideOnly(Side.CLIENT)
    protected IIcon tier1, tier2, tier3, tier4, tierOmega, tierStarter;

    public ItemStackUpgrade() {
        super("stack_upgrade");
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 5));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        return switch (meta) {
            case 1 -> super.getUnlocalizedName(stack) + ".gold";
            case 2 -> super.getUnlocalizedName(stack) + ".diamond";
            case 3 -> super.getUnlocalizedName(stack) + ".obsidian";
            case 4 -> super.getUnlocalizedName(stack) + ".omega";
            case 5 -> super.getUnlocalizedName(stack) + ".starter";
            default -> super.getUnlocalizedName(stack) + ".iron";
        };
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return switch (meta) {
            case 1 -> tier2;
            case 2 -> tier3;
            case 3 -> tier4;
            case 4 -> tierOmega;
            case 5 -> tierStarter;
            default -> tier1;
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister reg) {
        tier1 = reg.registerIcon(Reference.PREFIX_MOD + "stack_upgrade_tier_1");
        tier2 = reg.registerIcon(Reference.PREFIX_MOD + "stack_upgrade_tier_2");
        tier3 = reg.registerIcon(Reference.PREFIX_MOD + "stack_upgrade_tier_3");
        tier4 = reg.registerIcon(Reference.PREFIX_MOD + "stack_upgrade_tier_4");
        tierStarter = reg.registerIcon(Reference.PREFIX_MOD + "stack_upgrade_starter_tier");
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.backpack.stack_upgrade", formatMultiplier(multiplier(itemstack))));
    }

    @Override
    public UpgradeSlotChangeResult canAddUpgradeTo(IStorageWrapper wrapper, ItemStack upgradeStack, int targetSlot) {
        int[] conflicts = IUpgradeItem.findConflictSlots(wrapper, targetSlot, ItemStackUpgrade.class);
        if (conflicts.length >= 3) {
            return UpgradeSlotChangeResult.failOnlyXAllowed(
                conflicts,
                3,
                LangHelpers.localize("item.stack_upgrade.name"),
                wrapper.getDisplayName());
        }
        return super.canAddUpgradeTo(wrapper, upgradeStack, targetSlot);
    }

    @Override
    public StackUpgradeWrapper createWrapper(ItemStack stack, IStorageWrapper storage,
        Consumer<ItemStack> upgradeConsumer) {
        return new StackUpgradeWrapper(stack, storage, upgradeConsumer);
    }

    public static int multiplier(ItemStack stack) {
        return switch (stack.getItemDamage()) {
            case 1 -> ModConfig.stackUpgradeTier2Mul;
            case 2 -> ModConfig.stackUpgradeTier3Mul;
            case 3 -> ModConfig.stackUpgradeTier4Mul;
            case 5 -> ModConfig.stackUpgradeStarterMul;
            default -> ModConfig.stackUpgradeTier1Mul;
        };
    }

    public static String formatMultiplier(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
