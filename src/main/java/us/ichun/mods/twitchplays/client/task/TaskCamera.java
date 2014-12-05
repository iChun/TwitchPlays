package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;
import us.ichun.mods.twitchplays.common.TwitchPlays;

public class TaskCamera extends Task
{
    public int moveType; //UDLR;

    public TaskCamera(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
    }

    @Override
    public void init()
    {
        switch(moveType)
        {
            case 1:
            {
                TwitchPlays.tickHandlerClient.targetPitch += 90F;
                break;
            }
            case 2:
            {
                TwitchPlays.tickHandlerClient.targetPitch -= 90F;
                break;
            }
            case 3:
            {
                TwitchPlays.tickHandlerClient.targetYaw -= 90F;
                break;
            }
            case 4:
            {
                TwitchPlays.tickHandlerClient.targetYaw += 90F;
                break;
            }
        }
        TwitchPlays.tickHandlerClient.targetPitch = MathHelper.clamp_float(TwitchPlays.tickHandlerClient.targetPitch, -90F, 90F);
        TwitchPlays.tickHandlerClient.turnTime = TwitchPlays.tickHandlerClient.TURN_TIME;
        TwitchPlays.tickHandlerClient.oriYaw = TwitchPlays.tickHandlerClient.camYaw;
        TwitchPlays.tickHandlerClient.oriPitch = TwitchPlays.tickHandlerClient.camPitch;
    }

    @Override
    public boolean parse(String... args)
    {
        if(args.length == 2)
        {
            moveType = (args[1].equals("up") || args[1].equals("u")) ? 1 : (args[1].equals("down") || args[1].equals("d")) ? 2 : (args[1].equals("left") || args[1].equals("l")) ? 3 : (args[1].equals("right") || args[1].equals("r")) ? 4 : 0;
            return moveType != 0;
        }
        return false;
    }

    @Override
    public int maxActiveTime()
    {
        return 0;
    }

    @Override
    protected void update()
    {
    }
}
