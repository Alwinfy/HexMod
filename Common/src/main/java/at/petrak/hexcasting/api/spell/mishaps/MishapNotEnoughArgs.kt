package at.petrak.hexcasting.api.spell.mishaps

import at.petrak.hexcasting.api.spell.SpellDatum
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.Widget
import at.petrak.hexcasting.api.misc.FrozenColorizer
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor

class MishapNotEnoughArgs(val expected: Int, val got: Int) : Mishap() {
    override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer =
        dyeColor(DyeColor.LIGHT_GRAY)

    override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<SpellDatum<*>>) {
        for (i in got until expected)
            stack.add(SpellDatum.make(Widget.GARBAGE))
    }

    override fun errorMessage(ctx: CastingContext, errorCtx: Context): Component =
        error("not_enough_args", actionName(errorCtx.action), expected, got)
}
