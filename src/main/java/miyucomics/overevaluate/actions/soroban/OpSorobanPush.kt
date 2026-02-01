package miyucomics.overevaluate.actions.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpSorobanPush : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.isEmpty())
			throw MishapNotEnoughArgs(1, 0)
		val x = stack.last()
		stack.removeAt(stack.lastIndex)

		if (x !is DoubleIota || (x.double - x.double.toInt()) > DoubleIota.TOLERANCE)
			throw MishapInvalidIota.of(x, 0, "int")

		val soroban = (image.userData.getIntArray("soroban") ?: intArrayOf()).toMutableList()
		soroban.add(x.double.toInt())
		return OperationResult(image.withUsedOp().also { it.userData.putIntArray("soroban", soroban) }, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}