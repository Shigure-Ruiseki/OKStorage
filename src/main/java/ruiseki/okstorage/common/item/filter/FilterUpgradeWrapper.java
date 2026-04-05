package ruiseki.okstorage.common.item.filter;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import ruiseki.okcore.helper.ItemNBTHelpers;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IFilterUpgrade;
import ruiseki.okstorage.common.item.BasicUpgradeWrapper;

public class FilterUpgradeWrapper extends BasicUpgradeWrapper implements IFilterUpgrade {

    public FilterUpgradeWrapper(ItemStack upgrade, IStorageWrapper storage, Consumer<ItemStack> upgradeConsumer) {
        super(upgrade, storage, upgradeConsumer);
    }

    @Override
    public String getSettingLangKey() {
        return "gui.storage.filter_settings";
    }

    @Override
    public FilterWayType getfilterWay() {
        int ordinal = ItemNBTHelpers.getInt(upgrade, FILTER_WAY_TAG, FilterWayType.IN_OUT.ordinal());
        FilterWayType[] types = FilterWayType.values();
        if (ordinal < 0 || ordinal >= types.length) return FilterWayType.IN_OUT;
        return types[ordinal];
    }

    @Override
    public void setFilterWay(FilterWayType filterWay) {
        if (filterWay == null) filterWay = FilterWayType.IN_OUT;
        ItemNBTHelpers.setInt(upgrade, FILTER_WAY_TAG, filterWay.ordinal());
        save();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        if (!isEnabled()) return true;
        return checkFilter(stack) && (getfilterWay() == FilterWayType.IN_OUT || getfilterWay() == FilterWayType.IN);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        if (!isEnabled()) return true;
        return checkFilter(stack) && (getfilterWay() == FilterWayType.IN_OUT || getfilterWay() == FilterWayType.OUT);
    }
}
