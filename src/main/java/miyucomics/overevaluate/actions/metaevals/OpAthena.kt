package miyucomics.overevaluate.actions.metaevals

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.FrameFinishEval
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.overevaluate.frames.AthenaFrame

object OpAthena : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		val instructions = stack.getList(stack.lastIndex, stack.size)
		stack.removeLast()

		return OperationResult(
			image.withUsedOp().copy(stack = stack),
			listOf(),
			continuation
				.pushFrame(FrameFinishEval)
				.pushFrame(AthenaFrame)
				.pushFrame(FrameEvaluate(instructions, false)),
			HexEvalSounds.HERMES
		)
	}
}