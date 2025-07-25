package miyucomics.overevaluate.actions.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpSorobanReset : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation) =
		OperationResult(image.withUsedOp().also { it.userData.putDouble("soroban", 0.0) }, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
}