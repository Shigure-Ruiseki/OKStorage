package ruiseki.okstorage.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import ruiseki.okstorage.Reference;
import ruiseki.okstorage.client.gui.container.StorageGuiContainer;
import ruiseki.okstorage.common.init.ModItems;

public class NEIConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.registerGuiOverlay(StorageGuiContainer.class, "crafting", new BackpackPositioner());
        API.registerGuiOverlayHandler(StorageGuiContainer.class, new BackpackOverlay(), "crafting");
        API.addRecipeCatalyst(ModItems.CRAFTING_UPGRADE.newItemStack(), "crafting");
    }

    @Override
    public String getName() {
        return Reference.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Reference.VERSION;
    }
}
