package me.starzebra.features.feature;

import me.starzebra.Insanity;
import me.starzebra.events.ClickEvent;
import me.starzebra.features.feature.settings.NumberSetting;
import me.starzebra.utils.ItemUtils;
import me.starzebra.utils.NetworkUtils;
import me.starzebra.utils.ReflectionUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EasyAOTV extends Feature{

    public NumberSetting delay;

    public EasyAOTV() {
        super("Easy AOTV", Category.PLAYER);
        this.addSetting(this.delay = new NumberSetting("Delay", 50, 10, 500, 1));
    }

    @SubscribeEvent
    public void onLeftClick(ClickEvent.LeftClickEvent event){
        ItemStack heldItem = Insanity.mc.thePlayer.getHeldItem();
        if(heldItem == null) return;
        if(this.isToggled() && heldItem.getItem() == Items.fishing_rod){
            new Thread(() -> {
                KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindSneak.getKeyCode(), true);
                try {
                    Thread.sleep((long) this.delay.getValue());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                useSkyBlockItem("ASPECT_OF_THE_VOID", true);
                try {
                    Thread.sleep((long) this.delay.getValue());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                KeyBinding.setKeyBindState(Insanity.mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }).start();



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
