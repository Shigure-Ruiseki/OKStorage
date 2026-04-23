package ruiseki.okstorage.api.controller;

import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import ruiseki.okcore.datastructure.BlockPos;
import ruiseki.okcore.helper.TileHelpers;
import ruiseki.okstorage.common.block.controller.TEController;

public interface IControllerBoundable {

    String CONTROLLER_POS_TAG = "controllerPos";

    void setControllerPos(BlockPos controllerPos);

    Optional<BlockPos> getControllerPos();

    void removeControllerPos();

    BlockPos getStorageBlockPos();

    World getStorageBlockLevel();

    default boolean canBeConnected() {
        return getControllerPos().isEmpty();
    }

    void registerController(TEController controllerBlockEntity);

    void unregisterController();

    boolean canConnectStorages();

    default void runOnController(World world, Consumer<TEController> toRun) {
        getControllerPos().ifPresent(pos -> {
            TileHelpers.getTileEntity(world, pos, TEController.class)
                .ifPresent(toRun);
        });
    }

    default void saveControllerPos(NBTTagCompound tag) {
        getControllerPos().ifPresent(p -> tag.setLong(IControllerBoundable.CONTROLLER_POS_TAG, p.toLong()));
    }

    default void loadControllerPos(NBTTagCompound tag) {
        if (tag.hasKey(CONTROLLER_POS_TAG)) {
            BlockPos controllerPos = BlockPos.fromLong(tag.getLong(CONTROLLER_POS_TAG));
            setControllerPos(controllerPos);
        }
    }

    default void addToController(World level, BlockPos pos, BlockPos controllerPos) {
        // noop by default
    }

    default void addToAdjacentController() {
        World world = getStorageBlockLevel();
        if (!world.isRemote) {
            BlockPos pos = getStorageBlockPos();
            for (ForgeDirection dir : ForgeDirection.values()) {
                BlockPos offsetPos = pos.offset(dir);
                TileHelpers.getTileEntity(world, offsetPos, IControllerBoundable.class)
                    .ifPresentOrElse(boundable -> {
                        if (boundable.canConnectStorages()) {
                            boundable.getControllerPos()
                                .ifPresent(controllerPos -> addToController(world, pos, controllerPos));
                        }
                    }, () -> addToController(world, pos, offsetPos));
            }
        }
    }
}
