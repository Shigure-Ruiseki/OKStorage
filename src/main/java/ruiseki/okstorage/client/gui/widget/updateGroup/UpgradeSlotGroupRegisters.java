package ruiseki.okstorage.client.gui.widget.updateGroup;

import ruiseki.okcore.init.IInitListener;
import ruiseki.okstorage.api.widget.UpgradeSlotGroupRegistry;

public class UpgradeSlotGroupRegisters implements IInitListener {

    @Override
    public void onInit(Step step) {
        if (step == Step.POSTINIT) {
            UpgradeSlotGroupRegistry.register(new CommonFilterSlotGroupFactory());
            UpgradeSlotGroupRegistry.register(new AdvancedCommonFilterSlotGroupFactory());
            UpgradeSlotGroupRegistry.register(new FeedingFilterSlotGroupFactory());
            UpgradeSlotGroupRegistry.register(new AdvancedFeedingFilterSlotGroupFactory());
            UpgradeSlotGroupRegistry.register(new CraftingSlotGroup());
        }
    }
}
