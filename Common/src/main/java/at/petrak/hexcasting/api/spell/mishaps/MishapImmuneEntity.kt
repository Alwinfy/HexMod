package at.petrak.hexcasting.api.spell.mishaps

import at.petrak.hexcasting.api.misc.FrozenColorizer
import at.petrak.hexcasting.api.mod.HexItemTags
import at.petrak.hexcasting.api.spell.SpellDatum
import at.petrak.hexcasting.api.spell.casting.CastingContext
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack

class MishapImmuneEntity(val entity: Entity) : Mishap() {
    override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer =
        dyeColor(DyeColor.BLUE)

    override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<SpellDatum<*>>) {
        val items = mutableListOf<ItemStack>()
        for (hand in InteractionHand.values()) {
            if (hand != ctx.castingHand || ctx.caster.getItemInHand(hand).`is`(HexItemTags.WANDS)) {
                items.add(ctx.caster.getItemInHand(hand).copy())
                ctx.caster.setItemInHand(hand, ItemStack.EMPTY)
            }
        }

        val delta = entity.position().subtract(ctx.position).normalize().scale(0.5)

        for (item in items) {
            yeetItem(item, ctx, delta)
        }
    }

    override fun errorMessage(ctx: CastingContext, errorCtx: Context): Component {
        return error("immune_entity", actionName(errorCtx.action), entity.displayName)
    }
}
