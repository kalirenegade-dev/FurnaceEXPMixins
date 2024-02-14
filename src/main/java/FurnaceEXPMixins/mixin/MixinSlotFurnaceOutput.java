package FurnaceEXPMixins.mixin;

import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlotFurnaceOutput.class)
public class MixinSlotFurnaceOutput {
    @Inject(method = "onCrafting", at = @At("RETURN"))
    protected void onCrafting(ItemStack stack, CallbackInfo ci) {

    }
}
