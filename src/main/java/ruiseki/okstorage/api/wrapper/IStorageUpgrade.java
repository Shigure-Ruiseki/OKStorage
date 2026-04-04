package ruiseki.okstorage.api.wrapper;

import ruiseki.okstorage.client.gui.handler.BaseItemStackHandler;

public interface IStorageUpgrade {

    String STORAGE_TAG = "Storage";

    BaseItemStackHandler getStorage();
}
