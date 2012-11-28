package net.minecraft.src.Transformer;

import net.minecraft.src.*;
import net.minecraft.src.universalelectricity.*;

public class RenderTransformer {

	int[] j = new int[] { 3, 4, 2, 5, 1, 6 };

	public boolean renderBase(RenderBlocks renderblocks, TileEntityTransformer transformer, int x, int y, int z, Block block) {
		if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.1F, 0.0F, 0.6F, 0.8F, 0.2F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.1F, 0.8F, 0.6F, 0.8F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 0;
			block.setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 0.9F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3) {
				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(0.4F, 0.4F, -0.1F, 0.6F, 0.6F, 0.0F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(0.4F, 0.4F, 1.0F, 0.6F, 0.6F, 1.1F);
				renderblocks.renderStandardBlock(block, x, y, z);
				
				for (int i = 0; i < transformer.primaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(0.4F, 0.4F, -0.1F, 0.6F, 0.6F, 0.0F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(0.4F, 0.4F, 1.0F, 0.6F, 0.6F, 1.1F);
				renderblocks.renderStandardBlock(block, x, y, z);

				for (int i = 0; i < transformer.primaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}

		} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.0F, 0.1F, 0.4F, 0.2F, 0.8F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.8F, 0.1F, 0.4F, 1.0F, 0.8F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 0;
			block.setBlockBounds(0.0F, 0.8F, 0.4F, 1.0F, 0.9F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(-0.1F, 0.4F, 0.4F, 0.0F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(1.0F, 0.4F, 0.4F, 1.1F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				for (int i = 0; i < transformer.primaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4) {
				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(-0.1F, 0.4F, 0.4F, 0.0F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(1.0F, 0.4F, 0.4F, 1.1F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				for (int i = 0; i < transformer.primaryCoil && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		}
		renderblocks.overrideBlockTexture = 0;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		renderblocks.renderStandardBlock(block, x, y, z);
		renderblocks.overrideBlockTexture = -1;
		return true;
	}

	public boolean renderExtendableBase(RenderBlocks renderblocks, TileEntityTransformer transformer, int x, int y, int z, Block block) {
		if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.1F, 0.0F, 0.6F, 1.0F, 0.2F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.1F, 0.8F, 0.6F, 1.0F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3) {
				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(0.4F, 0.4F, -0.1F, 0.6F, 0.6F, 0.0F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(0.4F, 0.4F, 1.0F, 0.6F, 0.6F, 1.1F);
				renderblocks.renderStandardBlock(block, x, y, z);

				for (int i = 0; i < transformer.primaryCoil && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(0.4F, 0.4F, -0.1F, 0.6F, 0.6F, 0.0F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(0.4F, 0.4F, 1.0F, 0.6F, 0.6F, 1.1F);
				renderblocks.renderStandardBlock(block, x, y, z);

				for (int i = 0; i < transformer.primaryCoil && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}

		} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.0F, 0.1F, 0.4F, 0.2F, 1.0F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.8F, 0.1F, 0.4F, 1.0F, 1.0F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(-0.1F, 0.4F, 0.4F, 0.0F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(1.0F, 0.4F, 0.4F, 1.1F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				for (int i = 0; i < transformer.primaryCoil && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4) {
				renderblocks.overrideBlockTexture = 3;
				block.setBlockBounds(-0.1F, 0.4F, 0.4F, 0.0F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);

				renderblocks.overrideBlockTexture = 2;
				block.setBlockBounds(1.0F, 0.4F, 0.4F, 1.1F, 0.6F, 0.6F);
				renderblocks.renderStandardBlock(block, x, y, z);
				
				for (int i = 0; i < transformer.primaryCoil && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < transformer.secondaryCoil && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		}
		renderblocks.overrideBlockTexture = 0;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		renderblocks.renderStandardBlock(block, x, y, z);
		renderblocks.overrideBlockTexture = -1;
		return true;
	}

	public boolean renderExtendableMiddle(RenderBlocks renderblocks, TileEntityTransformer transformer, int x, int y, int z, Block block) {
		if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.0F, 0.0F, 0.6F, 1.0F, 0.2F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.0F, 0.8F, 0.6F, 1.0F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}	
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}	
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.0F, 0.0F, 0.4F, 0.2F, 1.0F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.8F, 0.0F, 0.4F, 1.0F, 1.0F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		}
		renderblocks.overrideBlockTexture = -1;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		return true;
	}

	public boolean renderMiddle(RenderBlocks renderblocks, TileEntityTransformer transformer, int x, int y, int z, Block block) {
		if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.0F, 0.0F, 0.6F, 0.8F, 0.2F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.0F, 0.8F, 0.6F, 0.8F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 0;
			block.setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 0.9F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.0F, 0.0F, 0.4F, 0.2F, 0.8F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.8F, 0.0F, 0.4F, 1.0F, 0.8F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 0;
			block.setBlockBounds(0.0F, 0.8F, 0.4F, 1.0F, 0.9F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4) {
				for (int i = 0; i < (transformer.primaryCoil - 6) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 6) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		}
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		renderblocks.overrideBlockTexture = -1;
		return true;
	}

	public boolean rendertop(RenderBlocks renderblocks, TileEntityTransformer transformer, int x, int y, int z, Block block) {
		if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.0F, 0.0F, 0.6F, 0.8F, 0.2F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.4F, 0.0F, 0.8F, 0.6F, 0.8F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 0;
			block.setBlockBounds(0.4F, 0.8F, 0.0F, 0.6F, 0.9F, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 3) {
				for (int i = 0; i < (transformer.primaryCoil - 12) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 12) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 2) {
				for (int i = 0; i < (transformer.primaryCoil - 12) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F, 0.7F, (float) (0.12F + (0.11 * j[i])), 1.1F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 12) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.3F, (float) (0.12F + (0.11 * j[i])), -0.1F, 0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}	
			}
		} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4 || UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {
			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.0F, 0.0F, 0.4F, 0.2F, 0.8F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 1;
			block.setBlockBounds(0.8F, 0.0F, 0.4F, 1.0F, 0.8F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			renderblocks.overrideBlockTexture = 0;
			block.setBlockBounds(0.0F, 0.8F, 0.4F, 1.0F, 0.9F, 0.6F);
			renderblocks.renderStandardBlock(block, x, y, z);

			if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 5) {				
				for (int i = 0; i < (transformer.primaryCoil - 12) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 12) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			} else if (UniversalElectricity.getOrientationFromSide(transformer.facingDirection, (byte) 2) == 4) {
				for (int i = 0; i < (transformer.primaryCoil - 12) && i < 6; i++) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(0.7F, (float) (0.12F + (0.11 * j[i])), 0.3F, 1.1F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
				for (int i = 0; i < (transformer.secondaryCoil - 12) && i < 6; ++i) {
					renderblocks.overrideBlockTexture = 7;
					block.setBlockBounds(-0.1F, (float) (0.12F + (0.11 * j[i])), 0.3F, 0.3F, (float) (0.12F + (0.11 * j[i])), 0.7F);
					renderblocks.renderStandardBlock(block, x, y, z);
				}
			}
		}
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		renderblocks.overrideBlockTexture = -1;
		return true;
	}
}
