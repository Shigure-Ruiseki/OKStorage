package ruiseki.okstorage.common.block.barrel;

import com.cleanroommc.modularui.factory.SidedPosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import ruiseki.okcore.persist.nbt.NBTPersist;
import ruiseki.okstorage.api.IOpenState;
import ruiseki.okstorage.common.block.storage.TEStorage;

public class TEBarrel extends TEStorage implements IOpenState {

    @NBTPersist
    private boolean isOpen = false;

    public TEBarrel() {
        super();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void setOpen(boolean open) {
        this.isOpen = open;
        markDirty();
        onSendUpdate();
    }

    @Override
    public ModularPanel buildUI(SidedPosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        syncManager.addCloseListener(player -> { setOpen(false); });
        return super.buildUI(data, syncManager, settings);
    }
}
