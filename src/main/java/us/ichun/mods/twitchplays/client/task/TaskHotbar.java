package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskHotbar extends Task {
    private int slotChange;
    private boolean numMode;

    public TaskHotbar(WorldClient world, EntityPlayerSP player) {
        super(world, player);
    }

    @Override
    public boolean parse(String... args) {
        if (args.length == 2) {
            slotChange = (args[1].equals("next") || args[1].equals("n") || args[1].equals(">")) ? -1 : (args[1].equals("prev") || args[1].equals("p") || args[1].equals("<")) ? 1 : 0;
            if (slotChange == 0) {
                slotChange = Integer.parseInt(args[1]);
                numMode = true;
            }
            return !numMode ? slotChange != 0 : slotChange != player.inventory.currentItem;
        }
        return false;
    }

    @Override
    public int maxActiveTime() {
        return 1;
    }

    @Override
    protected void update() {
        if (numMode) {
            --slotChange;
            if (slotChange < 0) slotChange = 0; else if (slotChange > 8) slotChange = 8;
            player.inventory.currentItem = slotChange;
        } else
            player.inventory.changeCurrentItem(slotChange);
    }
}
