package ruiseki.okstorage.client.gui.syncHandler;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.utils.item.PlayerMainInvWrapper;
import com.cleanroommc.modularui.value.sync.SyncHandler;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.upgrade.StorageSHRegistry;
import ruiseki.okstorage.common.block.storage.StorageWrapper;

public class StorageSH extends SyncHandler {

    public final PlayerMainInvWrapper playerInv;
    public final StorageWrapper wrapper;
    public final IStoragePanel<?> panel;

    public StorageSH(PlayerMainInvWrapper playerInv, StorageWrapper wrapper, IStoragePanel<?> panel) {
        this.playerInv = playerInv;
        this.wrapper = wrapper;
        this.panel = panel;
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (!StorageSHRegistry.isServerEmpty()) {
            try {
                StorageSHRegistry.handleServer(this, id, buf);
                wrapper.markDirty();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {
        if (!StorageSHRegistry.isClientEmpty()) {
            try {
                StorageSHRegistry.handleClient(this, id, buf);
                wrapper.markDirty();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int getId(String name) {
        return StorageSHRegistry.getId(name);
    }
}
