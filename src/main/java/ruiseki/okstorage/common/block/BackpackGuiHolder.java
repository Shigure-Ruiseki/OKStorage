package ruiseki.okstorage.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.SidedPosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import ruiseki.okstorage.Reference;

public abstract class BackpackGuiHolder {

    protected final BackpackWrapper wrapper;
    protected final int rowSize;

    public BackpackGuiHolder(BackpackWrapper wrapper) {
        this.wrapper = wrapper;

        int size = wrapper.getSlots();
        this.rowSize = size > 81 ? 12 : 9;

    }

    protected BackpackPanel createPanel(PanelSyncManager syncManager, UISettings settings, EntityPlayer player,
        TileEntity tile) {

        int width = 20 + rowSize * ItemSlot.SIZE;

        return new BackpackPanel(player, tile, syncManager, settings, wrapper, width);
    }

    protected void addCommonWidgets(BackpackPanel panel) {
        panel.addSortingButtons();
        panel.addTransferButtons();
        panel.addBackpackInventorySlots();
        panel.addSearchBar();
        panel.addUpgradeSlots();
        panel.addSettingTab();
        panel.addUpgradeTabs();
        panel.addTexts();
    }

    public static final class TileEntityGuiHolder extends BackpackGuiHolder implements IGuiHolder<SidedPosGuiData> {

        public TileEntityGuiHolder(BackpackWrapper wrapper) {
            super(wrapper);
        }

        @Override
        public ModularScreen createScreen(SidedPosGuiData data, ModularPanel mainPanel) {
            return new ModularScreen(Reference.MOD_ID, mainPanel);
        }

        @Override
        public ModularPanel buildUI(SidedPosGuiData data, PanelSyncManager syncManager, UISettings settings) {
            BackpackPanel panel = createPanel(syncManager, settings, data.getPlayer(), data.getTileEntity());
            addCommonWidgets(panel);
            return panel;
        }
    }
}
