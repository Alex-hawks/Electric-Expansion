package electricexpansion.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import electricexpansion.api.tile.EnergyCoordinates;
import electricexpansion.client.misc.TextureLocations;
import electricexpansion.common.ElectricExpansion;
import electricexpansion.common.containers.ContainerDistribution;
import electricexpansion.common.tile.TileEntityQuantumBatteryBox;

@SideOnly(Side.CLIENT)
public class GuiQuantumBatteryBox extends GuiContainer
{
    private TileEntityQuantumBatteryBox tileEntity;
    
    private GuiTextField textFieldX;
    private GuiTextField textFieldY;
    private GuiTextField textFieldZ;
    
    private int containerWidth;
    private int containerHeight;
    
    private byte frequency;
    
    public GuiQuantumBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityQuantumBatteryBox tileEntity)
    {
        super(new ContainerDistribution(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();
        this.xSize = 235;
        int var1 = (this.width - this.xSize) / 2;
        int var2 = (this.height - this.ySize) / 2;
        //this.textFieldFrequency = new GuiTextField(this.fontRenderer, 6, 45, 49, 13);
        //this.textFieldFrequency.setMaxStringLength(3);
        //this.textFieldFrequency.setText(this.tileEntity.getFrequency() + "");
        
        this.textFieldX = new GuiTextField(this.fontRenderer, 150, 5, 45, 11);
        this.textFieldX.setMaxStringLength(5);
        this.textFieldX.setText(this.tileEntity.getFrequency() + "");
        
        this.textFieldY = new GuiTextField(this.fontRenderer, 150, 19, 45, 11);
        this.textFieldY.setMaxStringLength(5);
        this.textFieldY.setText(this.tileEntity.getFrequency() + "");
        
        this.textFieldZ = new GuiTextField(this.fontRenderer, 150, 33, 45, 11);
        this.textFieldZ.setMaxStringLength(5);
        this.textFieldZ.setText(this.tileEntity.getFrequency() + "");
        
        this.buttonList.clear();
        
        this.buttonList.add(new GuiButton(0, var1 + 6, var2 + 60, 50, 20, "Set"));
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.textFieldX.drawTextBox();
        this.textFieldY.drawTextBox();
        this.textFieldZ.drawTextBox();
        
        String displayJoules = ElectricityDisplay.getDisplayShort(this.tileEntity.getJoulesForDisplay(), ElectricUnit.JOULES);
        
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 22, 6, 4210752);
        //this.fontRenderer.drawString("Current Frequency: " + this.tileEntity.getFrequency(), 10, 20, 4210752);
        this.fontRenderer.drawString("Current Storage: " + displayJoules, 10, 30, 4210752);
        if (this.tileEntity.getOwningPlayer() != null)
        {
            this.fontRenderer.drawString("Player: " + this.tileEntity.getOwningPlayer(), 40, 66, 4210752);
        }
        else
        {
            this.fontRenderer.drawString("I have no owner. BUG!", 40, 66, 4210752);
        }
    }
    
    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TextureLocations.GUI_WPT);
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
        
        if (this.tileEntity.getJoulesForDisplay() > 0)
        {
            int scale = (int) (this.tileEntity.getJoulesForDisplay() / this.tileEntity.getMaxEnergyStored() * 72);
            this.drawTexturedModalRect(this.containerWidth + 70, this.containerHeight + 51, 0, 166, scale, 5);
        }
    }
    
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.textFieldX.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.textFieldY.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.textFieldZ.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
    }
    
    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        if (par2 == Keyboard.KEY_RETURN || par2 == Keyboard.KEY_NUMPADENTER)
        {
            try
            {
                this.tileEntity.setFrequency(new EnergyCoordinates(Float.parseFloat(this.textFieldX.getText()), Float.parseFloat(this.textFieldY.getText()), Float.parseFloat(this.textFieldZ.getText())));
            }
            catch (Exception e) { }
        }
        
        if (this.textFieldX.isFocused())
            this.textFieldX.textboxKeyTyped(par1, par2);
        else if (this.textFieldY.isFocused())
            this.textFieldY.textboxKeyTyped(par1, par2);
        else if (this.textFieldZ.isFocused())
            this.textFieldZ.textboxKeyTyped(par1, par2);
    }
    
    @Override
    public void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 0:
                PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ElectricExpansion.CHANNEL, this.tileEntity, this.frequency));
                break;
            default:
        }
    }
    
    @Override
    public void updateScreen()
    {
        if (!this.textFieldX.isFocused())
        {
            this.textFieldX.setText(this.tileEntity.getFrequency().x + "");
        }
        
        if (!this.textFieldY.isFocused())
        {
            this.textFieldY.setText(this.tileEntity.getFrequency().y + "");
        }
        
        if (!this.textFieldZ.isFocused())
        {
            this.textFieldZ.setText(this.tileEntity.getFrequency().z + "");
        }
        
    }
}