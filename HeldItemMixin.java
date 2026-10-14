package com.example.butterflyknife.mixin;

import com.example.butterflyknife.KnifeClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemMixin {
    // The parameter list must match renderFirstPersonItem in 1.21.11 exactly.
    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void inspect(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
                         float swing, ItemStack stack, float equip, MatrixStack matrices,
                         @Coerce Object queue, int light, CallbackInfo ci) {
        if (hand != Hand.MAIN_HAND || KnifeClient.inspectTick < 0) return;
        if (!(stack.isOf(Items.DIAMOND_SWORD) || stack.isOf(Items.NETHERITE_SWORD))) return;
        float t = (KnifeClient.inspectTick + tickDelta) / KnifeClient.INSPECT_TICKS;
        float ease = (float) Math.sin(Math.PI * t);
        matrices.translate(-0.15f * ease, 0.25f * ease, -0.1f * ease);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-35f * ease));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(720f * t));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(25f * ease));
    }
}
