package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class TaskDrop extends Task {
    private boolean all;
    public TaskDrop(WorldClient world, EntityPlayerSP player) {
        super(world, player);
    }

    @Override
    public boolean parse(String... args) {
        if (args.length == 2 && (args[1].equals("all") || args[1].equals("stack")))
            all = true;
        return true;
    }

    @Override
    public int maxActiveTime() {
        return 0;
    }

    @Override
    protected void update() {
        player.dropOneItem(all);
    }

    @Override
    public String getName()
    {
        return "drop";
    }
}
