package miyucomics.overevaluate.actions.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpSorobanRetrieve : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		val soroban = (image.userData.getIntArray("soroban") ?: intArrayOf()).toMutableList()
		if (soroban.isEmpty()) {
			stack.add(DoubleIota(0.0))
			soroban.add(1)
		} else {
			val x = soroban.removeLast()
			stack.add(DoubleIota(x.toDouble()))
			soroban.add(x + 1)
		}
		return OperationResult(image.withUsedOp().copy(stack = stack).also { it.userData.putIntArray("soroban", soroban) }, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}