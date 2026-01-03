package miyucomics.overevaluate.actions.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpSorobanPop : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val data = image.userData
		val soroban = if (data.contains("soroban", 11)) data.getIntArray("soroban") else intArrayOf()
		val newImage = image.withUsedOp().copy()
		newImage.userData.putIntArray("soroban", soroban.(0))
		return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}