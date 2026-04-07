package ruiseki.okstorage.client.gui.syncHandler;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.upgrade.StorageSlotSHRegistry;
import ruiseki.okstorage.api.wrapper.IUpgradeWrapper;

public class StorageSlotSH extends ItemSlotSH {

    public static final int UPDATE_SET_MEMORY_STACK = 6;
    public static final int UPDATE_UNSET_MEMORY_STACK = 7;
    public static final int UPDATE_SET_SLOT_LOCK = 8;
    public static final int UPDATE_UNSET_SLOT_LOCK = 9;

    public final IStorageWrapper wrapper;
    public final IStoragePanel<?> panel;

    public StorageSlotSH(ModularSlot slot, IStorageWrapper wrapper, IStoragePanel<?> panel) {
        super(slot);
        this.wrapper = wrapper;
        this.panel = panel;
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (!StorageSlotSHRegistry.isServerEmpty()) {
            try {
                StorageSlotSHRegistry.handleServer(this, id, buf);
                wrapper.markDirty();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.readOnServer(id, buf);
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {
        if (!StorageSlotSHRegistry.isClientEmpty()) {
            try {
                StorageSlotSHRegistry.handleClient(this, id, buf);
                wrapper.markDirty();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.readOnClient(id, buf);
    }

    public IUpgradeWrapper getWrapper() {
        return this.wrapper.getUpgradeHandler()
            .getWrapperInSlot(getSlot().getSlotIndex());
    }

    public static int getId(String name) {
        return StorageSlotSHRegistry.getId(name);
    }
}
