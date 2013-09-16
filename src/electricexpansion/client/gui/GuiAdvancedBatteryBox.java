package electricexpansion.client.gui;

import java.util.Map;
import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.client.misc.TextureLocations;
import electricexpansion.common.containers.ContainerAdvancedBatteryBox;
import electricexpansion.common.tile.TileEntityAdvancedBatteryBox;
import electricexpansion.common.misc.EnumAdvBattBoxMode;
import net.minecraftforge.common.ForgeDirection;
import com.google.common.collect.ImmutableMap;

@SideOnly(Side.CLIENT)
public class GuiAdvancedBatteryBox extends GuiContainer
{
	private TileEntityAdvancedBatteryBox tileEntity;

	private int guiTopLeftX;
	private int guiTopLeftY;

	static final Map<ForgeDirection, int[]> dirChooserCoords = ImmutableMap.<ForgeDirection, int[]>builder()
		.put(ForgeDirection.UP, new int[]{386, 214})
		.put(ForgeDirection.DOWN, new int[]{386, 272})
		.put(ForgeDirection.NORTH, new int[]{404, 226})
		.put(ForgeDirection.SOUTH, new int[]{368, 260})
		.put(ForgeDirection.EAST, new int[]{416, 244})
		.put(ForgeDirection.WEST, new int[]{356, 244}).build();

	static final Map<String, int[]> dirChooserSprites = ImmutableMap.<String, int[]>builder()
		.put("INPUT", new int[]{0, 406})
		.put("OUTPUT", new int[]{16, 406}).build();

	static final Map<String, int[]> modeChangeCoords = ImmutableMap.<String, int[]>builder()
		.put("INPUT", new int[]{394, 82})
		.put("OUTPUT", new int[]{394, 130}).build();


	private ArrayList<EnumAdvBattBoxMode> validModes;

	public GuiAdvancedBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityAdvancedBatteryBox te)
	{
		super(new ContainerAdvancedBatteryBox(par1InventoryPlayer, te));
		this.tileEntity = te;
		this.validModes = te.getAvailableModes();
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.xSize = 220;

		this.guiTopLeftX = (this.width - this.xSize) / 2;
		this.guiTopLeftY = (this.height - this.ySize) / 2;

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 4, 6, 4210752);
		String displayJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getEnergyStored(), ElectricUnit.JOULES);
		String displayMaxJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getMaxEnergyStored(), ElectricUnit.JOULES);
		String displayInputVoltage = ElectricityDisplay.getDisplayShort(this.tileEntity.getInputVoltage(), ElectricUnit.VOLTAGE);
		String displayOutputVoltage = ElectricityDisplay.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE);

		this.fontRenderer.drawString(displayJoules + " of", 73 - displayJoules.length(), 25, 4210752);
		this.fontRenderer.drawString(displayMaxJoules, 70, 35, 4210752);
		this.fontRenderer.drawString("Output: " + displayOutputVoltage, 40, 55, 4210752);
		this.fontRenderer.drawString("Input: " + displayInputVoltage, 40, 65, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(TextureLocations.GUI_BAT_BOX);

		this.xSize = 220;

		this.guiTopLeftX = (this.width - this.xSize) / 2;
		this.guiTopLeftY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.guiTopLeftX, this.guiTopLeftY, 0, 0, this.xSize, this.ySize);

		EnumAdvBattBoxMode mode = this.tileEntity.getInputMode();
		this.drawTexturedModalRect(this.guiTopLeftX + 197, guiTopLeftY + 41, mode.ordinal() * 17, 169, 16, 16);

		mode = this.tileEntity.getOutputMode();
		this.drawTexturedModalRect(this.guiTopLeftX + 197, guiTopLeftY + 65, mode.ordinal() * 17, 186, 16, 16);

		int scale = (int) (this.tileEntity.getEnergyStored() / this.tileEntity.getMaxEnergyStored() * 72.0D);
		this.drawTexturedModalRect(this.guiTopLeftX + 64, this.guiTopLeftY + 46, 0, 166, scale, 3);

		// Draw input/output sprites on the direction chooser
		int[] sprite = dirChooserSprites.get("INPUT");
		int[] pos = dirChooserCoords.get(this.tileEntity.getInputDir());
		this.drawTexturedModalRect(this.guiTopLeftX + pos[0] / 2, this.guiTopLeftY + pos[1] / 2, sprite[0] / 2, sprite[1] / 2, 7, 7);

		sprite = dirChooserSprites.get("OUTPUT");
		pos = dirChooserCoords.get(this.tileEntity.getOutputDir());
		this.drawTexturedModalRect(this.guiTopLeftX + pos[0] / 2, this.guiTopLeftY + pos[1] / 2, sprite[0] / 2, sprite[1] / 2, 7, 7);

	}

	@Override
	protected void mouseClicked(int x, int y, int buttonID)
	{
		super.mouseClicked(x, y, buttonID);
		int targetX, targetY;

		for (Map.Entry<String, int[]> entry : modeChangeCoords.entrySet()) {
			int[] coords = entry.getValue();
			targetX = coords[0] / 2 + this.guiTopLeftX;
			targetY = coords[1] / 2 + this.guiTopLeftY;

			if (x >= targetX && x <= (targetX + 16) && y >= targetY && y <= (targetY + 16))
			{

				// When put in contstructor, doesn't properly load everything first time gui is opened
				this.validModes = this.tileEntity.getAvailableModes();

				if (entry.getKey() == "INPUT")
				{
					int newMode = (this.validModes.indexOf(this.tileEntity.getInputMode()) + 1) % this.validModes.size();
					this.tileEntity.setInputMode(this.validModes.get(newMode));
					return;
				} 
				else if (entry.getKey() == "OUTPUT")
				{
					int newMode = (this.validModes.indexOf(this.tileEntity.getOutputMode()) + 1) % this.validModes.size();
					this.tileEntity.setOutputMode(this.validModes.get(newMode));
					return;
				}
			}
		}

		for (Map.Entry<ForgeDirection, int[]> entry : dirChooserCoords.entrySet()) {
			int[] coords = entry.getValue();
			targetX = coords[0] / 2 + this.guiTopLeftX;
			targetY = coords[1] / 2 + this.guiTopLeftY;
			if (x >= targetX && x <= (targetX + 7) && y >= targetY && y <= (targetY + 7))
			{
				ForgeDirection dir = entry.getKey();
				if (this.tileEntity.getInputDir() != dir && this.tileEntity.getOutputDir() != dir)
				{
					if (buttonID == 1 || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					{
						this.tileEntity.setOutputDir(dir);
					} 
					else 
					{
						this.tileEntity.setInputDir(dir);
					}
				}
				return;
			}
		}
	}
}
