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
    protected IIcon tier1, tier2, tier3, tier4;

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
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        switch (meta) {
            case 1:
                return super.getUnlocalizedName(stack) + ".gold";
            case 2:
                return super.getUnlocalizedName(stack) + ".diamond";
            case 3:
                return super.getUnlocalizedName(stack) + ".obsidian";
            default:
                return super.getUnlocalizedName(stack) + ".iron";
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return switch (meta) {
            case 1 -> tier2;
            case 2 -> tier3;
            case 3 -> tier4;
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
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag) {
        list.add(LangHelpers.localize("tooltip.storage.stack_upgrade", multiplier(itemstack)));
    }

    @Override
    public UpgradeSlotChangeResult canAddUpgradeTo(IStorageWrapper wrapper, ItemStack upgradeStack, int targetSlot) {
        int[] conflicts = IUpgradeItem.findConflictSlots(wrapper, targetSlot, ItemStackUpgrade.class);
        if (conflicts.length >= 3) {
            return UpgradeSlotChangeResult.fail(
                "gui.storage.error.add.only_x_upgrades_allowed",
                conflicts,
                3,
                upgradeStack.getDisplayName(),
                wrapper.getDisplayName());
        }
        return UpgradeSlotChangeResult.success();
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
            default -> ModConfig.stackUpgradeTier1Mul;
        };
    }
}
