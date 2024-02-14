package FurnaceEXPMixins.events;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class MixinFurnaceEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            BlockPos pos = event.getPos();
            if (world.getTileEntity(pos) instanceof TileEntityFurnace) {
                TileEntityFurnace furnace = (TileEntityFurnace) world.getTileEntity(pos);
                float accumulatedXP = getAccumulatedXPFromNBT(furnace);

                int outXP = MathHelper.floor(accumulatedXP);
                if (outXP < MathHelper.ceil(accumulatedXP) && Math.random() < (double)(accumulatedXP - (float)outXP)) {
                    ++outXP;
                }

                if (outXP > 0) {
                    // Drop accumulated XP
                    while (outXP > 0) {
                        int orbXP = EntityXPOrb.getXPSplit(outXP);
                        outXP -= orbXP;
                        world.spawnEntity(new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), (int)orbXP));
                    }
                    // Reset accumulated XP in NBT
                    furnace.writeToNBT(setAccumulatedXPToNBT(furnace, 0));

                }
            }
        }
    }

    private static float getAccumulatedXPFromNBT(TileEntityFurnace furnace) {
        NBTTagCompound nbt = furnace.getTileData();
        return nbt.getFloat("AccumulatedXP");
    }

    private static NBTTagCompound setAccumulatedXPToNBT(TileEntityFurnace furnace, float xp) {
        NBTTagCompound nbt = furnace.getTileData();
        nbt.setFloat("AccumulatedXP", xp);
        return nbt;
    }
}