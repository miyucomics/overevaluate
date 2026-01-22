package miyucomics.overevaluate.actions.handlers

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import kotlin.math.pow

class OpRoot(private val n: Int) : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)
		val x = stack.last()
		stack.removeAt(stack.lastIndex)

		if (x !is DoubleIota)
			throw MishapInvalidIota.of(x, 0, "double.positive")
		if (x.double < 0)
			throw MishapInvalidIota.of(x, 0, "double.positive")

		stack.add(DoubleIota(x.double.pow(1 / n.toDouble())))
		return OperationResult(image.withUsedOp().copy(stack = stack), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}