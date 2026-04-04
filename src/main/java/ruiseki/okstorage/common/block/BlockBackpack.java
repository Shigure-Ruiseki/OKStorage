package ruiseki.okstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.blockstate.core.BlockPropertyTrait;
import com.gtnewhorizon.gtnhlib.blockstate.properties.DirectionBlockProperty;
import com.gtnewhorizon.gtnhlib.blockstate.registry.BlockPropertyRegistry;
import com.gtnewhorizon.gtnhlib.client.model.color.BlockColor;
import com.gtnewhorizon.gtnhlib.client.model.color.IBlockColor;

import lombok.Getter;
import ruiseki.okcore.block.BlockOK;
import ruiseki.okcore.item.ItemBlockOK;
import ruiseki.okstorage.OKBCreativeTab;

public class BlockBackpack extends BlockOK {

    @Getter
    private final int backpackSlots;
    @Getter
    private final int upgradeSlots;

    private final static DirectionBlockProperty property = new DirectionBlockProperty() {

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
            if (te instanceof TEBackpack tile) {
                return tile.getFacing();
            }
            return ForgeDirection.NORTH;
        }

        @Override
        public void setValue(World world, int x, int y, int z, ForgeDirection value) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TEBackpack tile) {
                tile.setFacing(value);
            }
        }

        @Override
        public ForgeDirection getValue(ItemStack stack) {
            return ForgeDirection.NORTH;
        }
    };

    public BlockBackpack(String name, int backpackSlots, int upgradeSlots) {
        super(name, TEBackpack.class, Material.cloth);
        setStepSound(soundTypeCloth);
        setHardness(1f);
        setCreativeTab(OKBCreativeTab.INSTANCE);
        this.backpackSlots = backpackSlots;
        this.upgradeSlots = upgradeSlots;
        this.isFullSize = this.isOpaque = false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemIn) {
        super.onBlockPlacedBy(world, x, y, z, player, itemIn);
        int heading = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        ForgeDirection facing = getDirectionForHeading(heading);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEBackpack backpack) {
            backpack.setFacing(facing);
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
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBackpack.class;
    }

    @Override
    protected void registerComponent() {

        BlockColor.registerBlockColors(new IBlockColor() {

            @Override
            public int colorMultiplier(@Nullable ItemStack stack, int tintIndex) {
                if (stack == null) return -1;

                BackpackWrapper wrapper = new BackpackWrapper(backpackSlots, upgradeSlots);
                return switch (tintIndex) {
                    case 0 -> wrapper.getMainColor();
                    case 1 -> wrapper.getAccentColor();
                    default -> -1;
                };
            }

            @Override
            public int colorMultiplier(@Nullable IBlockAccess world, int x, int y, int z, int tintIndex) {
                if (world == null) return -1;

                TileEntity te = world.getTileEntity(x, y, z);
                if (!(te instanceof TEBackpack backpack)) return -1;

                return switch (tintIndex) {
                    case 0 -> backpack.getMainColor();
                    case 1 -> backpack.getAccentColor();
                    default -> -1;
                };
            }

        }, this);

        BlockPropertyRegistry.registerBlockItemProperty(this, property);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        TEBackpack backpack = new TEBackpack();
        BackpackWrapper wrapper = new BackpackWrapper(backpack, backpackSlots, upgradeSlots);
        backpack.setWrapper(wrapper);
        return backpack;
    }

    @Override
    public boolean shouldDropInventory(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX,
        float subY, float subZ) {
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof TEBackpack backpack) {
            return backpack.onBlockActivated(worldIn, player, ForgeDirection.getOrientation(side), subX, subY, subZ);
        }
        return super.onBlockActivated(worldIn, x, y, z, player, side, subX, subY, subZ);
    }

    public static class ItemBackpack extends ItemBlockOK {

        public ItemBackpack(Block block) {
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
