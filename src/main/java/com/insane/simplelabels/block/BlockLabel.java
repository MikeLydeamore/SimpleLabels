package com.insane.simplelabels.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.insane.simplelabels.tile.TileLabel;

public class BlockLabel extends Block implements ITileEntityProvider
{

    public static float BOUNDS_MIN = 2f / 16f;
    public static float BOUNDS_MAX = 14f / 16f;
    public static float WIDTH = 0.01f;
    public static int renderId;

    public static final PropertyEnum DIRECTION = PropertyEnum.create("direction", EnumFacing.class);
    
    public BlockLabel()
    {
        super(Material.wood);
        this.setUnlocalizedName("label");
        this.setRegistryName("label");
        this.setStepSound(soundTypeWood);
        this.setHardness(2f);
    }
    
    @Override
    protected BlockState createBlockState()
    {
    	return new BlockState(this, new IProperty[] { DIRECTION });
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
    	return getDefaultState().withProperty(DIRECTION, EnumFacing.getFront(meta));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) 
    {
        EnumFacing side = (EnumFacing) state.getValue(DIRECTION);
        return side.getIndex();
    }
    
    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock)
    {
    	TileLabel te = (TileLabel) world.getTileEntity(pos);
    	if (te.getDSU() == null)
    	{
    		this.breakBlock(world, pos, state);
    	}
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs tab, List subItems)
    {
        for (int ix = 0; ix < 6; ix++)
        {
            subItems.add(new ItemStack(this, 1, ix));
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        TileLabel te = new TileLabel();
        te.init(meta);
        return te;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player)
    {
    	IBlockState state = this.getDefaultState();
    	state = state.withProperty(DIRECTION, facing);
        return state;
    }
    
    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
    {
    	if (!world.isRemote)
    	{
    		TileLabel te = (TileLabel) world.getTileEntity(pos);
    	
    		te.onRightClick(player.isSneaking(), player);
    	}
    	
    	super.onBlockClicked(world, pos, player);
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase livingBase, ItemStack stack)
    {
    	int meta = getMetaFromState(state);
    	if (meta == 0 || meta == 1)
    	{
    		int whichDirectionFacing = MathHelper.floor_double((double)(livingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	
    		TileLabel te = (TileLabel) world.getTileEntity(pos);
    		te.setPlacedDirection(whichDirectionFacing);
    	}
    }
    
    @Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float var7, float var8, float var9) {
    	
		if (!world.isRemote) 
		{
			TileLabel te = (TileLabel) world.getTileEntity(pos);
			te.addFromPlayer(player);
		}
		
		return true;
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        int meta = getMetaFromState(world.getBlockState(pos));
        switch (meta)
        {
        case 0:
            this.setBlockBounds(BOUNDS_MIN, 1 - WIDTH, BOUNDS_MIN, BOUNDS_MAX, 1, BOUNDS_MAX);
            break;
        case 1:
            this.setBlockBounds(BOUNDS_MIN, 0, BOUNDS_MIN, BOUNDS_MAX, WIDTH, BOUNDS_MAX);
            break;
        case 2:
            this.setBlockBounds(BOUNDS_MIN, BOUNDS_MIN, 1 - WIDTH, BOUNDS_MAX, BOUNDS_MAX, 1);
            break;
        case 3:
            this.setBlockBounds(BOUNDS_MIN, BOUNDS_MIN, 0, BOUNDS_MAX, BOUNDS_MAX, WIDTH);
            break;
        case 4:
            this.setBlockBounds(1 - WIDTH, BOUNDS_MIN, BOUNDS_MIN, 1, BOUNDS_MAX, BOUNDS_MAX);
            break;
        case 5:
            this.setBlockBounds(0, BOUNDS_MIN, BOUNDS_MIN, WIDTH, BOUNDS_MAX, BOUNDS_MAX);
            break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(1 - WIDTH, BOUNDS_MIN, BOUNDS_MIN, 1, BOUNDS_MAX, BOUNDS_MAX);
    }

    @Override
    public int getRenderType()
    {
        return renderId;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }
    
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
