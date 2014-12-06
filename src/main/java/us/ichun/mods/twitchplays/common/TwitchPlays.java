package us.ichun.mods.twitchplays.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ichun.common.core.config.Config;
import ichun.common.core.config.ConfigHandler;
import ichun.common.core.config.IConfigUser;
import ichun.common.core.updateChecker.ModVersionChecker;
import ichun.common.core.updateChecker.ModVersionInfo;
import ichun.common.iChunUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import us.ichun.mods.twitchplays.client.core.TickHandlerClient;

@Mod(modid = "TwitchPlays", name = "TwitchPlays",
        version = TwitchPlays.version,
        dependencies = "required-after:iChunUtil@[" + iChunUtil.versionMC +".1.3,)",
        acceptableRemoteVersions = "[" + iChunUtil.versionMC +".0.0," + iChunUtil.versionMC + ".1.0)"
)
public class TwitchPlays
        implements IConfigUser
{
        public static final String version = iChunUtil.versionMC + ".0.0";

        @Mod.Instance("TwitchPlays")
        public static TwitchPlays instance;

        private static final Logger logger = LogManager.getLogger("TwitchPlays");

        public static Config config;

        @SideOnly(Side.CLIENT)
        public static TickHandlerClient tickHandlerClient;

        @Override
        public boolean onConfigChange(Config cfg, Property prop)
        {
                return true;
        }

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
                config = ConfigHandler.createConfig(event.getSuggestedConfigurationFile(), "twitchplays", "Twitch Plays", logger, instance);

                config.setCurrentCategory("general", "ichun.config.cat.general.name", "ichun.config.cat.general.comment");
                config.createIntBoolProperty("minicam", "twitchplays.config.prop.minicam.name", "twitchplays.config.prop.minicam.comment", true, false, true);
                config.createIntProperty("minicamSize", "twitchplays.config.prop.minicamSize.name", "twitchplays.config.prop.minicamSize.comment", true, false, 25, 5, 90);
                config.createIntProperty("minicamDistance", "twitchplays.config.prop.minicamDistance.name", "twitchplays.config.prop.minicamDistance.comment", true, false, 50, 5, 500);
                config.createIntBoolProperty("allowTwitchStaff", "twitchplays.config.prop.allowTwitchStaff.name", "twitchplays.config.prop.allowTwitchStaff.comment", true, false, false);

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
