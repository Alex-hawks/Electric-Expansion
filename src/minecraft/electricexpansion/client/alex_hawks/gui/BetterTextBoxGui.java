package electricexpansion.client.alex_hawks.gui;

import org.lwjgl.input.Keyboard;


import net.minecraft.src.*;

public class BetterTextBoxGui extends Gui
{
	public BetterTextBoxGui(GuiScreen guiscreen, FontRenderer fontrenderer, int x, int y, int width, int height, String text)
    {
		isFocused = false;
        isEnabled = true;
        parentGuiScreen = guiscreen;
        fontRenderer = fontrenderer;
        xPos = x;
        yPos = y;
        this.width = width;
        this.height = height;
        setText(text);
        
        allowedcharacters = "1, 2, 3, 4, 5, 6, 7, 8, 9, 0";
    }

    public void setText(String s)
    {
        text = s;
    }

    public String getText()
    {
        return text;
    }

    public void updateCursorCounter()
    {
        cursorCounter++;
    }

    public void textboxKeyTyped(char c, int keycode)
    {
        if(!isEnabled || !isFocused)
        {
            return;
        }
        /*if(c == '\t')//tab
        {
            parentGuiScreen.selectNextField();
        }*/
        if(c == '\026')//paste
        {
            String s = GuiScreen.getClipboardString();
            if(s == null || s == "")
            {
                return;
            }
            for(int i = 0; i < s.length(); i++)
            {
            	if(text.length() == maxStringLength)
            	{
            		return;
            	}
            	
            	char tc = s.charAt(i);
            	
            	if(allowedcharacters.indexOf(tc) >= 0)
            	{
            		text+=tc;
            	}
            }
            
        }
        if(keycode == Keyboard.KEY_BACK && text.length() > 0)
        {
            text = text.substring(0, text.length() - 1);
        }
        if(allowedcharacters.indexOf(c) >= 0 && (text.length() < maxStringLength || maxStringLength == 0))
        {
            text += c;
        }
    }

    public void mouseClicked(int x, int y, int button)
    {
        if(isEnabled && 
        		x >= xPos && x < xPos + width && 
        		y >= yPos && y < yPos + height)//on the box
        {
        	setFocused(true);
        }
        else
        {
        	setFocused(false);
        }
    }

    public void setFocused(boolean focused)
    {
        needsFocus = focused;
    }
    
    private void updateFocus()
    {
    	if(isFocused != needsFocus)
    	{
    		if(needsFocus && !isFocused)
            {
                cursorCounter = 0;
            }
            isFocused = needsFocus;
    	}
    }

    public void drawTextBox()
    {
    	updateFocus();
        drawRect(xPos - 1, yPos - 1, xPos + width + 1, yPos + height + 1, 0xffa0a0a0);
        drawRect(xPos, yPos, xPos + width, yPos + height, 0xff000000);
        if(isEnabled)
        {
            boolean flag = isFocused && (cursorCounter / 6) % 2 == 0;
            drawString(fontRenderer, (new StringBuilder()).append(text).append(flag ? "_" : "").toString(), xPos + 4, yPos + (height - 8) / 2, 0xe0e0e0);
        } else
        {
            drawString(fontRenderer, text, xPos + 4, yPos + (height - 8) / 2, 0x707070);
        }
    }

    public void setMaxStringLength(int i)
    {
        maxStringLength = i;
    }
    
    public void setAllowedCharacters(String s)
    {
    	if(s == null)
    	{
    		s = ChatAllowedCharacters.allowedCharacters;
    	}
    	else
    	{
    		allowedcharacters = s;
    	}
    }

    public void setReturnButton(GuiButton guibutton)
    {
    	returnbutton = guibutton;
    }
    
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    public boolean isFocused;
    private boolean needsFocus;
    public boolean isEnabled;
    private GuiScreen parentGuiScreen;
    
    private GuiButton returnbutton;
	private String allowedcharacters;
}
