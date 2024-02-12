package FurnaceEXPMixins.mixin.vanilla;

import FurnaceEXPMixins.FurnaceEXPMixins;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin extends TileEntity {

    @Inject(method = "smeltItem", at = @At("HEAD"))
    private void smeltItem(CallbackInfo callbackInfo) {
        FurnaceEXPMixins.LOGGER.info("smeltItem");
        TileEntityFurnace furnace = (TileEntityFurnace)(Object)this;

        // Accumulate experience gained from smelting
        for (int i = 0; i < furnace.getSizeInventory(); i++) {
            ItemStack itemStack = furnace.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                float itemExperience = getSmeltingExperience(itemStack);
                float totalExperience = itemStack.getTagCompound().getFloat("totalExperience");

                totalExperience += itemExperience;
                if(!itemStack.hasTagCompound()){
                    itemStack.setTagCompound(new NBTTagCompound());
                    itemStack.getTagCompound().setFloat("totalExperience", totalExperience);

                }
            }
        }
    }

    // Helper method to get the experience gained from smelting an ItemStack
    private float getSmeltingExperience(ItemStack stack) {
        return FurnaceRecipes.instance().getSmeltingExperience(stack);
    }



}
