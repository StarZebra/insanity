package me.starzebra.mixin;

import me.starzebra.features.ItemUpdateFix;
import me.starzebra.utils.ItemUtils;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerController {

    @Redirect(method = {"isHittingPosition"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;areItemStackTagsEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private boolean shouldTagsBeEqual(ItemStack stackA, ItemStack stackB){
        if(ItemUpdateFix.toggled){
            return Objects.equals(ItemUtils.getSkyblockItemID(stackA), ItemUtils.getSkyblockItemID(stackB));
        }
        return ItemStack.areItemStackTagsEqual(stackA, stackB);

    }
}
