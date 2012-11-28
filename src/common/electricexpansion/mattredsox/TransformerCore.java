package net.minecraft.src.Transformer;

import net.minecraft.src.*;
import net.minecraft.src.basiccomponents.*;
import net.minecraft.src.forge.*;
import net.minecraft.src.universalelectricity.*;

public class TransformerCore {

	public final String Version = "0.1.2";
	public final String UEVersion = "0.4.2";

	public NetworkMod mod;

	public static int TransformerRenderID;
	public RenderTransformer TransformerRenderer;

	public static String textureFile = "/transformer/textures/items.png";
	
	public static final Block blocktransformer = new BlockTransformer(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "BlockTransformer", 189, true));
	public static final Item itemtransformer = new ItemTransformer(UniversalElectricity.getConfigID(UniversalElectricity.configuration, "ItemTransformer", 1584, false), blocktransformer).setItemName("Transformer");

	public TransformerCore(NetworkMod networkmod) {
		mod = networkmod;
		initialize();
	}

	public void initialize() {
		MinecraftForgeClient.preloadTexture(textureFile);

		TransformerRenderID = ModLoader.getUniqueBlockModelID(mod, true);
		TransformerRenderer = new RenderTransformer();

		ModLoader.registerBlock(blocktransformer);
		ModLoader.registerTileEntity(TileEntityTransformer.class, "TileEntityTransformer");

		ModLoader.addName(new ItemStack(itemtransformer, 1, 0), "Transformer");
		ModLoader.addName(new ItemStack(itemtransformer, 1, 1), "Tier 2 upgrade");
		ModLoader.addName(new ItemStack(itemtransformer, 1, 2), "Tier 3 upgrade");

		ModLoader.addRecipe(new ItemStack(itemtransformer, 1, 0), new Object[] { "@@@", "? ?", "@!@", '?', BasicComponents.ItemMotor, '!', BasicComponents.ItemSteelPlate, '@', BasicComponents.ItemSteelIngot });
		ModLoader.addRecipe(new ItemStack(itemtransformer, 1, 1), new Object[] { "@@@", "? ?", "@!@", '?', BasicComponents.ItemMotor, '!', new ItemStack(BasicComponents.ItemCircuit, 1, 0), '@', BasicComponents.ItemSteelIngot });
		ModLoader.addRecipe(new ItemStack(itemtransformer, 1, 2), new Object[] { "@@@", "? ?", "@!@", '?', BasicComponents.ItemMotor, '!', new ItemStack(BasicComponents.ItemCircuit, 1, 1), '@', BasicComponents.ItemSteelIngot });
	}

	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess world, int x, int y, int z, Block block, int modelID) {
		if (modelID == TransformerRenderID) {
			if (world.getBlockMetadata(x, y, z) == 2) {
				TileEntityTransformer transformer = (TileEntityTransformer) world.getBlockTileEntity(x, y, z);
				return TransformerRenderer.rendertop(renderblocks, transformer, x, y, z, block);
			} else if (world.getBlockMetadata(x, y, z) == 1 && world.getBlockId(x, y + 1, z) != this.blocktransformer.blockID) {
				TileEntityTransformer transformer = (TileEntityTransformer) world.getBlockTileEntity(x, y, z);
				return TransformerRenderer.renderMiddle(renderblocks, transformer, x, y, z, block);
			} else if (world.getBlockMetadata(x, y, z) == 1 && world.getBlockId(x, y + 1, z) == this.blocktransformer.blockID && world.getBlockMetadata(x, y + 1, z) == 2) {
				TileEntityTransformer transformer = (TileEntityTransformer) world.getBlockTileEntity(x, y, z);
				return TransformerRenderer.renderExtendableMiddle(renderblocks, transformer, x, y, z, block);
			} else if (world.getBlockMetadata(x, y, z) == 0 && world.getBlockId(x, y + 1, z) == this.blocktransformer.blockID && world.getBlockMetadata(x, y + 1, z) == 1) {
				TileEntityTransformer transformer = (TileEntityTransformer) world.getBlockTileEntity(x, y, z);
				return TransformerRenderer.renderExtendableBase(renderblocks, transformer, x, y, z, block);
			} else if (world.getBlockMetadata(x, y, z) == 0 && world.getBlockId(x, y + 1, z) != this.blocktransformer.blockID) {
				TileEntityTransformer transformer = (TileEntityTransformer) world.getBlockTileEntity(x, y, z);
				return TransformerRenderer.renderBase(renderblocks, transformer, x, y, z, block);
			}
		}
		return false;
	}
}
