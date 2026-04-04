package ruiseki.okstorage.client.gui.syncHandler;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.utils.item.PlayerMainInvWrapper;
import com.cleanroommc.modularui.value.sync.SyncHandler;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.common.SortType;
import ruiseki.okstorage.common.block.StorageWrapper;
import ruiseki.okstorage.common.helpers.StorageInventoryHelpers;

public class StorageSH extends SyncHandler {

    public static final int UPDATE_SET_SORT_TYPE = 0;
    public static final int UPDATE_SORT_INV = 1;
    public static final int UPDATE_TRANSFER_TO_STORAGE_INV = 2;
    public static final int UPDATE_TRANSFER_TO_PLAYER_INV = 3;
    public static final int UPDATE_SETTING = 4;
    public static final int DEPLOY_SLEEPING_BAG = 5;

    private final PlayerMainInvWrapper playerInv;
    private final StorageWrapper wrapper;
    private final IStoragePanel<?> panel;

    public StorageSH(PlayerMainInvWrapper playerInv, StorageWrapper wrapper, IStoragePanel<?> panel) {
        this.playerInv = playerInv;
        this.wrapper = wrapper;
        this.panel = panel;
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        switch (id) {
            case UPDATE_SET_SORT_TYPE:
                setSortType(buf);
                break;

            case UPDATE_SORT_INV:
                sortInventory(buf);
                break;

            case UPDATE_TRANSFER_TO_STORAGE_INV:
                transferToStorage(buf);
                break;

            case UPDATE_TRANSFER_TO_PLAYER_INV:
                transferToPlayerInventory(buf);
                break;

            case UPDATE_SETTING:
                updateStorage(buf);
                break;

            default:
                return;
        }
        wrapper.markDirty();
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {}

    public void setSortType(PacketBuffer buf) {
        SortType sortType = NetworkUtils.readEnumValue(buf, SortType.class);
        setSortType(sortType);
    }

    public void setSortType(SortType sortType) {
        wrapper.setSortType(sortType);
    }

    public void sortInventory(PacketBuffer buf) throws IOException {
        for (int i = 0; i < wrapper.getSlots(); i++) {
            wrapper.setStackInSlot(i, buf.readItemStackFromBuffer());
        }
    }

    public void transferToStorage(boolean transferMatched) {
        StorageInventoryHelpers.transferPlayerInventoryToStorage(wrapper, playerInv, transferMatched);
    }

    public void transferToStorage(PacketBuffer buf) {
        boolean transferMatched = buf.readBoolean();
        StorageInventoryHelpers.transferPlayerInventoryToStorage(wrapper, playerInv, transferMatched);
    }

    public void transferToPlayerInventory(boolean transferMatched) {
        StorageInventoryHelpers.transferStorageToPlayerInventory(wrapper, playerInv, transferMatched);
    }

    public void transferToPlayerInventory(PacketBuffer buf) {
        boolean transferMatched = buf.readBoolean();
        StorageInventoryHelpers.transferStorageToPlayerInventory(wrapper, playerInv, transferMatched);
    }

    public void updateStorage(PacketBuffer buf) throws IOException {
        boolean lock = buf.readBoolean();
        String playerUuid = buf.readStringFromBuffer(36);
        boolean tab = buf.readBoolean();
        wrapper.lockStorage = lock;
        wrapper.playerUuid = playerUuid;
        wrapper.keepTab = tab;
    }
}
