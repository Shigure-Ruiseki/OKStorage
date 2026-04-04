package ruiseki.okstorage.client.gui.widget;

import static ruiseki.okstorage.client.gui.OKBGuiTextures.VANILLA_SEARCH_BACKGROUND;

import java.util.ArrayList;
import java.util.List;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import ruiseki.okstorage.client.gui.slot.StorageSlot;
import ruiseki.okstorage.common.block.StoragePanel;
import ruiseki.okstorage.common.search.ItemStackKey;
import ruiseki.okstorage.common.search.ItemStackKeyPool;
import ruiseki.okstorage.common.search.SearchNode;
import ruiseki.okstorage.common.search.SearchParser;

public class SearchBarWidget extends TextFieldWidget {

    protected String prevText = " ";
    private final StoragePanel panel;
    private List<StorageSlot> originalOrder;

    public SearchBarWidget(StoragePanel panel) {
        this.panel = panel;
        background(VANILLA_SEARCH_BACKGROUND);
        value(new StringValue(prevText));
        tooltip().addLine(IKey.lang("gui.search_bar.tool_tip"))
            .pos(RichTooltip.Pos.NEXT_TO_MOUSE);
    }

    @Override
    public void drawBackground(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        IDrawable bg = getCurrentBackground(context.getTheme(), widgetTheme);
        if (bg != null) {
            bg.draw(context, 2, -1, getArea().width - 4, getArea().height + 1, widgetTheme.getTheme());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        String txt = getText();
        if (txt.isEmpty()) prevText = " ";
        if (!txt.equals(prevText)) {
            doSearch(txt);
            prevText = txt;
        }
    }

    @Override
    public void onInit() {
        cacheOriginalOrder();
        doSearch(prevText);
    }

    private void cacheOriginalOrder() {
        Column storageInvCol = panel.storageInvCol;
        if (storageInvCol == null) return;

        originalOrder = new ArrayList<>();
        for (IWidget child : storageInvCol.getChildren()) {
            if (child instanceof StorageSlot slot) {
                originalOrder.add(slot);
            }
        }
    }

    public void research() {
        doSearch(prevText);
    }

    public void doSearch(String search) {
        Column storageInvCol = panel.storageInvCol;
        if (storageInvCol == null) return;

        IWidget parent = storageInvCol.getParent();
        if (!(parent instanceof StorageList storageList)) return;

        int columns = panel.rowSize;
        int slotSize = StorageSlot.SIZE;

        SearchNode compiledSearch = search.isEmpty() ? null : SearchParser.parse(search);

        if (compiledSearch == null) {
            for (int i = 0; i < originalOrder.size(); i++) {
                StorageSlot slot = originalOrder.get(i);
                slot.setFocus(true);

                int x = (i % columns) * slotSize;
                int y = (i / columns) * slotSize;
                slot.left(x)
                    .top(y);
            }
            return;
        }

        List<StorageSlot> matched = new ArrayList<>();
        List<StorageSlot> others = new ArrayList<>();

        for (StorageSlot slot : originalOrder) {
            if (!slot.getSlot()
                .getHasStack()) {
                slot.setFocus(false);
                others.add(slot);
                continue;
            }

            ItemStackKey key = ItemStackKeyPool.get(
                slot.getSlot()
                    .getStack());
            boolean match = compiledSearch.matches(key);
            slot.setFocus(match);

            if (match) matched.add(slot);
            else others.add(slot);
        }

        matched.addAll(others);

        for (int i = 0; i < matched.size(); i++) {
            StorageSlot slot = matched.get(i);
            int x = (i % columns) * slotSize;
            int y = (i / columns) * slotSize;
            slot.left(x)
                .top(y);
            slot.scheduleResize();
        }

        storageList.getScrollData()
            .scrollTo(storageList.getScrollArea(), 0);
    }

}
