package at.petrak.hexcasting.api.spell

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.ContinuationFrame
import at.petrak.hexcasting.api.spell.casting.OperatorSideEffect
import at.petrak.hexcasting.api.spell.mishaps.MishapNotEnoughArgs

interface SpellOperator : Operator {
    val argc: Int

    fun hasCastingSound(ctx: CastingContext): Boolean = true

    fun awardsCastingStat(ctx: CastingContext): Boolean = true

    fun execute(
        args: List<SpellDatum<*>>,
        ctx: CastingContext
    ): Triple<RenderedSpell, Int, List<ParticleSpray>>?

    override fun operate(continuation: MutableList<ContinuationFrame>, stack: MutableList<SpellDatum<*>>, local: SpellDatum<*>, ctx: CastingContext): OperationResult {
        if (this.argc > stack.size)
            throw MishapNotEnoughArgs(this.argc, stack.size)
        val args = stack.takeLast(this.argc)
        for (_i in 0 until this.argc) stack.removeLast()
        val executeResult = this.execute(args, ctx) ?: return OperationResult(stack, local, listOf())
        val (spell, mana, particles) = executeResult

        val sideEffects = mutableListOf(
            OperatorSideEffect.ConsumeMana(mana),
            OperatorSideEffect.AttemptSpell(spell, this.isGreat, this.hasCastingSound(ctx), this.awardsCastingStat(ctx))
        )
        for (spray in particles) {
            sideEffects.add(OperatorSideEffect.Particles(spray))
        }

        return OperationResult(stack, local, sideEffects)
    }

}
