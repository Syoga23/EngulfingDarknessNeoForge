package dev.quarris.engulfingdarkness.capability;

import dev.quarris.engulfingdarkness.ModRef;  // Assuming this holds your mod ID and capability registry
import dev.quarris.engulfingdarkness.darkness.Darkness;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.Lazy;


public class DarknessProvider implements CapabilitySerializable<CompoundTag> {  // Replaces ICapabilitySerializable
    public static final ResourceLocation KEY = ModRef.res("darkness");

    private final Lazy<Darkness> lazyThis;
    private final Player player;

    public DarknessProvider(Player player) {
        this.player = player;
        this.lazyThis = Lazy.of(() -> new Darkness(player));  // Still works, renamed imports
    }

    // Updated getter: Simpler, no generics beyond the interface
    @Override
    public Lazy<Darkness> getCapability(Capability<?> cap, Direction side) {
        if (cap == ModRef.Capabilities.DARKNESS) {
            return this.lazyThis;  // Direct return, no cast needed
        }
        return LazyOptional.empty();
    }

    // Updated serialize: Matches new interface signature
    @Override
    public CompoundTag serialize(HolderLookup.Provider provider) {
        return this.lazyThis.map(darkness -> darkness.serializeNBT(provider)).orElse(new CompoundTag());
    }

    // Updated deserialize: Matches new interface signature
    @Override
    public void deserialize(HolderLookup.Provider provider, CompoundTag tag) {
        this.lazyThis.ifPresent(darkness -> darkness.deserializeNBT(provider, tag));
    }

    // Optional: Invalidate the LazyOptional when the provider is no longer needed (e.g., when entity dies/removes)
    public void invalidate() {
        this.lazyThis.invalidate();
    }
}