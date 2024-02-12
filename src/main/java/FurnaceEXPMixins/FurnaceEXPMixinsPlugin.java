package FurnaceEXPMixins;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fermiumbooter.FermiumRegistryAPI;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.launch.MixinBootstrap;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import FurnaceEXPMixins.handlers.ForgeConfigHandler;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(-5000)
public class FurnaceEXPMixinsPlugin implements IFMLLoadingPlugin {

	private static final Map<String, String> earlyMap = setupEarlyMap();

	private static Map<String, String> setupEarlyMap() {
		Map<String, String> map = new HashMap<>();
		map.put("Enable Furnace EXP Storage", "mixins.furnaceexpmixins.core.entitylist.json");
		return Collections.unmodifiableMap(map);
	}

	private static final Map<String, String> lateMap = setupLateMap();

	private static Map<String, String> setupLateMap() {
		Map<String, String> map = new HashMap<>();
		return Collections.unmodifiableMap(map);
	}

	public FurnaceEXPMixinsPlugin() {
		MixinBootstrap.init();

		// Log a message indicating that the Mixin loader is initialized
		FurnaceEXPMixins.LOGGER.log(Level.INFO, "FurnaceEXPMixinsPlugin initialized");

		FurnaceEXPMixins.LOGGER.log(Level.INFO, "FurnaceEXPMixins Early Enqueue Start");
		for (Map.Entry<String, String> entry : earlyMap.entrySet()) {
			FurnaceEXPMixins.LOGGER.log(Level.INFO, "FurnaceEXPMixins Early Enqueue: " + entry.getKey());
			FermiumRegistryAPI.enqueueMixin(false, entry.getValue(), () -> ForgeConfigHandler.getBoolean(entry.getKey()));
		}

		FurnaceEXPMixins.LOGGER.log(Level.INFO, "FurnaceEXPMixins Late Enqueue Start");
		for (Map.Entry<String, String> entry : lateMap.entrySet()) {
			FurnaceEXPMixins.LOGGER.log(Level.INFO, "FurnaceEXPMixins Late Enqueue: " + entry.getKey());
			FermiumRegistryAPI.enqueueMixin(true, entry.getValue(), () -> ForgeConfigHandler.getBoolean(entry.getKey()));
		}
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
