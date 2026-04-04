package ruiseki.okstorage.client.gui.syncHandler;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.ItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import ruiseki.okstorage.api.IStoragePanel;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.upgrade.UpgradeSlotSHRegistry;
import ruiseki.okstorage.common.item.wrapper.UpgradeWrapperBase;
import ruiseki.okstorage.common.item.wrapper.UpgradeWrapperFactory;

public class UpgradeSlotSH extends ItemSlotSH {

    public final IStorageWrapper wrapper;
    public final IStoragePanel<?> panel;

    public UpgradeSlotSH(ModularSlot slot, IStorageWrapper wrapper, IStoragePanel<?> panel) {
        super(slot);
        this.wrapper = wrapper;
        this.panel = panel;
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (!UpgradeSlotSHRegistry.isServerEmpty()) {
            try {
                UpgradeSlotSHRegistry.handleServer(this, id, buf);
                wrapper.markDirty();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        super.readOnServer(id, buf);
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {
        if (!UpgradeSlotSHRegistry.isClientEmpty()) {
            try {
                UpgradeSlotSHRegistry.handleClient(this, id, buf);
                // wrapper.syncToServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.readOnClient(id, buf);
    }

    public UpgradeWrapperBase getWrapper() {
        ItemStack stack = getSlot().getStack();
        if (stack == null) return null;
        return UpgradeWrapperFactory.createWrapper(stack, this.wrapper);
    }

    public static int getId(String name) {
        return UpgradeSlotSHRegistry.getId(name);
    }
}
