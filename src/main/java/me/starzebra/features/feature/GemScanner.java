package me.starzebra.features.feature;

import me.starzebra.Insanity;
import me.starzebra.features.feature.settings.NumberSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;


public class GemScanner extends Feature{

    public NumberSetting scanDist;
    private boolean isScanning = false;
    private static HashMap<Integer, ArrayList<BlockPos>> veins = new HashMap<>();


    public GemScanner() {
        super("Gemstone Scanner", 0, Category.RENDER);
        this.scanDist = new NumberSetting("Scan distance", 15, 1, 30, 1);
        this.addSetting(this.scanDist);
    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event){
        if(Insanity.mc.thePlayer == null) return;
        if(this.isToggled() && !isScanning){
            EntityPlayerSP p = Insanity.mc.thePlayer;
            scanBlocks(p.posX, p.posY, p.posZ);
            //System.out.println("SCANNING");
        }
    }

    public void scanBlocks(double startX, double startY, double startZ){
        isScanning = true;
        //ArrayList<BlockPos> stones = new ArrayList<>();
        for(int x = (int) (startX-scanDist.getValue()); x < startX+scanDist.getValue(); x++){
            for(int y = (int) (startY-scanDist.getValue()); y < startY+scanDist.getValue(); y++){
                for(int z = (int) (startZ-scanDist.getValue()); z < startZ+scanDist.getValue(); z++){


                    BlockPos pos = new BlockPos(x,y,z);
                    Block block = Insanity.mc.theWorld.getBlockState(pos).getBlock();
                    //System.out.println(pos);
                    if(block == Blocks.stained_glass || block == Blocks.stained_glass_pane){
                        if(Insanity.mc.theWorld.getBlockState(pos).getValue(BlockStainedGlass.COLOR) == EnumDyeColor.RED){
                            System.out.println("[DEBUG] found glass at "+ pos + ":::" +Insanity.mc.theWorld.getBlockState(pos).getValue(BlockStainedGlass.COLOR));
                            //stones.add(pos);

                            //veins.put(1, stones);
                            return;
                        }

                    }
                }
            }
        }
        isScanning = false;
    }

    enum Gemstone {
        RUBY(EnumDyeColor.RED),
        JADE(EnumDyeColor.LIME),
        AMETHYST(EnumDyeColor.PURPLE),
        AMBER(EnumDyeColor.ORANGE),
        SAPPHIRE(EnumDyeColor.LIGHT_BLUE),
        TOPAZ(EnumDyeColor.YELLOW),
        JASPER(EnumDyeColor.PINK);

        Gemstone(EnumDyeColor dyeColor){
        }
    }
}
