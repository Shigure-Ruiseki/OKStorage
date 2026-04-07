package ruiseki.okstorage.client.gui.syncHandler.value;

import ruiseki.okcore.init.IInitListener;
import ruiseki.okstorage.api.upgrade.DelegatedValueSHRegistry;
import ruiseki.okstorage.api.wrapper.IProgressable;
import ruiseki.okstorage.api.wrapper.ISmeltingUpgrade;
import ruiseki.okstorage.api.wrapper.IUpgradeWrapper;

public class DelegatedValueSHRegisters implements IInitListener {

    public static final String UPDATE_PROGRESS = "update_progress";
    public static final String UPDATE_FUEL = "update_fuel";

    @Override
    public void onInit(Step step) {
        if (step == Step.POSTINIT) {

            DelegatedValueSHRegistry.registerServer(UPDATE_PROGRESS, (value, buf) -> {
                IUpgradeWrapper wrapper = value.getWrapper();
                if (!(value instanceof DelegatedFloatSH handler)) return;
                if (!(wrapper instanceof IProgressable upgrade)) return;
                handler.setDelegatedSupplier(upgrade::getProgress);
            });

            DelegatedValueSHRegistry.registerServer(UPDATE_FUEL, (value, buf) -> {
                IUpgradeWrapper wrapper = value.getWrapper();
                if (!(value instanceof DelegatedFloatSH handler)) return;
                if (!(wrapper instanceof ISmeltingUpgrade upgrade)) return;
                handler.setDelegatedSupplier(upgrade::getBurnProgress);
            });
        }
    }
}
