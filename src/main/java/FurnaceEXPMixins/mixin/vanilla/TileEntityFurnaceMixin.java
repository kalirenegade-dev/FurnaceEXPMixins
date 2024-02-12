package FurnaceEXPMixins.mixin.vanilla;

import FurnaceEXPMixins.FurnaceEXPMixins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin extends TileEntity {

    private float totalExperience = 0;

    @Inject(method = "smeltItem", at = @At("HEAD"))
    private void smeltItem(CallbackInfo callbackInfo) {
        TileEntityFurnace furnace = (TileEntityFurnace)(Object)this;

        // Accumulate experience gained from smelting
        for (int i = 0; i < furnace.getSizeInventory(); i++) {
            ItemStack itemStack = furnace.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                float itemExperience = getSmeltingExperience(itemStack);
                totalExperience += itemExperience;
                FurnaceEXPMixins.LOGGER.info("itemExperience = " + itemExperience);
                FurnaceEXPMixins.LOGGER.log(Level.INFO, "itemExperience smelted.");

            }
        }
    }

    @Inject(method = "removeStackFromSlot", at = @At("HEAD"))
    private void onRemoveStackFromSlot(int index, CallbackInfoReturnable<ItemStack> cir) {
        TileEntityFurnace furnace = (TileEntityFurnace)(Object)this;
        if (totalExperience > 0) {
            // Distribute accumulated experience to players
            World world = furnace.getWorld();
            if (!world.isRemote) {
                List<EntityPlayer> playerList = world.playerEntities;
                for (EntityPlayer player : playerList) {
                    player.addExperience((int)totalExperience);
                    FurnaceEXPMixins.LOGGER.info("totalExperience = " + totalExperience);

                }
            }
            totalExperience = 0; // Reset accumulated experience
        }
    }

    // Helper method to get the experience gained from smelting an ItemStack
    private float getSmeltingExperience(ItemStack stack) {
        return FurnaceRecipes.instance().getSmeltingExperience(stack);
    }
}
