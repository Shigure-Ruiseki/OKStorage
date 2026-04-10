package ruiseki.okstorage.client.gui.widget.updateGroup;

import java.util.HashMap;
import java.util.Map;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.widget.IUpgradeSlotGroupFactory;
import ruiseki.okstorage.api.widget.UpgradeSlotGroupRegistry;
import ruiseki.okstorage.common.block.storage.StorageWrapper;

public class UpgradeSlotUpdateGroup {

    public final IStoragePanel<?> panel;
    public final StorageWrapper wrapper;
    public final int slotIndex;
    public final PanelSyncManager syncManager;

    final Map<String, Object> components = new HashMap<>();

    public UpgradeSlotUpdateGroup(IStoragePanel<?> panel, StorageWrapper wrapper, int slotIndex) {
        this.panel = panel;
        this.wrapper = wrapper;
        this.slotIndex = slotIndex;
        this.syncManager = panel.getSyncManager();

        for (IUpgradeSlotGroupFactory factory : UpgradeSlotGroupRegistry.getFactories()) {
            factory.build(this);
        }
    }

    public <T> void put(String key, T value) {
        components.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {

        Object obj = components.get(key);

        if (obj == null) {
            return null;
        }

        return (T) obj;
    }
}
