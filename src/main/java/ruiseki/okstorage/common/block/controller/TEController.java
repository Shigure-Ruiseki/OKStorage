package ruiseki.okstorage.common.block.controller;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;
import ruiseki.okcore.datastructure.BlockPos;
import ruiseki.okcore.helper.TileHelpers;
import ruiseki.okcore.item.IItemHandlerModifiable;
import ruiseki.okcore.tileentity.TileEntityOK;
import ruiseki.okstorage.api.controller.IControllerBoundable;
import ruiseki.okstorage.common.search.ItemStackKey;

public class TEController extends TileEntityOK {

    private static final long INVALID_SLOT_LOG_INTERVAL_TICKS = 20;
    private static final int INVALID_SLOT_REFRESH_THRESHOLD = 2;
    private static final long INVALID_SLOT_REFRESH_COOLDOWN_TICKS = 40;

    private List<BlockPos> storagePositions = new ArrayList<>();
    private final Map<BlockPos, Integer> storagePositionIndexes = new HashMap<>();
    private List<Integer> baseIndexes = new ArrayList<>();
    private int totalSlots = 0;
    protected final Map<ItemStackKey, Set<BlockPos>> stackStorages = new HashMap<>();
    private final Map<BlockPos, Set<ItemStackKey>> storageStacks = new HashMap<>();
    protected final Map<Item, Set<ItemStackKey>> itemStackKeys = new HashMap<>();
    private final Comparator<BlockPos> distanceComparator = Comparator.<BlockPos>comparingDouble(p -> p.distSqr(getPos())).thenComparing(Comparator.naturalOrder());
    protected final Set<BlockPos> emptySlotsStorages = new TreeSet<>(distanceComparator);

    protected final Map<Item, Set<BlockPos>> memorizedItemStorages = new HashMap<>();
    private final Map<BlockPos, Set<Item>> storageMemorizedItems = new HashMap<>();
    protected final Map<Integer, Set<BlockPos>> memorizedStackStorages = new HashMap<>();
    private final Map<BlockPos, Set<Integer>> storageMemorizedStacks = new HashMap<>();
    protected final Map<Item, Set<BlockPos>> filterItemStorages = new HashMap<>();
    private final Map<BlockPos, Set<Item>> storageFilterItems = new HashMap<>();
    private Set<BlockPos> linkedBlocks = new TreeSet<>(distanceComparator);
    private Set<BlockPos> connectingBlocks = new TreeSet<>(distanceComparator);
    private Set<BlockPos> nonConnectingBlocks = new TreeSet<>(distanceComparator);

    private WeakReference<IItemHandlerModifiable>[] cachedHandlers = new WeakReference[0];
    private long lastInvalidSlotLogTime = -INVALID_SLOT_LOG_INTERVAL_TICKS;
    private long lastInvalidSlotRefreshTime = -INVALID_SLOT_REFRESH_COOLDOWN_TICKS;
    private int invalidSlotIncidentCount = 0;
    private boolean refreshingAfterInvalidSlots = false;

    public TEController() {

    }

    @Override
    public void onChunkLoad() {
        super.onChunkLoad();
        if (worldObj != null && !worldObj.isRemote) {
            stackStorages.clear();
            storageStacks.clear();
            itemStackKeys.clear();
            emptySlotsStorages.clear();
//            storagePositions.forEach(this::addStorageStacksAndRegisterListeners);
        }
    }

    public boolean isStorageConnected(BlockPos storagePos) {
        return storagePositions.contains(storagePos);
    }

    public void searchAndAddBoundables() {
        Set<BlockPos> positionsToCheck = new HashSet<>();
        for (ForgeDirection dir : ForgeDirection.values()) {
            positionsToCheck.add(getPos().offset(dir));
        }
        searchAndAddBoundables(positionsToCheck, false);
    }

    public void changeSlots(BlockPos storagePos, int newSlots, boolean hasEmptySlots) {
        updateBaseIndexesAndTotalSlots(storagePos, newSlots);
        updateEmptySlots(storagePos, hasEmptySlots);
    }

    public void updateEmptySlots(BlockPos storagePos, boolean hasEmptySlots) {
        if (emptySlotsStorages.contains(storagePos) && !hasEmptySlots) {
            emptySlotsStorages.remove(storagePos);
        } else if (!emptySlotsStorages.contains(storagePos) && hasEmptySlots) {
            emptySlotsStorages.add(storagePos);
        }
    }

    private void updateBaseIndexesAndTotalSlots(BlockPos storagePos, int newSlots) {
        int index = storagePositions.indexOf(storagePos);
        int originalSlots = getStorageSlots(index);

        int diff = newSlots - originalSlots;

        for (int i = index; i < baseIndexes.size(); i++) {
            baseIndexes.set(i, baseIndexes.get(i) + diff);
        }

        totalSlots += diff;
        onSendUpdate();
    }

    private int getStorageSlots(int index) {
        int previousBaseIndex = index == 0 ? 0 : baseIndexes.get(index - 1);
        return baseIndexes.get(index) - previousBaseIndex;
    }

    public int getSlots(int storageIndex) {
        if (storageIndex < 0 || storageIndex >= baseIndexes.size()) {
            return 0;
        }
        return getStorageSlots(storageIndex);
    }

    private void searchAndAddBoundables(Set<BlockPos> positionsToCheck, boolean addingLinkedSelf) {
        Set<BlockPos> positionsChecked = new HashSet<>();

        boolean first = true;
        while (!positionsToCheck.isEmpty()) {
            Iterator<BlockPos> it = positionsToCheck.iterator();
            BlockPos posToCheck = it.next();
            it.remove();

            final boolean finalFirst = first;
            IControllerBoundable boundable = TileHelpers.getSafeTile(worldObj, posToCheck, IControllerBoundable.class);
            if (boundable != null) {
//                tryToConnectStorageAndAddPositionsToCheckAround(positionsToCheck, addingLinkedSelf, positionsChecked, posToCheck, finalFirst, boundable);
            }
            else {
                positionsChecked.add(posToCheck);
            }
            first = false;
        }
    }

}
