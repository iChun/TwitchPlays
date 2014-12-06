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

        registerTask("look", TaskLook.class);
        registerTask("jump", TaskJump.class);

        registerTask("camera", TaskCamera.class);
        registerTask("cam", TaskCamera.class);

        registerTask("togglethirdperson", TaskToggleThirdPerson.class);
        registerTask("toggleminicam", TaskShowMinicam.class);

        registerTask("mine", TaskMineBlock.class);

        registerTask("hotbar", TaskHotbar.class);

        registerTask("equip", TaskEquip.class);
        registerTask("hold", TaskEquip.class);
    }

    //TODO mount/dismount, craft, smelt, interact, mine/attack/place/interact, respawn, equip, drop, etc, democracy/anarchy, <task><no>

}
