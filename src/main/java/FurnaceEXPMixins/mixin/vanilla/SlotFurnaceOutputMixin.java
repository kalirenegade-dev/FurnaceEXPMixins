package FurnaceEXPMixins.mixin.vanilla;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(SlotFurnaceOutput.class)
public class SlotFurnaceOutputMixin {

    private int removeCount;
    private EntityPlayer player;

    @Inject(method = "onCrafting", at = @At("HEAD"))
    private void onCrafting(ItemStack stack, CallbackInfo ci) {
        stack.onCrafting(player.world, player, this.removeCount);

        if (!player.world.isRemote) {
            float totalExperience = getExperienceFromStack(stack);
            if (totalExperience > 0) {
                int expToAdd = MathHelper.floor(totalExperience);
                while (expToAdd > 0) {
                    int k = EntityXPOrb.getXPSplit(expToAdd);
                    expToAdd -= k;
                    player.world.spawnEntity(new EntityXPOrb(player.world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, k));
                }
            }
        }

        this.removeCount = 0;
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerSmeltedEvent(player, stack);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition, CallbackInfo ci) {
        this.player = player;
    }



    private float getExperienceFromStack(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        float itemExperience = FurnaceRecipes.instance().getSmeltingExperience(stack);
        float totalExperience = tagCompound.getFloat("totalExperience");
        totalExperience += itemExperience * this.removeCount;
        tagCompound.setFloat("totalExperience", totalExperience);
        return totalExperience;
    }

}
