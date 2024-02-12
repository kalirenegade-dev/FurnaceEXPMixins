package FurnaceEXPMixins;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import FurnaceEXPMixins.proxy.CommonProxy;
import FurnaceEXPMixins.handlers.ModRegistry;

@Mod(modid = FurnaceEXPMixins.MODID, version = FurnaceEXPMixins.VERSION, name = FurnaceEXPMixins.NAME, acceptableRemoteVersions = "*")
public class FurnaceEXPMixins
{
    public static final String MODID = "furnaceexpmixins";
    public static final String VERSION = "2.0.0";
    public static final String NAME = "FurnaceEXPMixins";
    public static final Logger LOGGER = LogManager.getLogger();

    @SidedProxy(clientSide = "FurnaceEXPMixins.proxy.ClientProxy", serverSide = "FurnaceEXPMixins.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(MODID)
	public static FurnaceEXPMixins instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModRegistry.init();
        FurnaceEXPMixins.PROXY.preInit();

        }

}