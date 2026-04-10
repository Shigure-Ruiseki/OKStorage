package ruiseki.okstorage.common.block.storage;

import static ruiseki.okstorage.common.block.storage.StoragePanel.LAYERED_TAB_TEXTURE;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;

import ruiseki.okstorage.client.gui.OKBGuiTextures;
import ruiseki.okstorage.client.gui.widget.MemorySettingWidget;
import ruiseki.okstorage.client.gui.widget.StorageSettingWidget;
import ruiseki.okstorage.client.gui.widget.TabWidget;
import ruiseki.okstorage.client.gui.widget.TabWidget.ExpandDirection;
import ruiseki.okstorage.client.gui.widget.upgrade.SortingSettingWidget;

public class StorageSettingPanel extends ModularPanel {

    private final StoragePanel parent;

    private final TabWidget storageTab;
    private final TabWidget memoryTab;
    private final TabWidget sortTab;

    public StorageSettingPanel(StoragePanel parent) {
        super("storage_settings");
        this.parent = parent;

        size(6, parent.getArea().height).relative(parent)
            .top(0)
            .right(0);

        storageTab = new TabWidget(1, ExpandDirection.RIGHT);
        storageTab.tooltipStatic(
            tooltip -> tooltip.addLine(IKey.lang("gui.storage.storage_settings"))
                .pos(RichTooltip.Pos.NEXT_TO_MOUSE));
        storageTab.setExpandedWidget(new StorageSettingWidget(parent, this, storageTab));
        storageTab.setTabIcon(OKBGuiTextures.STORAGE_ICON);

        memoryTab = new TabWidget(2, ExpandDirection.RIGHT);
        memoryTab.tooltipStatic(
            tooltip -> tooltip.addLine(IKey.lang("gui.storage.memory_settings"))
                .pos(RichTooltip.Pos.NEXT_TO_MOUSE));
        memoryTab.setExpandedWidget(new MemorySettingWidget(parent, this, memoryTab));
        memoryTab.setTabIcon(OKBGuiTextures.BRAIN_ICON);

        sortTab = new TabWidget(3, ExpandDirection.RIGHT);
        sortTab.tooltipStatic(
            tooltip -> tooltip.addLine(IKey.lang("gui.storage.sorting_settings"))
                .pos(RichTooltip.Pos.NEXT_TO_MOUSE));
        sortTab.setExpandedWidget(new SortingSettingWidget(parent, this, sortTab));
        sortTab.setTabIcon(OKBGuiTextures.NO_SORT_ICON);

        child(storageTab).child(memoryTab)
            .child(sortTab);
    }

    public void updateTabState(int openIndex) {
        storageTab.setEnabled(true);
        memoryTab.setEnabled(true);
        sortTab.setEnabled(true);

        switch (openIndex) {
            case 0:
                memoryTab.setShowExpanded(false);
                sortTab.setShowExpanded(false);
                parent.isMemorySettingTabOpened = false;
                parent.isSortingSettingTabOpened = false;
                memoryTab.setEnabled(!storageTab.isShowExpanded());
                break;

            case 1:
                storageTab.setShowExpanded(false);
                sortTab.setShowExpanded(false);
                parent.isSortingSettingTabOpened = false;
                sortTab.setEnabled(!memoryTab.isShowExpanded());
                break;

            case 2:
                storageTab.setShowExpanded(false);
                memoryTab.setShowExpanded(false);
                parent.isMemorySettingTabOpened = false;
                break;
        }
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public void onOpen(ModularScreen screen) {
        super.onOpen(screen);
        parent.isMemorySettingTabOpened = memoryTab.isShowExpanded();
        parent.shouldMemorizeRespectNBT = ((MemorySettingWidget) memoryTab.getExpandedWidget()).isRespectNBT();
        parent.isSortingSettingTabOpened = sortTab.isShowExpanded();
        parent.upgradeSlotGroupWidget.setEnabled(false);
    }

    @Override
    public void onClose() {
        super.onClose();
        parent.isMemorySettingTabOpened = false;
        parent.shouldMemorizeRespectNBT = false;
        parent.isSortingSettingTabOpened = false;
        parent.updateUpgradeWidgets();
        parent.upgradeSlotGroupWidget.setEnabled(true);
    }

    @Override
    public void postDraw(ModularGuiContext context, boolean transformed) {
        super.postDraw(context, transformed);
        LAYERED_TAB_TEXTURE.draw(
            context,
            0,
            0,
            6,
            getArea().height,
            WidgetTheme.getDefault()
                .getTheme());
    }

}
