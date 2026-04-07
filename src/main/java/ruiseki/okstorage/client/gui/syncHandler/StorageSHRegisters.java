package ruiseki.okstorage.client.gui.syncHandler;

import com.cleanroommc.modularui.network.NetworkUtils;

import ruiseki.okcore.init.IInitListener;
import ruiseki.okstorage.api.upgrade.StorageSHRegistry;
import ruiseki.okstorage.common.SortType;
import ruiseki.okstorage.common.helpers.StorageInventoryHelpers;

public class StorageSHRegisters implements IInitListener {

    public static final String UPDATE_SET_SORT_TYPE = "update_set_sort_type";
    public static final String UPDATE_SORT_INV = "update_sort_inv";
    public static final String UPDATE_TRANSFER_TO_STORAGE_INV = "update_transfer_to_backpack_inv";
    public static final String UPDATE_TRANSFER_TO_PLAYER_INV = "update_transfer_to_player_inv";
    public static final String UPDATE_SETTING = "update_setting";

    @Override
    public void onInit(Step step) {
        if (step == Step.POSTINIT) {

            StorageSHRegistry.registerServer(UPDATE_SET_SORT_TYPE, (handler, buf) -> {
                SortType sortType = NetworkUtils.readEnumValue(buf, SortType.class);
                handler.wrapper.setSortType(sortType);
            });

            StorageSHRegistry.registerServer(UPDATE_SORT_INV, (handler, buf) -> {
                for (int i = 0; i < handler.wrapper.getSlots(); i++) {
                    handler.wrapper.setStackInSlot(i, buf.readItemStackFromBuffer());
                }
            });

            StorageSHRegistry.registerServer(UPDATE_TRANSFER_TO_STORAGE_INV, (handler, buf) -> {
                boolean transferMatched = buf.readBoolean();
                StorageInventoryHelpers
                    .transferPlayerInventoryToStorage(handler.wrapper, handler.playerInv, transferMatched);
            });

            StorageSHRegistry.registerServer(UPDATE_TRANSFER_TO_PLAYER_INV, (handler, buf) -> {
                boolean transferMatched = buf.readBoolean();
                StorageInventoryHelpers
                    .transferStorageToPlayerInventory(handler.wrapper, handler.playerInv, transferMatched);
            });

            StorageSHRegistry.registerServer(UPDATE_SETTING, (handler, buf) -> {
                boolean lock = buf.readBoolean();
                String playerUuid = buf.readStringFromBuffer(36);
                boolean tab = buf.readBoolean();
                handler.wrapper.lockStorage = lock;
                handler.wrapper.playerUuid = playerUuid;
                handler.wrapper.keepTab = tab;
            });
        }
    }
}
