package miyucomics.overevaluate.actions.handlers

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds

class OpPluto(private val frameCount: Int) : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		var stack = image.stack.toList()

		for (i in 0..frameCount) {
			var done = false
			var newContinuation = continuation

			while (!done && newContinuation is SpellContinuation.NotDone) {
				val breakResult = newContinuation.frame.breakDownwards(stack)
				done = breakResult.first
				stack = breakResult.second
				newContinuation = newContinuation.next
			}

			if (!done)
				return OperationResult(image.withUsedOp().copy(stack = listOf()), listOf(), continuation, HexEvalSounds.SPELL)
		}

		return OperationResult(image.withUsedOp().copy(stack = stack), listOf(), continuation, HexEvalSounds.SPELL)
	}
}