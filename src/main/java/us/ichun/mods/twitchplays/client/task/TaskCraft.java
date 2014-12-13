package us.ichun.mods.twitchplays.client.task;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class TaskCraft extends Task {
    private String itemName;

    public TaskCraft(WorldClient world, EntityPlayerSP player) {
        super(world, player);
    }

    @Override
    public boolean parse(String... args) {
        if (args.length == 2 || args.length == 3) {
            itemName = args[1];
            if(itemName.indexOf(":") == -1)
            {
                itemName = "minecraft:" + itemName;
            }
            return true;
        }
        return false;
    }

    @Override
    public int maxActiveTime() {
        return 0;
    }

    @Override
    protected void update() {
        String[] array = itemName.split(":");
        if (array.length == 2) {
            String modid = array[0];
            String name = array[1];
            ItemStack query = GameRegistry.findItemStack(modid, name, 1);
            if (query != null && query.getItem() != null) {

                int gridSize = 2;

                int x = (int)Math.floor(player.posX);
                int y = (int)Math.floor(player.boundingBox.minY);
                int z = (int)Math.floor(player.posZ);

                for(int i = x - 4; i <= x + 4; i++)
                {
                    for(int j = y - 4; j <= y + 4; j++)
                    {
                        for(int k = z - 4; k <= z + 4; k++)
                        {
                            Block b = world.getBlock(i, j, k);
                            if(b.equals(Blocks.crafting_table))
                            {
                                MovingObjectPosition mop = world.rayTraceBlocks(Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ), Vec3.createVectorHelper(i + 0.5D, j + 0.5D, k + 0.5D));
                                if(mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mop.blockX == x && mop.blockY == y && mop.blockY == z)
                                {
                                    gridSize = 3;
                                    break;
                                }
                            }
                        }
                    }
                }

                ItemStack result = null;
                ArrayList<ItemStack> required = new ArrayList<ItemStack>();

                List recipes = CraftingManager.getInstance().getRecipeList();
                for(Object o : recipes)
                {
                    if(o instanceof ShapedRecipes)
                    {
                        ShapedRecipes recipe = (ShapedRecipes)o;
                        ItemStack res = recipe.getRecipeOutput();
                        if(res != null && query.getItem() == res.getItem() && query.getItemDamage() == res.getItemDamage() && ItemStack.areItemStackTagsEqual(query, res) && recipe.recipeWidth <= gridSize && recipe.recipeHeight <= gridSize)
                        {
                            result = res;
                            for(ItemStack is : recipe.recipeItems)
                            {
                                required.add(is);
                            }
                        }
                    }
                    else if(o instanceof ShapedOreRecipe)
                    {
                        ShapedOreRecipe recipe = (ShapedOreRecipe)o;
                        ItemStack res = recipe.getRecipeOutput();
                        if(res != null && query.getItem() == res.getItem() && query.getItemDamage() == res.getItemDamage() && ItemStack.areItemStackTagsEqual(query, res) && (Integer)ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, "width") <= gridSize && (Integer)ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, "height") <= gridSize)
                        {
                            result = res;
                            for(Object is : (Object[])ObfuscationReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, "input"))
                            {
                                if(is instanceof ItemStack)
                                {
                                    required.add((ItemStack)is);
                                }
                            }
                        }
                    }
                    else if(o instanceof ShapelessRecipes)
                    {
                        ShapelessRecipes recipe = (ShapelessRecipes)o;
                        ItemStack res = recipe.getRecipeOutput();
                        if(res != null && query.getItem() == res.getItem() && query.getItemDamage() == res.getItemDamage() && ItemStack.areItemStackTagsEqual(query, res) && recipe.recipeItems.size() < gridSize * gridSize)
                        {
                            result = res;
                            for(Object is : recipe.recipeItems)
                            {
                                if(is instanceof ItemStack)
                                {
                                    required.add((ItemStack)is);
                                }
                            }
                        }
                    }
                    else if(o instanceof ShapelessOreRecipe)
                    {
                        ShapelessOreRecipe recipe = (ShapelessOreRecipe)o;
                        ItemStack res = recipe.getRecipeOutput();
                        if(res != null && query.getItem() == res.getItem() && query.getItemDamage() == res.getItemDamage() && ItemStack.areItemStackTagsEqual(query, res) && ((ArrayList<Object>)ObfuscationReflectionHelper.getPrivateValue(ShapelessOreRecipe.class, recipe, "input")).size() < gridSize * gridSize)
                        {
                            result = res;
                            for(Object is : ((ArrayList<Object>)ObfuscationReflectionHelper.getPrivateValue(ShapelessOreRecipe.class, recipe, "input")))
                            {
                                if(is instanceof ItemStack)
                                {
                                    required.add((ItemStack)is);
                                }
                            }
                        }
                    }
                }

                if(result != null)
                {
                    ArrayList<ItemStack> invCheck = new ArrayList<ItemStack>(required);
                    ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                        ItemStack item = player.inventory.getStackInSlot(i);
                        if (item != null && item.getItem() != null)
                        {
                            items.add(item);
                        }
                    }
                    for(int i = invCheck.size() - 1; i >= 0; i--)
                    {
                        ItemStack is = invCheck.get(i);
                        for(ItemStack is1 : items)
                        {
                            if (is1.getItem() == is.getItem() && (is.getItemDamage() == Short.MAX_VALUE || is1.getItemDamage() == is.getItemDamage()))
                            {
                                invCheck.remove(is);
                                break;
                            }
                        }
                    }

                    if(invCheck.isEmpty())
                    {
                        for(ItemStack is : required)
                        {
                            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                                ItemStack item = player.inventory.getStackInSlot(i);
                                if (item != null && item.getItem() != null && item.getItem() == is.getItem() && (is.getItemDamage() == Short.MAX_VALUE || item.getItemDamage() == is.getItemDamage()))
                                {
                                    item.stackSize--;
                                    if(item.stackSize == 0)
                                    {
                                        player.inventory.setInventorySlotContents(i, null);
                                    }
                                    break;
                                }
                            }
                        }
                        if(!player.inventory.addItemStackToInventory(result.copy()))
                        {
                            player.dropPlayerItemWithRandomChoice(result.copy(), false);
                        }
                    }
                 }
            }
        }
    }

    @Override
    public String getName()
    {
        return "craft";
    }
}
