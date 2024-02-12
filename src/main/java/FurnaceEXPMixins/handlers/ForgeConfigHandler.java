package FurnaceEXPMixins.handlers;

import com.google.common.collect.BiMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.Level;
import FurnaceEXPMixins.FurnaceEXPMixins;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Config(modid = FurnaceEXPMixins.MODID)
public class ForgeConfigHandler {

	
	@Config.Comment("Additional Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

	@Config.Comment("Additional Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();

	@Config.Comment("Enable/Disable Tweaks and Patches")
	@Config.Name("Toggle Mixins")
	public static final MixinConfig mixinConfig = new MixinConfig();

	@SuppressWarnings("unused")
	public static class MixinConfig {

		@Config.Comment("Allowes furnaces to store xp when used with a hopper")
		@Config.Name("Enable Furnace EXP Storage")
		@Config.RequiresMcRestart
		public boolean chunkEntityListUpdate = true;

	}

	public static class ServerConfig {

	}

	public static class ClientConfig {


	}


	@Mod.EventBusSubscriber(modid = FurnaceEXPMixins.MODID)
	private static class EventHandler{
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(FurnaceEXPMixins.MODID)) {

				ConfigManager.sync(FurnaceEXPMixins.MODID, Config.Type.INSTANCE);
				refreshValues();
			}
		}

		//Include remaining ISeeDragons patches
		private static void refreshValues() {

		}

	}

	//This is jank, but easier than setting up a whole custom GUI config
	private static File configFile;
	private static String configBooleanString = "";

	public static boolean getBoolean(String name) {
		if(configFile==null) {
			configFile = new File("config", FurnaceEXPMixins.MODID + ".cfg");
			if(configFile.exists() && configFile.isFile()) {
				try (Stream<String> stream = Files.lines(configFile.toPath())) {
					configBooleanString = stream.filter(s -> s.trim().startsWith("B:")).collect(Collectors.joining());
				}
				catch(Exception ex) {
					FurnaceEXPMixins.LOGGER.log(Level.ERROR, "Failed to parse FurnaceEXPMixins config: " + ex);
				}
			}
		}
		//If config is not generated or missing entries, don't enable injection on first run
		return configBooleanString.contains("B:\"" + name + "\"=true");
	}
}