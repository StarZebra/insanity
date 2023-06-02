package me.starzebra.features.feature;

import me.starzebra.Insanity;
import me.starzebra.events.ClickEvent;
import me.starzebra.utils.ItemUtils;
import me.starzebra.utils.NetworkUtils;
import me.starzebra.utils.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoulWhipping extends Feature{

    public SoulWhipping() {
        super("Soul Whipper", Category.COMBAT);
    }

    @SubscribeEvent
    public void onLeftClick(ClickEvent.LeftClickEvent event){
        if(this.isToggled()){
            useSkyBlockItem("SOUL_WHIP", true);
        }
    }

    public static void useSkyBlockItem(String itemId, boolean rightClick) {
        for(int i = 0; i < 9; i++) {
            ItemStack item = Insanity.mc.thePlayer.inventory.getStackInSlot(i);
            if(itemId.equals(ItemUtils.getSkyblockItemID(item))) {
                int previousItem = Insanity.mc.thePlayer.inventory.currentItem;
                Insanity.mc.thePlayer.inventory.currentItem = i;
                if(rightClick) {
                    Insanity.mc.playerController.sendUseItem(Insanity.mc.thePlayer, Insanity.mc.theWorld, item);
                } else {
                    NetworkUtils.sendPacket(new C09PacketHeldItemChange(i));
                    ReflectionUtils.click(ReflectionUtils.ClickType.LEFT);
                    NetworkUtils.sendPacket(new C09PacketHeldItemChange(previousItem));
                }
                Insanity.mc.thePlayer.inventory.currentItem = previousItem;
                return;
            }
        }
        System.out.println("Insanity could not find itemid ["+itemId+"]");


    }
}
