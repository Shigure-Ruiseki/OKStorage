package ruiseki.okstorage.client.gui.widget;

import java.util.Arrays;
import java.util.List;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.widgets.layout.Row;

import ruiseki.okstorage.client.gui.OKBGuiTextures;
import ruiseki.okstorage.client.gui.syncHandler.StorageSH;
import ruiseki.okstorage.client.gui.widget.upgrade.ExpandedTabWidget;
import ruiseki.okstorage.common.block.StoragePanel;
import ruiseki.okstorage.common.block.StorageSettingPanel;
import ruiseki.okstorage.common.block.StorageWrapper;

public class StorageSettingWidget extends ExpandedTabWidget {

    private final StoragePanel panel;
    private final StorageWrapper wrapper;
    private final StorageSettingPanel settingPanel;
    private final TabWidget parentTabWidget;

    private static final List<CyclicVariantButtonWidget.Variant> KEEP_TAB_VARIANTS = Arrays.asList(
        new CyclicVariantButtonWidget.Variant(IKey.lang("gui.storage.keep_tab"), OKBGuiTextures.KEEP_TAB_ICON),
        new CyclicVariantButtonWidget.Variant(IKey.lang("gui.storage.not_keep_tab"), OKBGuiTextures.NOT_KEEP_TAB_ICON));

    private static final List<CyclicVariantButtonWidget.Variant> LOCK_VARIANTS = Arrays.asList(
        new CyclicVariantButtonWidget.Variant(IKey.lang("gui.storage.lock_storage"), OKBGuiTextures.LOCK_BACKPACK_ICON),
        new CyclicVariantButtonWidget.Variant(
            IKey.lang("gui.storage.unlock_storage"),
            OKBGuiTextures.UNLOCK_BACKPACK_ICON));

    public StorageSettingWidget(StoragePanel panel, StorageSettingPanel settingPanel, TabWidget parentTabWidget) {
        super(2, OKBGuiTextures.BACKPACK_ICON, "gui.storage.storage_settings", 80, TabWidget.ExpandDirection.RIGHT);

        this.panel = panel;
        this.wrapper = panel.wrapper;
        this.settingPanel = settingPanel;
        this.parentTabWidget = parentTabWidget;

        Row buttonRow = (Row) new Row().leftRel(0.5f)
            .height(20)
            .coverChildrenWidth()
            .childPadding(2);

        CyclicVariantButtonWidget tabButton = new CyclicVariantButtonWidget(
            KEEP_TAB_VARIANTS,
            wrapper.keepTab ? 0 : 1,
            (index) -> {
                wrapper.keepTab = index == 0;
                updateWrapper();
            });

        CyclicVariantButtonWidget lockButton = new CyclicVariantButtonWidget(
            LOCK_VARIANTS,
            wrapper.lockBackpack ? 0 : 1,
            (index) -> {
                wrapper.lockBackpack = index == 0;
                updateWrapper();
            });

        buttonRow.top(28)
            .child(tabButton)
            .child(lockButton);

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
        settingPanel.updateTabState(0);
    }

    private void updateWrapper() {
        this.panel.backpackSyncHandler.syncToServer(StorageSH.UPDATE_SETTING, buffer -> {
            buffer.writeBoolean(wrapper.lockBackpack);
            buffer.writeStringToBuffer(
                panel.player.getUniqueID()
                    .toString());
            buffer.writeBoolean(wrapper.keepTab);
        });
    }
}
