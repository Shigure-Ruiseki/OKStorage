package ruiseki.okstorage.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.SidedPosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import lombok.experimental.Delegate;
import ruiseki.okcore.persist.nbt.NBTPersist;
import ruiseki.okcore.tileentity.TileEntityOK;
import ruiseki.okcore.tileentity.TileSideCapability;
import ruiseki.okstorage.Reference;

public class TEStorage extends TileSideCapability
    implements ISidedInventory, IGuiHolder<SidedPosGuiData>, TileEntityOK.ITickingTile {

    private int[] allSlots;

    @NBTPersist
    private ForgeDirection facing = ForgeDirection.NORTH;

    @NBTPersist(StorageWrapper.BACKPACK_NBT)
    private StorageWrapper wrapper;

    @Delegate
    protected final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    public TEStorage() {
        wrapper = new StorageWrapper();
        this.wrapper.setInventorySlotChangeHandler(new Runnable() {

            @Override
            public void run() {
                markDirty();
            }
        });
        allSlots = new int[wrapper.getSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }
    }

    public void setWrapper(StorageWrapper wrapper) {
        this.wrapper = wrapper;
        this.wrapper.setInventorySlotChangeHandler(new Runnable() {

            @Override
            public void run() {
                markDirty();
            }
        });
        allSlots = new int[wrapper.getSlots()];
        for (int i = 0; i < allSlots.length; i++) {
            allSlots[i] = i;
        }
    }

    public boolean onBlockActivated(World world, EntityPlayer player, ForgeDirection side, float hitX, float hitY,
        float hitZ) {
        if (wrapper.canPlayerAccess(player.getUniqueID())) {
            if (!worldObj.isRemote) {
                GuiFactories.sidedTileEntity()
                    .open(player, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN);
            }
        }
        return true;
    }

    @Override
    protected void doUpdate() {
        super.doUpdate();
        if (wrapper.tick(worldObj, getPos())) {
            markDirty();
        }
    }

    @Override
    public ModularScreen createScreen(SidedPosGuiData data, ModularPanel mainPanel) {
        return new ModularScreen(Reference.MOD_ID, mainPanel);
    }

    @Override
    public ModularPanel buildUI(SidedPosGuiData data, PanelSyncManager syncManager, UISettings settings) {
        return new StorageGuiHolder.TileEntityGuiHolder(wrapper).buildUI(data, syncManager, settings);
    }

    public ForgeDirection getFacing() {
        return facing;
    }

    public void setFacing(ForgeDirection facing) {
        this.facing = facing;
        markDirty();
        onSendUpdate();
    }

    @Override
    public void onChunkLoad() {
        super.onChunkLoad();
        if (worldObj != null && !worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return allSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (slot < 0 || slot >= getSizeInventory()) {
            return false;
        }
        if (!wrapper.canInsert(slot, stack)) {
            return false;
        }
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (slot < 0 || slot >= getSizeInventory()) {
            return false;
        }
        ItemStack existing = wrapper.getStackInSlot(slot);
        if (existing == null || existing.stackSize < stack.stackSize) {
            return false;
        }
        if (!wrapper.canExtract(slot, stack)) {
            return false;
        }
        return stack.getItem() == existing.getItem();
    }

    @Override
    public int getSizeInventory() {
        return wrapper.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= getSizeInventory()) {
            return null;
        }
        return wrapper.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if (slot < 0 || slot >= getSizeInventory()) {
            return null;
        }
        ItemStack fromStack = wrapper.getStackInSlot(slot);
        if (fromStack == null) {
            return null;
        }
        if (fromStack.stackSize <= amount) {
            wrapper.setStackInSlot(slot, null);
            return fromStack;
        }
        ItemStack result = fromStack.splitStack(amount);
        wrapper.setStackInSlot(slot, fromStack.stackSize > 0 ? fromStack : null);
        return result;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getSizeInventory()) {
            return;
        }

        if (stack == null) {
            wrapper.setStackInSlot(slot, null);
        }

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        wrapper.setStackInSlot(slot, stack);
    }

    @Override
    public String getInventoryName() {
        return wrapper.getDisplayName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return wrapper.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64 * wrapper.applySlotLimitModifiers(1, 0);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return canInteractWith(player);
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }
}
