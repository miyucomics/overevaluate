package miyucomics.overevaluate.actions.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpSorobanPop : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val soroban = (image.userData.getIntArray("soroban") ?: intArrayOf()).toMutableList()
		if (soroban.isEmpty()) {
			soroban.add(0)
		} else {
			soroban.removeLast()
		}
		return OperationResult(image.withUsedOp().also { it.userData.putIntArray("soroban", soroban) }, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}