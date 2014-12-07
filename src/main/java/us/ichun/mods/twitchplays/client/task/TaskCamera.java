package us.ichun.mods.twitchplays.client.task;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;
import us.ichun.mods.twitchplays.common.TwitchPlays;

public class TaskCamera extends Task
{
    public int moveType; //UDLR;

    public int camDist;
    public int camSize;

    public TaskCamera(WorldClient world, EntityPlayerSP player)
    {
        super(world, player);
        camDist = -1;
        camSize = -1;
    }

    @Override
    public void init()
    {
        if(moveType > 0)
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
        else if(camDist != -1)
        {
            TwitchPlays.config.get("minicamDistance").set(camDist);
            TwitchPlays.config.save();
        }
        else
        {
            TwitchPlays.config.get("minicamSize").set(camDist);
            TwitchPlays.config.save();
        }
    }

    @Override
    public boolean requiresOp(String...args)
    {
        return args.length == 3 && args[1].equals("size");
    }

    @Override
    public boolean bypassOrder(String...args) { return args.length == 3 && (args[1].equals("dist") || args[1].equals("distance") || args[1].equals("size")); };

    @Override
    public boolean parse(String... args)
    {
        if(args.length == 2)
        {
            moveType = (args[1].equals("up") || args[1].equals("u")) ? 1 : (args[1].equals("down") || args[1].equals("d")) ? 2 : (args[1].equals("left") || args[1].equals("l")) ? 3 : (args[1].equals("right") || args[1].equals("r")) ? 4 : 0;
            return moveType != 0;
        }
        else if(args.length == 3)
        {
            if((args[1].equals("dist") || args[1].equals("distance")))
            {
                try
                {
                    camDist = MathHelper.clamp_int(Integer.parseInt(args[2]), 5, 500);
                    return true;
                }
                catch(NumberFormatException e)
                {
                    return false;
                }
            }
            else if(args[1].equals("size"))
            {
                try
                {
                    camSize = MathHelper.clamp_int(Integer.parseInt(args[2]), 5, 90);
                    return true;
                }
                catch(NumberFormatException e)
                {
                    return false;
                }
            }
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

    @Override
    public String getName()
    {
        return "camera " + (moveType == 1 ? "up" : moveType == 2 ? "down" : moveType == 3 ? "left" : "right");
    }
}
