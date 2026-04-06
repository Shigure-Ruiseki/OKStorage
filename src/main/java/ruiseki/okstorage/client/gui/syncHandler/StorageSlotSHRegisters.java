package ruiseki.okstorage.client.gui.syncHandler;

import ruiseki.okcore.init.IInitListener;
import ruiseki.okstorage.api.upgrade.StorageSlotSHRegistry;

public class StorageSlotSHRegisters implements IInitListener {

    public static final String UPDATE_SET_MEMORY_STACK = "update_set_memory_stack";
    public static final String UPDATE_UNSET_MEMORY_STACK = "update_unset_memory_stack";
    public static final String UPDATE_SET_SLOT_LOCK = "update_set_slot_lock";
    public static final String UPDATE_UNSET_SLOT_LOCK = "update_unset_slot_lock";

    @Override
    public void onInit(Step step) {
        if (step == Step.POSTINIT) {
            StorageSlotSHRegistry.registerServer(
                UPDATE_SET_MEMORY_STACK,
                (slot, buf) -> {
                    slot.wrapper.setMemoryStack(
                        slot.getSlot()
                            .getSlotIndex(),
                        buf.readBoolean());
                });

            StorageSlotSHRegistry.registerServer(
                UPDATE_UNSET_MEMORY_STACK,
                (slot, buf) -> {
                    slot.wrapper.unsetMemoryStack(
                        slot.getSlot()
                            .getSlotIndex());
                });

            StorageSlotSHRegistry.registerServer(
                UPDATE_SET_SLOT_LOCK,
                (slot, buf) -> {
                    slot.wrapper.setSlotLocked(
                        slot.getSlot()
                            .getSlotIndex(),
                        true);
                });

            StorageSlotSHRegistry.registerServer(
                UPDATE_UNSET_SLOT_LOCK,
                (slot, buf) -> {
                    slot.wrapper.setSlotLocked(
                        slot.getSlot()
                            .getSlotIndex(),
                        false);
                });

        }
    }

}
