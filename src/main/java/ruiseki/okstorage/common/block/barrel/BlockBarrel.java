package ruiseki.okstorage.common.block.barrel;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.blockstate.core.BlockPropertyTrait;
import com.gtnewhorizon.gtnhlib.blockstate.properties.BooleanBlockProperty;
import com.gtnewhorizon.gtnhlib.blockstate.registry.BlockPropertyRegistry;

import ruiseki.okcore.helper.TileHelpers;
import ruiseki.okstorage.api.IOpenState;
import ruiseki.okstorage.common.block.BlockStorage;

public class BlockBarrel extends BlockStorage {

    private final static BooleanBlockProperty open = new BooleanBlockProperty() {

        @Override
        public String getName() {
            return "open";
        }

        @Override
        public boolean hasTrait(BlockPropertyTrait trait) {
            return switch (trait) {
                case SupportsWorld, WorldMutable, SupportsStacks, StackMutable -> true;
                default -> false;
            };
        }

        @Override
        public Boolean getValue(IBlockAccess world, int x, int y, int z) {
            IOpenState state = TileHelpers.getSafeTile(world, x, y, z, IOpenState.class);
            return state != null && state.isOpen();
        }

        @Override
        public Boolean getValue(ItemStack stack) {
            return false;
        }
    };

    public BlockBarrel(String name, int slots, int upgradeSlots) {
        super(name, TEBarrel.class, Material.wood, slots, upgradeSlots);
    }

    @Override
    protected void registerComponent() {
        super.registerComponent();
        BlockPropertyRegistry.registerBlockItemProperty(this, open);
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof TEBarrel storage) {
            storage.setOpen(true);
            return storage.onBlockActivated(worldIn, player, ForgeDirection.getOrientation(side), subX, subY, subZ);
        }
        return true;
    }
}
