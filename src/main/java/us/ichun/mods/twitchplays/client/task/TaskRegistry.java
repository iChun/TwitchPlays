package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import us.ichun.mods.twitchplays.common.TwitchPlays;

import java.util.HashMap;

public class TaskRegistry
{
    private static HashMap<String, Class<? extends Task>> tasks = new HashMap<String, Class<? extends Task>>();

    public static void registerTask(String command, Class<? extends Task> taskClz)
    {
        if(tasks.containsKey(command))
        {
            TwitchPlays.console("Already contains command: " + command, true);
        }
        else
        {
            tasks.put(command, taskClz);
        }
    }

    public static boolean hasTask(String taskName)
    {
        String[] disabled = TwitchPlays.config.getString("disabledTasks").split(", *");
        for(String s : disabled)
        {
            if(taskName.equalsIgnoreCase(s))
            {
                return false;
            }
        }
        return tasks.containsKey(taskName);
    }

    public static Task createTask(WorldClient world, EntityPlayerSP player, String...args)
    {
        Class clz = tasks.get(args[0]);

        try
        {
            Task task = (Task)clz.getConstructor(WorldClient.class, EntityPlayerSP.class).newInstance(world, player);
            if(task.parse(args))
            {
                return task;
            }
        }
        catch(Exception e)
        {
        }

        return null;
    }

    static
    {
        registerTask("forward", TaskMovement.class);
        registerTask("fwd", TaskMovement.class);
        registerTask("f", TaskMovement.class);
        registerTask("w", TaskMovement.class);

        registerTask("back", TaskMovement.class);
        registerTask("bck", TaskMovement.class);
        registerTask("b", TaskMovement.class);
        registerTask("s", TaskMovement.class);

        registerTask("left", TaskMovement.class);
        registerTask("l", TaskMovement.class);
        registerTask("a", TaskMovement.class);

        registerTask("right", TaskMovement.class);
        registerTask("r", TaskMovement.class);
        registerTask("d", TaskMovement.class);

        registerTask("look", TaskLook.class);//look up,down,left,right
        registerTask("jump", TaskJump.class);

        registerTask("camera", TaskCamera.class);//camera up,down,left,right,distance 5-500
        registerTask("cam", TaskCamera.class);

        registerTask("mine", TaskMineBlock.class);

        registerTask("hotbar", TaskHotbar.class);//hotbar 1-9,next,prev,>,<

        registerTask("equip", TaskEquip.class);//equip <itemname> <meta>(minecraft:stick...etc)
        registerTask("hold", TaskEquip.class);

        registerTask("respawn", TaskRespawn.class);
        registerTask("swim", TaskSwim.class);//swim forward,back,left,right

        registerTask("q", TaskDrop.class);
        registerTask("drop", TaskDrop.class);

        registerTask("crouch", TaskToggleSneak.class);
        registerTask("sneak", TaskToggleSneak.class);

        registerTask("uncrouch", TaskUnSneak.class);
        registerTask("unsneak", TaskUnSneak.class);

        registerTask("closegui", TaskCloseGui.class);

        registerTask("place", TaskPlaceBlock.class);

        registerTask("craft", TaskCraft.class);//craft <itemname> (default 2x2 grid. Be within range [4x4x4] of a crafting table to use a 3x3 grid)

        //Op only tasks
        registerTask("cleartasks", TaskClearTasks.class);
        registerTask("endtask", TaskEndTask.class);

        registerTask("endsession", TaskEndSession.class);

        registerTask("togglethirdperson", TaskToggleThirdPerson.class);
        registerTask("toggleminicam", TaskShowMinicam.class);

        registerTask("twitchinput", TaskToggleTwitchInput.class);//ops,all

        registerTask("command", TaskCommand.class);//<normal ingame command>
    }

    //TODO mount/dismount, craft, smelt, interact, mine/attack/place/interact, etc, democracy/anarchy
}
