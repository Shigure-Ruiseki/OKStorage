package ruiseki.okstorage.common.block.storage;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.blockstate.core.BlockPropertyTrait;
import com.gtnewhorizon.gtnhlib.blockstate.properties.DirectionBlockProperty;
import com.gtnewhorizon.gtnhlib.blockstate.registry.BlockPropertyRegistry;

import lombok.Getter;
import ruiseki.okcore.block.BlockOK;
import ruiseki.okcore.tileentity.TileEntityOK;
import ruiseki.okstorage.OKSCreativeTab;
import ruiseki.okstorage.api.wrapper.IAdminProtectable;

public class BlockStorage extends BlockOK {

    @Getter
    private final int slots;
    @Getter
    private final int upgradeSlots;

    private final static DirectionBlockProperty facing = new DirectionBlockProperty() {

        @Override
        public String getName() {
            return "facing";
        }

        @Override
        public boolean hasTrait(BlockPropertyTrait trait) {
            return switch (trait) {
                case SupportsWorld, WorldMutable, StackMutable, SupportsStacks -> true;
                default -> false;
            };
        }

        @Override
        public ForgeDirection getValue(IBlockAccess world, int x, int y, int z) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TEStorage tile) {
                return tile.getFacing();
            }
            return ForgeDirection.NORTH;
        }

        @Override
        public ForgeDirection getValue(ItemStack stack) {
            return ForgeDirection.NORTH;
        }
    };

    public BlockStorage(String name, int slots, int upgradeSlots) {
        this(name, TEStorage.class, Material.wood, slots, upgradeSlots);
    }

    public BlockStorage(String name, Class<? extends TileEntityOK> teClass, Material material, int slots,
        int upgradeSlots) {
        super(name, teClass, material);
        setStepSound(soundTypeWood);
        setHardness(1f);
        setCreativeTab(OKSCreativeTab.INSTANCE);
        this.slots = slots;
        this.upgradeSlots = upgradeSlots;
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        if (hasAdminProtection(world, x, y, z) && !player.capabilities.isCreativeMode) {
            return -1.0f;
        }
        return super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }

    private boolean hasAdminProtection(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEStorage backpack) {
            for (var entry : backpack.getWrapper()
                .gatherCapabilityUpgrades(IAdminProtectable.class)
                .values()) {
                if (entry.isAdmin()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public float getExplosionResistance(Entity exploder, World world, int x, int y, int z, double explosionX,
        double explosionY, double explosionZ) {
        if (hasAdminProtection(world, x, y, z)) {
            return Float.MAX_VALUE;
        }
        return super.getExplosionResistance(exploder, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        if (hasAdminProtection(world, x, y, z)) {
            return;
        }
        super.onBlockExploded(world, x, y, z, explosion);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemIn) {
        super.onBlockPlacedBy(world, x, y, z, player, itemIn);
        int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        ForgeDirection facing = getDirectionForHeading(heading);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEStorage storage) {
            storage.setFacing(facing);
            if (!world.isRemote) {
                storage.getWrapper()
                    .forceStopAllJukeboxes(world, x + 0.5f, y + 0.5f, z + 0.5f);
            }
        }
    }

    private ForgeDirection getDirectionForHeading(int heading) {
        return switch (heading) {
            case 1 -> ForgeDirection.EAST;
            case 2 -> ForgeDirection.NORTH;
            case 3 -> ForgeDirection.WEST;
            default -> ForgeDirection.SOUTH;
        };
    }

    @Override
    protected void registerComponent() {
        BlockPropertyRegistry.registerBlockItemProperty(this, facing);
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof TEStorage storage) {
            return storage.onBlockActivated(worldIn, player, ForgeDirection.getOrientation(side), subX, subY, subZ);
        }
        return true;
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemStorage.class;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        TEStorage storage = (TEStorage) super.createTileEntity(world, metadata);
        StorageWrapper wrapper = new StorageWrapper(slots, upgradeSlots);
        storage.setWrapper(wrapper);
        return storage;
    }

    public static class ItemStorage extends ItemBlock {

        public ItemStorage(Block block) {
            super(block);
        }

        @Override
        public String getItemStackDisplayName(ItemStack stack) {
            if (stack.hasTagCompound() && stack.getTagCompound()
                .hasKey("display", 10)) {
                return stack.getTagCompound()
                    .getCompoundTag("display")
                    .getString("Name");
            }
            return super.getItemStackDisplayName(stack);
        }
    }
}
