package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class TaskEquip extends Task {
    private String itemName;
    private int meta;

    public TaskEquip(WorldClient world, EntityPlayerSP player) {
        super(world, player);
    }

    @Override
    public boolean parse(String... args) {
        if (args.length == 2 || args.length == 3) {
            itemName = args[1];
            if (args.length == 3)
                meta = Integer.parseInt(args[2]);
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
                query.setItemDamage(meta);
                int slot = searchPlayerInventory(query);
                if (slot != -1) {
                    if (slot < 9)
                        player.inventory.currentItem = slot;
                    else {
                        ItemStack tempStack = player.getCurrentEquippedItem();
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, player.inventory.getStackInSlot(slot));
                        player.inventory.setInventorySlotContents(slot, tempStack);
                    }
                }
            }
        }
    }

    private int searchPlayerInventory(ItemStack stack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item != null && item.getItem() != null && item.getItem() == stack.getItem() && item.getItemDamage() == stack.getItemDamage())
                return i;
        }
        return -1;
    }
}
