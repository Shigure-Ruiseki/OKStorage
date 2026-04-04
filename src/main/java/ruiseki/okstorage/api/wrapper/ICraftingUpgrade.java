package ruiseki.okstorage.api.wrapper;

public interface ICraftingUpgrade extends IStorageUpgrade {

    String CRAFTING_DEST_TAG = "CraftingDest";
    String USE_STORAGE_TAG = "UseStorage";

    CraftingDestination getCraftingDes();

    void setCraftingDes(CraftingDestination type);

    boolean isUseStorage();

    void setUseStorage(boolean used);

    enum CraftingDestination {
        STORAGE,
        INVENTORY;
    }
}
