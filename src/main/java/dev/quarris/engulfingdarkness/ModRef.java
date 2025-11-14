package dev.quarris.engulfingdarkness;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.fml.config.ModConfigs;

public class ModRef {

    public static final String ID = "engulfingdarkness";

    public static final Logger LOGGER = LogUtils.getLogger();

    static ModConfigs CONFIGS;
    public static ModConfigs configs() {
        return CONFIGS;
    }

    public static ResourceLocation res(String res) {
        return ResourceLocation.fromNamespaceAndPath(ID, res);
    }

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DARKNESS = DATA_COMPONENTS.register("darkness",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static class Tags {
        public static final TagKey<Item> LIGHT_BRINGERS = ItemTags.create(ModRef.res("light_bringers"));
    }

}