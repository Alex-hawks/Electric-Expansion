package electricexpansion.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.prefab.network.PacketManager;

import cpw.mods.fml.common.network.PacketDispatcher;

import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.cables.TileEntityLogisticsWire;

public class GuiLogisticsWire extends GuiScreen {
    
	private TileEntityLogisticsWire tileEntity;

	public GuiLogisticsWire(TileEntityLogisticsWire LogisticsWire) 
	{
        this.tileEntity = LogisticsWire;
	}

	public final int xSizeOfTexture = 176;
	public final int ySizeOfTexture = 88;
		
	@Override
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();

		int var4 = this.mc.renderEngine.getTexture(ElectricExpansion.MATT_TEXTURE_PATH + "Logistics.png");
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.renderEngine.bindTexture(var4);

		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;

		drawTexturedModalRect(posX, posY, 0, 0, xSizeOfTexture, ySizeOfTexture);
		
		this.fontRenderer.drawString("Logistics Wire", posX + xSizeOfTexture / 2 - 35, posY + 4, 4210752);
		
		super.drawScreen(x, y, f);
	}

	public void initGui() {
		this.controlList.clear();

		int posX = (this.width - xSizeOfTexture) / 2;
		int posY = (this.height - ySizeOfTexture) / 2;

		this.controlList.add(new GuiSwitchButton(0, posX + 13, posY + 15, 150, 16, "Redstone Output", tileEntity.buttonStatus0));
		this.controlList.add(new GuiSwitchButton(1, posX + 13, posY + 38, 150, 16, "Unused", tileEntity.buttonStatus1));
		this.controlList.add(new GuiSwitchButton(2, posX + 13, posY + 61, 150, 16, "Unused", tileEntity.buttonStatus2));
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			tileEntity.buttonStatus0 = !tileEntity.buttonStatus0;
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (int) -1, tileEntity.buttonStatus0));
			break;
		case 1:
			tileEntity.buttonStatus1 = !tileEntity.buttonStatus1;
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (int) 0, tileEntity.buttonStatus1));
			break;
		case 2:
			tileEntity.buttonStatus2 = !tileEntity.buttonStatus2;
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (int) 1, tileEntity.buttonStatus2));
			break;
		}
	}
}