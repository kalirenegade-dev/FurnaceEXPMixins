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
        int quantity = cookedItem.getCount();
        System.out.println("Resulting items count: " + quantity);


        // Determine XP value from the smelted item
        float xp = FurnaceRecipes.instance().getSmeltingExperience(cookedItem);

        // Determine XP value from the smelted item
        if (xp == 99999.0){
            xp = (quantity); //some items smelt to 9999.0 exp but give out 1xp per item so we will just grab the count
        }

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

