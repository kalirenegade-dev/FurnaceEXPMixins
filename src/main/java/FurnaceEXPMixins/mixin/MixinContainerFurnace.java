package FurnaceEXPMixins.mixin;

import FurnaceEXPMixins.interfaces.MixinContainerFurnaceInterface;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ContainerFurnace.class)
public abstract class MixinContainerFurnace implements MixinContainerFurnaceInterface {

    @Final
    @Shadow
    private IInventory tileFurnace;

    @Override
    public IInventory getTileFurnace(){
        return tileFurnace;
    }
    
}
