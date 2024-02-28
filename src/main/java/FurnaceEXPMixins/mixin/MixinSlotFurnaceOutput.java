package FurnaceEXPMixins.mixin;

import FurnaceEXPMixins.interfaces.MixinContainerFurnaceInterface;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.mixin.*;

@Mixin(SlotFurnaceOutput.class)
public abstract class MixinSlotFurnaceOutput {
    
    @Final
    @Shadow
    private EntityPlayer player;
    @Shadow
    private int removeCount;

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void onCrafting(ItemStack p75208_1)  {
        p75208_1.onCrafting(this.player.world, this.player, this.removeCount);
        if (!this.player.world.isRemote) {

            ContainerFurnace inv = ((ContainerFurnace) player.openContainer);
            TileEntityFurnace furnace;
            if(inv instanceof MixinContainerFurnaceInterface)
                furnace = (TileEntityFurnace) ((MixinContainerFurnaceInterface) inv).getTileFurnace();
            else
                furnace = new TileEntityFurnace();

            float accumulatedXP = furnace.getTileData().getFloat("AccumulatedXP");
            System.out.println("Imagine this shit had xp in it lol " + accumulatedXP);
            int outXP = MathHelper.floor(accumulatedXP);
            if (outXP < MathHelper.ceil(accumulatedXP) && Math.random() < (double)(accumulatedXP - (float)outXP)) {
                ++outXP;
            }

            while(outXP > 0) {
                int orbXP = EntityXPOrb.getXPSplit(outXP);
                outXP -= orbXP;
                this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5, this.player.posZ + 0.5, orbXP));
            }

            furnace.getTileData().setFloat("AccumulatedXP",0);
        }
        this.removeCount = 0;

        FMLCommonHandler.instance().firePlayerSmeltedEvent(this.player, p75208_1);
    }
}


