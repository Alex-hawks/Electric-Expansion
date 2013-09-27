//package electricexpansion.client.gui;
//
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//
//import org.lwjgl.opengl.GL11;
//
//import universalelectricity.prefab.network.PacketManager;
//import cpw.mods.fml.common.network.PacketDispatcher;
//import electricexpansion.client.misc.TextureLocations;
//import electricexpansion.common.ElectricExpansion;
//import electricexpansion.common.cables.TileEntityLogisticsWire;
//
//public class GuiLogisticsWire extends GuiScreen
//{
//    private TileEntityLogisticsWire tileEntity;
//    
//    public GuiLogisticsWire(TileEntityLogisticsWire LogisticsWire)
//    {
//        this.tileEntity = LogisticsWire;
//    }
//    
//    public final int xSizeOfTexture = 176;
//    public final int ySizeOfTexture = 88;
//    
//    @Override
//    public void drawScreen(int x, int y, float f)
//    {
//        this.drawDefaultBackground();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.mc.func_110434_K().func_110577_a(TextureLocations.GUI_LOGISTICS);
//        int posX = (this.width - this.xSizeOfTexture) / 2;
//        int posY = (this.height - this.ySizeOfTexture) / 2;
//        this.drawTexturedModalRect(posX, posY, 0, 0, this.xSizeOfTexture, this.ySizeOfTexture);
//        this.fontRenderer.drawString("Logistics Wire", posX + this.xSizeOfTexture / 2 - 35, posY + 4, 4210752);
//        super.drawScreen(x, y, f);
//    }
//    
//    @Override
//    public void onGuiClosed()
//    {
//        super.onGuiClosed();
//        
//        PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (byte) 7, false));
//    }
//    
//    @Override
//    public void actionPerformed(GuiButton button)
//    {
//        switch (button.id)
//        {
//            case 0:
//                this.tileEntity.buttonStatus0 = !this.tileEntity.buttonStatus0;
//                PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (byte) -1, this.tileEntity.buttonStatus0));
//                break;
//            case 1:
//                this.tileEntity.buttonStatus1 = !this.tileEntity.buttonStatus1;
//                PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (byte) 0, this.tileEntity.buttonStatus1));
//                break;
//            case 2:
//                this.tileEntity.buttonStatus2 = !this.tileEntity.buttonStatus2;
//                PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, (byte) 1, this.tileEntity.buttonStatus2));
//                break;
//        }
//    }
//    
//    @Override
//    @SuppressWarnings("unchecked")
//    public void updateScreen()
//    {
//        super.updateScreen();
//        
//        this.buttonList.clear();
//        
//        int posX = (this.width - this.xSizeOfTexture) / 2;
//        int posY = (this.height - this.ySizeOfTexture) / 2;
//        
//        this.buttonList.add(new ButtonSwitch(0, posX + 13, posY + 15, 150, 16, "Output to World", this.tileEntity.buttonStatus0));
//        this.buttonList.add(new ButtonSwitch(1, posX + 13, posY + 38, 150, 16, "Output to RS Network", this.tileEntity.buttonStatus1));
//        this.buttonList.add(new ButtonSwitch(2, posX + 13, posY + 61, 150, 16, "Unused", this.tileEntity.buttonStatus2));
//        
//        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
//        {
//            this.mc.thePlayer.closeScreen();
//        }
//    }
//}