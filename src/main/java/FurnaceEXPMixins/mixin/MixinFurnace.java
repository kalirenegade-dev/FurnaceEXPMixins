package FurnaceEXPMixins.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityFurnace.class)
public abstract class MixinFurnace {

    @Shadow public abstract boolean canSmelt();
    @Shadow public abstract ItemStack getStackInSlot(int index);

    @Inject(method = "smeltItem", at = @At("HEAD"), cancellable = true)
    private void NewSmeltItem(CallbackInfo ci) {
        if (canSmelt()) {
            ItemStack smeltingItem = getStackInSlot(0);
            ItemStack resultSlot = getStackInSlot(2);

            // Calculate XP
            float xp = getSmeltedItemXP(smeltingItem);

            // Debug output
            System.out.println("Smelted Item XP: " + xp);

            // Accumulate XP
            accumulateXP(xp);
        }
    }

    private float getSmeltedItemXP(ItemStack smeltedItem) {
        // Debug output
        System.out.println("Checking smelting experience for item: " + smeltedItem.getItem().getRegistryName());
        ItemStack cookedItem = FurnaceRecipes.instance().getSmeltingResult(smeltedItem);


        // Determine XP value from the smelted item
        float xp = FurnaceRecipes.instance().getSmeltingExperience(cookedItem);

        // Debug output
        System.out.println("Smelting experience for item: " + smeltedItem.getItem().getRegistryName() + " is " + xp);

        return xp;
    }

    private void accumulateXP(float xp) {
        // Retrieve Tile Entity instance
        TileEntityFurnace furnace = (TileEntityFurnace)(Object)this;

        // Retrieve current NBT data
        NBTTagCompound nbt = furnace.getTileData();

        // Retrieve current accumulated XP from NBT
        float currentXP = nbt.getFloat("AccumulatedXP");

        // Accumulate XP
        currentXP += xp;

        nbt.setFloat("AccumulatedXP", currentXP);


        // Debug output
        System.out.println("Accumulated XP after update: " + currentXP);
    }


}

