package miyucomics.overevaluate.actions.soroban

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

object OpSorobanIncrement : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		val soroban = image.userData.getInt("soroban").toDouble()
		stack.add(DoubleIota(soroban))
		return OperationResult(image.withUsedOp().copy(stack = stack).also { it.userData.putDouble("soroban", soroban + 1) }, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}