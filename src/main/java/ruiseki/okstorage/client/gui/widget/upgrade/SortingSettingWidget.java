package ruiseki.okstorage.client.gui.widget.upgrade;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Row;

import ruiseki.okstorage.client.gui.OKBGuiTextures;
import ruiseki.okstorage.client.gui.syncHandler.StorageSlotSH;
import ruiseki.okstorage.client.gui.syncHandler.StorageSlotSHRegisters;
import ruiseki.okstorage.client.gui.widget.TabWidget;
import ruiseki.okstorage.client.gui.widget.TabWidget.ExpandDirection;
import ruiseki.okstorage.common.block.storage.StoragePanel;
import ruiseki.okstorage.common.block.storage.StorageSettingPanel;
import ruiseki.okstorage.common.block.storage.StorageWrapper;

public class SortingSettingWidget extends ExpandedTabWidget {

    private final StoragePanel panel;
    private final StorageSettingPanel settingPanel;
    private final TabWidget parentTabWidget;

    public SortingSettingWidget(StoragePanel panel, StorageSettingPanel settingPanel, TabWidget parentTabWidget) {
        super(2, OKBGuiTextures.NO_SORT_ICON, "gui.storage.sorting_settings", 80, ExpandDirection.RIGHT);

        this.panel = panel;
        this.settingPanel = settingPanel;
        this.parentTabWidget = parentTabWidget;

        Row buttonRow = (Row) new Row().leftRel(0.5f)
            .height(20)
            .coverChildrenWidth()
            .childPadding(2);

        ButtonWidget<?> lockAllButton = new ButtonWidget<>().size(20)
            .overlay(OKBGuiTextures.ALL_FOUR_SLOT_ICON)
            .onMousePressed(button -> {
                if (button == 0) {
                    StorageWrapper wrapper = panel.wrapper;

                    for (int i = 0; i < wrapper.getSlots(); i++) {
                        wrapper.setSlotLocked(i, true);
                    }

                    for (StorageSlotSH syncHandler : panel.storageSlotSyncHandlers) {
                        syncHandler.syncToServer(StorageSlotSH.getId(StorageSlotSHRegisters.UPDATE_SET_SLOT_LOCK));
                    }

                    return true;
                }
                return false;
            })
            .tooltipStatic(
                t -> t.addLine(IKey.lang("gui.storage.lock_all_sort"))
                    .pos(RichTooltip.Pos.NEXT_TO_MOUSE));

        ButtonWidget<?> unlockAllButton = new ButtonWidget<>().size(20)
            .overlay(OKBGuiTextures.NONE_FOUR_SLOT_ICON)
            .onMousePressed(button -> {
                if (button == 0) {
                    StorageWrapper wrapper = panel.wrapper;

                    for (int i = 0; i < wrapper.getSlots(); i++) {
                        wrapper.setSlotLocked(i, false);
                    }

                    for (StorageSlotSH syncHandler : panel.storageSlotSyncHandlers) {
                        syncHandler.syncToServer(StorageSlotSH.getId(StorageSlotSHRegisters.UPDATE_UNSET_SLOT_LOCK));
                    }

                    return true;
                }
                return false;
            })
            .tooltipStatic(
                t -> t.addLine(IKey.lang("gui.storage.unlock_all_sort"))
                    .pos(RichTooltip.Pos.NEXT_TO_MOUSE));

        buttonRow.top(28)
            .child(lockAllButton)
            .child(unlockAllButton);

        child(buttonRow);
    }

    @Override
    public void onInit() {
        getContext().getUISettings()
            .getRecipeViewerSettings()
            .addExclusionArea(this);
    }

    @Override
    public void updateTabState() {
        parentTabWidget.setShowExpanded(!parentTabWidget.isShowExpanded());
        panel.isSortingSettingTabOpened = parentTabWidget.isShowExpanded();
        settingPanel.updateTabState(2);
    }
}
