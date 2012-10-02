package electricexpansion.mattredsox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.*;
public class CraftingEtcher

{
/** The static instance of this class */
private static final CraftingEtcher instance = new CraftingEtcher();
/** A list of all the recipes added */
private List recipes = new ArrayList();
/**
         * Returns the static instance of this class
         */
public static final CraftingEtcher getInstance()
{
         return instance;
}
private CraftingEtcher()
{
         Collections.sort(this.recipes, new RecipeSorterEtcher(this));
         System.out.println(this.recipes.size() + " Lego recipes");
}
/**
         * returns the List<> of all recipes
         */
public List getRecipeList()
{
         return this.recipes;
}
void addRecipe(ItemStack itemstack, Object ... inputArray)
{
         String var3 = "";
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;
         int var9;
         if (inputArray[var4] instanceof String[])
         {
                 String[] var7 = (String[])((String[])inputArray[var4++]);
                 String[] var8 = var7;
                 var9 = var7.length;
                 for (int var10 = 0; var10 < var9; ++var10)
                 {
                         String var11 = var8[var10];
                         ++var6;
                         var5 = var11.length();
                         var3 = var3 + var11;
                 }
         }
         else
         {
                 while (inputArray[var4] instanceof String)
                 {
                         String var13 = (String)inputArray[var4++];
                         ++var6;
                         var5 = var13.length();
                         var3 = var3 + var13;
                 }
         }
         HashMap var14;
         for (var14 = new HashMap(); var4 < inputArray.length; var4 += 2)
         {
                 Character var16 = (Character)inputArray[var4];
                 ItemStack var17 = null;
                 if (inputArray[var4 + 1] instanceof Item)
                 {
                         var17 = new ItemStack((Item)inputArray[var4 + 1]);
                 }
                 else if (inputArray[var4 + 1] instanceof Block)
                 {
                         var17 = new ItemStack((Block)inputArray[var4 + 1], 1, -1);
                 }
                 else if (inputArray[var4 + 1] instanceof ItemStack)
                 {
                         var17 = (ItemStack)inputArray[var4 + 1];
                 }
                 var14.put(var16, var17);
         }
         ItemStack[] var15 = new ItemStack[var5 * var6];
         for (var9 = 0; var9 < var5 * var6; ++var9)
         {
                 char var18 = var3.charAt(var9);
                 if (var14.containsKey(Character.valueOf(var18)))
                 {
                         var15[var9] = ((ItemStack)var14.get(Character.valueOf(var18))).copy();
                 }
                 else
                 {
                         var15[var9] = null;
                 }
         }
         this.recipes.add(new ShapedRecipes(var5, var6, var15, itemstack));
}
void addShapelessRecipe(ItemStack itemstack, Object ... inputArray)
{
         ArrayList var3 = new ArrayList();
         Object[] var4 = inputArray;
         int var5 = inputArray.length;
         for (int var6 = 0; var6 < var5; ++var6)
         {
                 Object var7 = var4[var6];
                 if (var7 instanceof ItemStack)
                 {
                         var3.add(((ItemStack)var7).copy());
                 }
                 else if (var7 instanceof Item)
                 {
                         var3.add(new ItemStack((Item)var7));
                 }
                 else
                 {
                         if (!(var7 instanceof Block))
                         {
                                 throw new RuntimeException("Invalid shapeless recipy!");
                         }
                         var3.add(new ItemStack((Block)var7));
                 }
         }
         this.recipes.add(new ShapelessRecipes(itemstack, var3));
}
public ItemStack findMatchingRecipe(InventoryCrafting creafting)
{
         int var2 = 0;
         ItemStack var3 = null;
         ItemStack var4 = null;
         for (int var5 = 0; var5 < creafting.getSizeInventory(); ++var5)
         {
                 ItemStack var6 = creafting.getStackInSlot(var5);
                 if (var6 != null)
                 {
                         if (var2 == 0)
                         {
                                 var3 = var6;
                         }
                         if (var2 == 1)
                         {
                                 var4 = var6;
                         }
                         ++var2;
                 }
         }
         if (var2 == 2 && var3.itemID == var4.itemID && var3.stackSize == 1 && var4.stackSize == 1 && Item.itemsList[var3.itemID].isDamageable())
         {
                 Item var10 = Item.itemsList[var3.itemID];
                 int var12 = var10.getMaxDamage() - var3.getItemDamageForDisplay();
                 int var7 = var10.getMaxDamage() - var4.getItemDamageForDisplay();
                 int var8 = var12 + var7 + var10.getMaxDamage() * 10 / 100;
                 int var9 = var10.getMaxDamage() - var8;
                 if (var9 < 0)
                 {
                         var9 = 0;
                 }
                 return new ItemStack(var3.itemID, 1, var9);
         }
         else
         {
                 Iterator var11 = this.recipes.iterator();
                 IRecipe var13;
                 do
                 {
                         if (!var11.hasNext())
                         {
                                 return null;
                         }
                         var13 = (IRecipe)var11.next();
                 }
                 while (!var13.matches(creafting));
                 return var13.getCraftingResult(creafting);
         }
}
}