package us.ichun.mods.twitchplays.client.task;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovingObjectPosition;

public class TaskMine extends Task {
    private int maxTime;
    private int blockX, blockY, blockZ, prevX, prevY, prevZ;

    public TaskMine(WorldClient world, EntityPlayerSP player) {
        super(world, player);
        maxTime = 1;
    }

    @Override
    public void init() {
        MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            blockX = mop.blockX;
            blockY = mop.blockY;
            blockZ = mop.blockZ;
        }
    }

    @Override
    public boolean parse(String... args) {
        return args.length == 1;
    }

    @Override
    public int maxActiveTime() {
        return maxTime; // would like a better way of determining task time
    }

    @Override
    protected void update() {
        MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            this.prevX = this.blockX;
            this.prevY = this.blockY;
            this.prevZ = this.blockZ;
            this.blockX = mop.blockX;
            this.blockY = mop.blockY;
            this.blockZ = mop.blockZ;
            Block block = world.getBlock(this.blockX, this.blockY, this.blockZ);
            if (block.getBlockHardness(world, this.blockX, this.blockY, this.blockZ) >= 0 && !block.isAir(world, this.blockX, this.blockY, this.blockZ) && (this.blockX == this.prevX && this.blockY == this.prevY && this.blockZ == this.prevZ)) { // So they don't ry to break an unbreakable block
                Minecraft.getMinecraft().playerController.onPlayerDamageBlock(this.blockX, this.blockY, this.blockZ, mop.sideHit);
                if (player.isCurrentToolAdventureModeExempt(this.blockX, this.blockY, this.blockZ)) {
                    maxTime++;
                    Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = true;
                }
            }
        }
    }

    @Override
    public void terminate() {
        Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
    }
}
