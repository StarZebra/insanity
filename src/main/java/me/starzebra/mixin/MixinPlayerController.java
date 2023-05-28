package me.starzebra.mixin;

import me.starzebra.Insanity;
import me.starzebra.utils.ItemUtils;
import me.starzebra.utils.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerController {

    @Redirect(method = {"isHittingPosition"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemStackTagsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean shouldTagsBeEqual(ItemStack stackA, ItemStack stackB){
        if(!Insanity.itemUpdateFix.isToggled()) return ItemStack.areItemStackTagsEqual(stackA, stackB);
        return Objects.equals(ItemUtils.getSkyblockItemID(stackA), ItemUtils.getSkyblockItemID(stackB));
    }

    @Inject(method = "clickBlock", at = @At(value = "HEAD"))
    private void onPlayerDamageBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir){
        if(Insanity.fungiCooldown > 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        ItemStack heldItem = player.getHeldItem();
        Block block = mc.theWorld.getBlockState(loc).getBlock();

        if(heldItem == null) return;
        if(!Objects.equals(ItemUtils.getSkyblockItemID(heldItem), "FUNGI_CUTTER")) return;

        String fungiMode = getFungiToolMode(heldItem);

        if (block.equals(Blocks.red_mushroom)) {
            if (fungiMode.equals("RED")) return;
            ReflectionUtils.click(ReflectionUtils.ClickType.RIGHT);
            Insanity.fungiCooldown = 40;

        } else if (block.equals(Blocks.brown_mushroom)) {
            if (fungiMode.equals("BROWN")) return;
            ReflectionUtils.click(ReflectionUtils.ClickType.RIGHT);
            Insanity.fungiCooldown = 40;
        }

    }

    private String getFungiToolMode(ItemStack item){
        NBTTagCompound nbt = ItemUtils.getExtraAttributes(item);
        return nbt.getString("fungi_cutter_mode");
    }

}
