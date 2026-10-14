package com.example.butterflyknife;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KnifeClient implements ClientModInitializer {
    public static final int INSPECT_TICKS = 50;
    public static int inspectTick = -1; // -1 = not inspecting
    static KeyBinding key;

    @Override
    public void onInitializeClient() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.butterflyknife.inspect", GLFW.GLFW_KEY_H,
            KeyBinding.Category.create(Identifier.of("butterflyknife", "main"))));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.player == null) { inspectTick = -1; return; }
            var held = mc.player.getMainHandStack();
            boolean knife = held.isOf(Items.DIAMOND_SWORD) || held.isOf(Items.NETHERITE_SWORD);
            if (!knife) { inspectTick = -1; return; }
            while (key.wasPressed()) if (inspectTick < 0) inspectTick = 0;
            if (inspectTick >= 0 && ++inspectTick > INSPECT_TICKS) inspectTick = -1;
        });
    }
}
