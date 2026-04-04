package ruiseki.okstorage;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okstorage.common.init.ModBlocks;

public class OKSCreativeTab extends CreativeTabs {

    public static final OKSCreativeTab INSTANCE = new OKSCreativeTab();

    public OKSCreativeTab() {
        super("okStorage");
    }

    @Override
    public Item getTabIconItem() {
        return ModBlocks.IRON_BARREL.getItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return LangHelpers.localize("creativetab." + getTabLabel());
    }
}
