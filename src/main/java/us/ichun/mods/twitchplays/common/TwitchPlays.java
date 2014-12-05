package us.ichun.mods.twitchplays.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ichun.common.core.updateChecker.ModVersionChecker;
import ichun.common.core.updateChecker.ModVersionInfo;
import ichun.common.iChunUtil;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.ichun.mods.twitchplays.client.core.TickHandlerClient;

@Mod(modid = "TwitchPlays", name = "TwitchPlays",
        version = TwitchPlays.version,
        dependencies = "required-after:iChunUtil@[" + iChunUtil.versionMC +".1.2,)",
        acceptableRemoteVersions = "[" + iChunUtil.versionMC +".0.0," + iChunUtil.versionMC + ".1.0)"
)
public class TwitchPlays
{
        public static final String version = iChunUtil.versionMC + ".0.2";

        @Mod.Instance("TwitchPlays")
        public static TwitchPlays instance;

        private static final Logger logger = LogManager.getLogger("Tabula");

        @SideOnly(Side.CLIENT)
        public static TickHandlerClient tickHandlerClient;

        @Mod.EventHandler
        public void preLoad(FMLPreInitializationEvent event)
        {
                if(FMLCommonHandler.instance().getEffectiveSide().isServer())
                {
                        console("You're loading TwitchPlays on a server! This is a client-only mod!", true);
                        return;
                }

                init(event);
        }

        @SideOnly(Side.CLIENT)
        public void init(FMLPreInitializationEvent event)
        {
                tickHandlerClient = new TickHandlerClient();

                FMLCommonHandler.instance().bus().register(tickHandlerClient);
                MinecraftForge.EVENT_BUS.register(tickHandlerClient);

                ModVersionChecker.register_iChunMod(new ModVersionInfo("TwitchPlays", iChunUtil.versionOfMC, version, false));
        }

        public static void console(String s, boolean warning)
        {
                StringBuilder sb = new StringBuilder();
                logger.log(warning ? Level.WARN : Level.INFO, sb.append("[").append(version).append("] ").append(s).toString());
        }

}
