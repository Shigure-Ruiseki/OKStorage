package ruiseki.okstorage.common.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.ItemHandlerHelper;

import ruiseki.okcore.datastructure.BlockPos;
import ruiseki.okcore.helper.LangHelpers;
import ruiseki.okstorage.api.IStorageWrapper;
import ruiseki.okstorage.api.wrapper.IFilterUpgrade;
import ruiseki.okstorage.api.wrapper.IInventoryModifiable;
import ruiseki.okstorage.api.wrapper.ISlotModifiable;
import ruiseki.okstorage.api.wrapper.ITickable;
import ruiseki.okstorage.api.wrapper.IToggleable;
import ruiseki.okstorage.client.gui.handler.StorageItemStackHandler;
import ruiseki.okstorage.client.gui.handler.UpgradeItemStackHandler;
import ruiseki.okstorage.common.SortType;
import ruiseki.okstorage.common.helpers.StorageItemStackHelpers;
import ruiseki.okstorage.common.item.wrapper.UpgradeWrapperBase;
import ruiseki.okstorage.common.item.wrapper.UpgradeWrapperFactory;

public class StorageWrapper implements IStorageWrapper {

    public final TileEntity tile;

    public final StorageItemStackHandler storageHandler;
    public UpgradeItemStackHandler upgradeHandler;
    public int storageSlots;
    public int upgradeSlots;

    public int mainColor;
    public int accentColor;

    public SortType sortType;

    public boolean lockStorage;
    public String playerUuid;
    public boolean keepTab;

    public String customName;

    public boolean isDirty;

    private Runnable onInventoryHandlerRefresh = () -> {};

    public String uuid;

    public static final String STORAGE_NBT = "StorageNBT";

    public static final String STORAGE_INV = "StorageInv";
    public static final String UPGRADE_INV = "UpgradeInv";
    public static final String STORAGE_SLOTS = "StorageSlots";
    public static final String UPGRADE_SLOTS = "UpgradeSlots";
    public static final String MEMORY_STACK_ITEMS_TAG = "MemoryItems";
    public static final String MEMORY_STACK_RESPECT_NBT_TAG = "MemoryRespectNBT";
    public static final String LOCKED_SLOTS_TAG = "LockedSlots";

    public static final String MAIN_COLOR = "MainColor";
    public static final String ACCENT_COLOR = "AccentColor";

    public static final String SORT_TYPE_TAG = "SortType";

    public static final String UUID_TAG = "UUID";

    public static final String LOCKED_STORAGE_TAG = "LockedStorage";
    public static final String PLAYER_UUID_TAG = "PlayerUUID";

    public static final String KEEP_TAB_TAG = "KeepTab";

    public static final String CUSTOM_NAME_TAG = "CustomName";

    public StorageWrapper() {
        this(null, 120, 7);
    }

    public StorageWrapper(TileEntity tile) {
        this(tile, 120, 7);
    }

    public StorageWrapper(int storageSlots, int upgradeSlots) {
        this(null, storageSlots, upgradeSlots);
    }

    public StorageWrapper(BlockStorage blockStorage, TileEntity tile) {
        this(tile, blockStorage.getSlots(), blockStorage.getUpgradeSlots());
    }

    public StorageWrapper(TileEntity tile, int storageSlots, int upgradeSlots) {
        this.tile = tile;
        this.storageSlots = storageSlots;
        this.upgradeSlots = upgradeSlots;
        this.mainColor = 0xFFCC613A;
        this.accentColor = 0xFF622E1A;
        this.sortType = SortType.BY_NAME;
        this.lockStorage = false;
        this.uuid = UUID.randomUUID()
            .toString();
        this.playerUuid = "";
        this.keepTab = true;

        this.storageHandler = new StorageItemStackHandler(storageSlots, this) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };

        this.upgradeHandler = new UpgradeItemStackHandler(upgradeSlots, this) {

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    @Override
    public UpgradeItemStackHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    @Override
    public String getDisplayName() {
        if (hasCustomInventoryName()) {
            return this.customName;
        }

        if (tile != null && tile.getWorldObj() != null) {
            Block block = tile.getWorldObj()
                .getBlock(tile.xCoord, tile.yCoord, tile.zCoord);
            if (block != null) {
                return LangHelpers.localize(block.getUnlocalizedName() + ".name");
            }
        }

        return LangHelpers.localize("container.inventory");
    }

    @Override
    public int getSlots() {
        return storageHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        ItemStack stack = storageHandler.getStackInSlot(slot);

        Map<Integer, IInventoryModifiable> mods = gatherCapabilityUpgrades(IInventoryModifiable.class);
        for (IInventoryModifiable mod : mods.values()) {
            stack = mod.onGet(slot, stack);
        }

        return stack;
    }

    @Override
    public void setStackInSlot(int slot, @Nullable ItemStack stack) {
        Map<Integer, IInventoryModifiable> mods = gatherCapabilityUpgrades(IInventoryModifiable.class);
        for (IInventoryModifiable mod : mods.values()) {
            stack = mod.onSet(slot, stack);
        }

        storageHandler.setStackInSlot(slot, stack);
    }

    @Override
    public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {

        // Apply slot-level modifications
        Map<Integer, IInventoryModifiable> mods = gatherCapabilityUpgrades(IInventoryModifiable.class);
        for (IInventoryModifiable mod : mods.values()) {
            stack = mod.onInsert(slot, stack, simulate);
            if (stack == null) return null;
        }

        return storageHandler.prioritizedInsertion(slot, stack, simulate);
    }

    @Override
    public @Nullable ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extracted = storageHandler.extractItem(slot, amount, simulate);
        if (extracted == null) return null;

        // Apply IInventoryModifiable wrappers
        Map<Integer, IInventoryModifiable> mods = gatherCapabilityUpgrades(IInventoryModifiable.class);
        for (IInventoryModifiable mod : mods.values()) {
            extracted = mod.onExtract(slot, extracted, simulate);
            if (extracted == null) return null; // cancel extraction
        }

        return extracted;
    }

    @Override
    public @Nullable ItemStack insertItem(@Nullable ItemStack stack, boolean simulate) {
        if (stack == null || stack.stackSize <= 0) return null;

        ItemStack remaining = ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize);

        for (int i = 0; i < storageHandler.getSlots() && remaining != null; i++) {
            remaining = insertItem(i, remaining, simulate);
        }

        return remaining;
    }

    @Override
    public ItemStack extractItem(ItemStack wanted, int amount, boolean simulate) {
        if (wanted == null || amount <= 0) return null;

        int remaining = amount;
        ItemStack result = null;

        for (int i = 0; i < storageHandler.getSlots(); i++) {
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack != null && slotStack.isItemEqual(wanted)) {
                int take = Math.min(slotStack.stackSize, remaining);
                ItemStack extracted = extractItem(i, take, simulate);

                if (result == null) {
                    result = extracted;
                } else if (extracted != null) {
                    result.stackSize += extracted.stackSize;
                }

                remaining -= take;
                if (remaining <= 0) break;
            }
        }

        return result;
    }

    @Override
    public int getSlotLimit(int slot) {
        return storageHandler.getSlotLimit(slot);
    }

    // Setting
    @Override
    public boolean isSlotMemorized(int slotIndex) {
        return storageHandler.isSlotMemorized(slotIndex);
    }

    @Override
    public ItemStack getMemoryStack(int slotIndex) {
        return storageHandler.getMemoryStack(slotIndex);
    }

    @Override
    public void setMemoryStack(int slotIndex, boolean respectNBT) {
        ItemStack currentStack = getStackInSlot(slotIndex);
        if (currentStack == null) return;

        ItemStack copiedStack = currentStack.copy();
        copiedStack.stackSize = 1;
        storageHandler.setMemoryStack(slotIndex, copiedStack);
        storageHandler.setRespectNBT(slotIndex, respectNBT);
    }

    @Override
    public void unsetMemoryStack(int slotIndex) {
        storageHandler.setMemoryStack(slotIndex, null);
        storageHandler.setRespectNBT(slotIndex, false);
    }

    @Override
    public boolean isMemoryStackRespectNBT(int slotIndex) {
        return storageHandler.isRespectNBT(slotIndex);
    }

    @Override
    public void setMemoryStackRespectNBT(int slotIndex, boolean respect) {
        storageHandler.setRespectNBT(slotIndex, respect);
    }

    @Override
    public boolean isSlotLocked(int slotIndex) {
        return storageHandler.isSlotLocked(slotIndex);
    }

    @Override
    public void setSlotLocked(int slotIndex, boolean locked) {
        storageHandler.setSlotLocked(slotIndex, locked);
    }

    @Override
    public int applyStackLimitModifiers(int original, int slot, ItemStack stack) {
        int result = original;

        Map<Integer, ISlotModifiable> gathered = gatherCapabilityUpgrades(ISlotModifiable.class);
        if (gathered.isEmpty()) return result;

        for (ISlotModifiable mod : gathered.values()) {
            result = mod.modifyStackLimit(result, slot, stack);
        }

        if (result != original) {
            return result - original;
        }

        return original;
    }

    @Override
    public int applySlotLimitModifiers(int original, int slot) {
        int result = original;

        Map<Integer, ISlotModifiable> gathered = gatherCapabilityUpgrades(ISlotModifiable.class);
        if (gathered.isEmpty()) return result;

        for (ISlotModifiable mod : gathered.values()) {
            result = mod.modifySlotLimit(result, slot);
        }

        if (result != original) {
            return result - original;
        }

        return original;
    }

    @Override
    public boolean canAddUpgrade(int slot, ItemStack stack) {
        ItemStack upgradeStack = upgradeHandler.getStackInSlot(slot);
        if (upgradeStack == null) return true;

        UpgradeWrapperBase wrapper = UpgradeWrapperFactory.createWrapper(upgradeStack, this);
        if (wrapper == null) return true;
        if (wrapper instanceof IToggleable toggleable && !toggleable.isEnabled()) return true;

        if (wrapper instanceof ISlotModifiable modifiable) {
            return modifiable.canAddUpgrade(slot, stack);
        }
        return true;
    }

    @Override
    public boolean canAddStack(int slot, ItemStack stack) {
        Map<Integer, ISlotModifiable> gathered = gatherCapabilityUpgrades(ISlotModifiable.class);
        if (stack != null && stack.getItem() instanceof BlockStorage.ItemStorage) {

            for (ISlotModifiable mod : gathered.values()) {
                if (mod.canAddStack(slot, stack)) {
                    return true;
                }
            }

            return false;
        }

        for (ISlotModifiable mod : gathered.values()) {
            if (!mod.canAddStack(slot, stack)) return false;
        }

        return true;
    }

    @Override
    public boolean canRemoveUpgrade(int slot) {
        ItemStack upgradeStack = upgradeHandler.getStackInSlot(slot);
        if (upgradeStack == null) return true;

        UpgradeWrapperBase wrapper = UpgradeWrapperFactory.createWrapper(upgradeStack, this);
        if (wrapper == null) return true;
        if (wrapper instanceof IToggleable toggleable && !toggleable.isEnabled()) return true;

        if (wrapper instanceof ISlotModifiable modifiable) {
            return modifiable.canRemoveUpgrade(slot);
        }

        return true;
    }

    @Override
    public boolean canReplaceUpgrade(int slot, ItemStack replacement) {
        ItemStack upgradeStack = upgradeHandler.getStackInSlot(slot);
        if (upgradeStack == null) return true;

        UpgradeWrapperBase wrapper = UpgradeWrapperFactory.createWrapper(upgradeStack, this);
        if (wrapper == null) return true;
        if (wrapper instanceof IToggleable toggleable && !toggleable.isEnabled()) return true;

        if (wrapper instanceof ISlotModifiable modifiable) {
            return modifiable.canReplaceUpgrade(slot, replacement);
        }
        return true;
    }

    @Override
    public boolean tick(World world, BlockPos pos) {
        Map<Integer, ITickable> gathered = gatherCapabilityUpgrades(ITickable.class);
        if (gathered.isEmpty()) return false;

        boolean dirty = false;

        for (ITickable wrapper : gathered.values()) {
            dirty |= wrapper.tick(world, pos);
        }
        return dirty;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack) {
        Map<Integer, IFilterUpgrade> gathered = gatherCapabilityUpgrades(IFilterUpgrade.class);
        if (gathered.isEmpty()) return true;
        for (IFilterUpgrade mod : gathered.values()) {
            if (!mod.canInsert(slot, stack)) return false;
        }
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack) {
        Map<Integer, IFilterUpgrade> gathered = gatherCapabilityUpgrades(IFilterUpgrade.class);
        if (gathered.isEmpty()) return true;
        for (IFilterUpgrade mod : gathered.values()) {
            if (!mod.canExtract(slot, stack)) return false;
        }
        return true;
    }

    public boolean canPlayerAccess(UUID playerUUID) {
        if (!lockStorage) return true;
        if (playerUUID == null || playerUuid == null || playerUuid.isEmpty()) return false;
        return playerUUID.equals(UUID.fromString(playerUuid));
    }

    public boolean hasCustomInventoryName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();

        if (storageHandler.isSizeInconsistent(storageSlots)) {
            storageHandler.resize(storageSlots);
        }
        if (getUpgradeHandler().isSizeInconsistent(upgradeSlots)) {
            getUpgradeHandler().resize(upgradeSlots);
        }

        tag.setInteger(STORAGE_SLOTS, storageSlots);
        tag.setInteger(UPGRADE_SLOTS, upgradeSlots);
        tag.setInteger(MAIN_COLOR, mainColor);
        tag.setInteger(ACCENT_COLOR, accentColor);

        tag.setTag(STORAGE_INV, storageHandler.serializeNBT());
        tag.setTag(UPGRADE_INV, upgradeHandler.serializeNBT());

        NBTTagCompound memoryTag = new NBTTagCompound();
        StorageItemStackHelpers.saveAllSlotsExtended(memoryTag, storageHandler.getMemorizedStacks());
        tag.setTag(MEMORY_STACK_ITEMS_TAG, memoryTag);

        List<Boolean> respectList = storageHandler.getRespectNBTList();
        byte[] respectBytes = new byte[storageSlots];
        for (int i = 0; i < storageSlots; i++) {
            boolean val = i < respectList.size() && respectList.get(i);
            respectBytes[i] = (byte) (val ? 1 : 0);
        }
        tag.setByteArray(MEMORY_STACK_RESPECT_NBT_TAG, respectBytes);

        List<Boolean> locked = storageHandler.getLockedSlotList();
        byte[] lockedBytes = new byte[storageSlots];
        for (int i = 0; i < storageSlots; i++) {
            boolean val = i < locked.size() && locked.get(i);
            lockedBytes[i] = (byte) (val ? 1 : 0);
        }
        tag.setByteArray(LOCKED_SLOTS_TAG, lockedBytes);

        tag.setBoolean(LOCKED_STORAGE_TAG, lockStorage);

        tag.setBoolean(KEEP_TAB_TAG, keepTab);

        tag.setString(UUID_TAG, uuid);

        if (lockStorage && playerUuid != null) {
            tag.setString(PLAYER_UUID_TAG, playerUuid);
        }

        if (hasCustomInventoryName() && this.customName != null) {
            tag.setString(CUSTOM_NAME_TAG, this.customName);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        if (tag == null) return;
        if (tag.hasKey(STORAGE_SLOTS, 3)) {
            this.storageSlots = tag.getInteger(STORAGE_SLOTS);
        }
        if (tag.hasKey(UPGRADE_SLOTS, 3)) {
            this.upgradeSlots = tag.getInteger(UPGRADE_SLOTS);
        }

        if (tag.hasKey(MAIN_COLOR, 3)) this.mainColor = tag.getInteger(MAIN_COLOR);
        if (tag.hasKey(ACCENT_COLOR, 3)) this.accentColor = tag.getInteger(ACCENT_COLOR);

        if (tag.hasKey(STORAGE_INV, 10)) {
            storageHandler.deserializeNBT(tag.getCompoundTag(STORAGE_INV));

            if (storageHandler.isSizeInconsistent(this.storageSlots)) {
                storageHandler.resize(this.storageSlots);
            }

            StorageItemStackHelpers.loadAllItemsExtended(tag.getCompoundTag(STORAGE_INV), storageHandler.getStacks());
        }

        if (tag.hasKey(MEMORY_STACK_ITEMS_TAG, 10)) {
            StorageItemStackHelpers
                .loadAllItemsExtended(tag.getCompoundTag(MEMORY_STACK_ITEMS_TAG), storageHandler.getMemorizedStacks());
        }

        if (tag.hasKey(MEMORY_STACK_RESPECT_NBT_TAG, 7)) {
            byte[] respectArr = tag.getByteArray(MEMORY_STACK_RESPECT_NBT_TAG);
            for (int i = 0; i < respectArr.length && i < this.storageSlots; i++) {
                setMemoryStackRespectNBT(i, respectArr[i] != 0);
            }
        }

        if (tag.hasKey(LOCKED_SLOTS_TAG, 7)) {
            byte[] lockedArr = tag.getByteArray(LOCKED_SLOTS_TAG);
            for (int i = 0; i < lockedArr.length && i < this.storageSlots; i++) {
                setSlotLocked(i, lockedArr[i] != 0);
            }
        }

        if (tag.hasKey(UPGRADE_INV, 10)) {
            upgradeHandler.deserializeNBT(tag.getCompoundTag(UPGRADE_INV));
            if (upgradeHandler.isSizeInconsistent(this.upgradeSlots)) {
                upgradeHandler.resize(this.upgradeSlots);
            }
        }

        if (tag.hasKey(SORT_TYPE_TAG, 1)) {
            byte type = tag.getByte(SORT_TYPE_TAG);
            if (type >= 0 && type < SortType.values().length) {
                this.sortType = SortType.values()[type];
            }
        }

        if (tag.hasKey(LOCKED_STORAGE_TAG, 1)) this.lockStorage = tag.getBoolean(LOCKED_STORAGE_TAG);
        if (tag.hasKey(KEEP_TAB_TAG, 1)) this.keepTab = tag.getBoolean(KEEP_TAB_TAG);

        if (tag.hasKey(UUID_TAG, 8)) {
            this.uuid = tag.getString(UUID_TAG);
        }

        if (tag.hasKey(PLAYER_UUID_TAG, 8)) {
            this.playerUuid = tag.getString(PLAYER_UUID_TAG);
        }

        if (tag.hasKey("display", 10)) {
            NBTTagCompound display = tag.getCompoundTag("display");
            if (display.hasKey("Name", 8)) this.customName = display.getString("Name");
        } else if (tag.hasKey(CUSTOM_NAME_TAG, 8)) {
            this.customName = tag.getString(CUSTOM_NAME_TAG);
        }
    }

    @Override
    public <T> Map<Integer, T> gatherCapabilityUpgrades(Class<T> capabilityClass) {
        Map<Integer, T> result = new HashMap<>();

        for (int i = 0; i < upgradeSlots; i++) {
            ItemStack stack = upgradeHandler.getStackInSlot(i);
            if (stack == null) continue;

            UpgradeWrapperBase wrapper = UpgradeWrapperFactory.createWrapper(stack, this);
            if (wrapper == null) continue;
            if (wrapper instanceof IToggleable toggleable && !toggleable.isEnabled()) continue;
            if (capabilityClass.isAssignableFrom(wrapper.getClass())) {
                result.put(i, capabilityClass.cast(wrapper));
            }
        }

        return result;
    }

    @Override
    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    @Override
    public SortType getSortType() {
        return sortType;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void markDirty() {
        this.isDirty = true;
        if (onInventoryHandlerRefresh != null) {
            onInventoryHandlerRefresh.run();
        }
    }

    @Override
    public void markClean() {
        this.isDirty = false;
    }

    @Override
    public void setInventorySlotChangeHandler(Runnable contentsChangeHandler) {
        this.onInventoryHandlerRefresh = contentsChangeHandler;
    }

    @Override
    public int getAccentColor() {
        return accentColor;
    }

    @Override
    public int getMainColor() {
        return mainColor;
    }

    @Override
    public void setColors(int mainColor, int accentColor) {
        this.mainColor = mainColor;
        this.accentColor = accentColor;
    }
}
