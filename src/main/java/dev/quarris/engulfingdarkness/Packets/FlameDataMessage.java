package dev.quarris.engulfingdarkness.Packets;

import dev.quarris.engulfingdarkness.ModRef;
import dev.quarris.engulfingdarkness.darkness.Darkness;
import dev.quarris.engulfingdarkness.darkness.LightBringer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class FlameDataMessage {
    private final LightBringer light;
    private final int flame;

    public FlameDataMessage(LightBringer light, int flame) {
        this.light = light;
        this.flame = flame;
    }

    public static void encode(FlameDataMessage msg, FriendlyByteBuf buf) {
        buf.writeUtf(BuiltInRegistries.ITEM.getKey(msg.light.item()).toString());
        buf.writeVarInt(msg.flame);
    }

    public static FlameDataMessage decode(FriendlyByteBuf buf) {
        ResourceLocation itemId = ResourceLocation.parse(buf.readUtf());
        Item item = BuiltInRegistries.ITEM.get(itemId);
        LightBringer light = LightBringer.getLightBringer(item);
        int flame = buf.readVarInt();
        return new FlameDataMessage(light, flame);
    }

    public static class Handler {
        public static void handle(FlameDataMessage msg, Context ctx) {
            ctx.enqueueWork(() -> Minecraft.getInstance().player.getCapability(ModRef.Capabilities.DARKNESS).ifPresent(darkness -> Darkness.setFlame(msg.light, msg.flame)));
            ctx.setPacketHandled(true);
        }
    }
}
