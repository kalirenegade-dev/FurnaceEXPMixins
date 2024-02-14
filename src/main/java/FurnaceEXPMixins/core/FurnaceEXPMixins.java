package FurnaceEXPMixins.core;

import FurnaceEXPMixins.events.MixinFurnaceEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FurnaceEXPMixins.MODID, version = FurnaceEXPMixins.VERSION, name = FurnaceEXPMixins.NAME, acceptableRemoteVersions = "*")
public class FurnaceEXPMixins
{
    public static final String MODID = "furnaceexpmixins";
    public static final String VERSION = "1.0.2";
    public static final String NAME = "FurnaceEXPMixins";

	@Instance(MODID)
	public static FurnaceEXPMixins instance;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(MixinFurnaceEvents.class);
    }

}