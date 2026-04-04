package ruiseki.okstorage.api.wrapper;

import net.minecraft.world.World;

import ruiseki.okcore.datastructure.BlockPos;

public interface ITickable {

    boolean tick(World world, BlockPos pos);
}
