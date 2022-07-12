package github.pitbox46.tetramaterialintegration.mixin;

import github.pitbox46.tetramaterialintegration.Config;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.module.schematic.ConfigSchematic;
import se.mickelus.tetra.module.schematic.OutcomeDefinition;
import se.mickelus.tetra.module.schematic.SchematicDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Mixin(value = ConfigSchematic.class, remap = false)
public class ConfigSchematicMixin {
    @Shadow private SchematicDefinition definition;

    @Inject(at = @At(value = "HEAD"), method = "getOutcomeFromMaterial", cancellable = true)
    private void onGetOutcomeFromMaterial(ItemStack materialStack, int slot, CallbackInfoReturnable<Optional<OutcomeDefinition>> cir) {
        cir.setReturnValue(Arrays.stream(this.definition.outcomes).filter((outcome) -> {
            return outcome.materialSlot == slot;
        }).filter((outcome) -> {
            Map<String, ArrayList<String>> map = Config.specialToolParts;
            if(map.containsKey(outcome.moduleKey)) {
                outcome.material.count = 1;
                return map.get(outcome.moduleKey).contains(materialStack.getItem().getRegistryName().toString()) &&
                        Arrays.stream(outcome.material.getApplicableItemStacks()).anyMatch(stack -> {
                            return stack.getItem().equals(getTetraMaterial(materialStack));
                        });
            }
            return outcome.material.getPredicate() != null && outcome.material.getPredicate().matches(materialStack);
        }).reduce((a, b) -> {
            return b;
        }));
    }

    private static Item getTetraMaterial(ItemStack stack) {
        //TESTING ONLY
        if(stack.getItem().equals(Items.GOLDEN_SWORD)) {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:iron_ingot"));
        }

        String mat = stack.hasTag() ? stack.getOrCreateTag().getString(Config.materialTagName) : "";
        if(mat.startsWith("tconstruct:")) {
            mat = Config.tConstructMatCompat.getOrDefault(mat, mat);
        }
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(mat));
    }
}
