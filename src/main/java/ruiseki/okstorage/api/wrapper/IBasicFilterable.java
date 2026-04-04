package ruiseki.okstorage.api.wrapper;

import net.minecraft.item.ItemStack;

import ruiseki.okcore.helper.ItemStackHelpers;
import ruiseki.okstorage.client.gui.handler.BaseItemStackHandler;

public interface IBasicFilterable {

    String FILTER_ITEMS_TAG = "FilterItems";
    String FILTER_TYPE_TAG = "FilterType";

    BaseItemStackHandler getFilterItems();

    FilterType getFilterType();

    void setFilterType(FilterType type);

    enum FilterType {
        WHITELIST,
        BLACKLIST;
    }

    default boolean checkFilter(ItemStack check) {
        switch (getFilterType()) {
            case WHITELIST:
                for (ItemStack s : getFilterItems().getStacks()) {
                    if (ItemStackHelpers.areItemsEqualIgnoreDurability(s, check)) {
                        return true;
                    }
                }
                return false;
            case BLACKLIST:
                for (ItemStack s : getFilterItems().getStacks()) {
                    if (ItemStackHelpers.areItemsEqualIgnoreDurability(s, check)) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
