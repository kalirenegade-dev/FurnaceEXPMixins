package furnaceexpmixins.core;

import furnaceexpmixins.events.MixinFurnaceEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = furnaceexpmixins.MODID, version = furnaceexpmixins.VERSION, name = furnaceexpmixins.NAME, acceptableRemoteVersions = "*")
public class furnaceexpmixins
{
    public static final String MODID = "furnaceexpmixins";
    public static final String VERSION = "2.0.0";
    public static final String NAME = "furnaceexpmixins";

	@Instance(MODID)
	public static furnaceexpmixins instance;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(MixinFurnaceEvents.class);
    }

}