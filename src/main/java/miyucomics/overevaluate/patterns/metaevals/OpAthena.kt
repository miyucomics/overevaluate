package miyucomics.overevaluate.patterns.metaevals

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.FrameFinishEval
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.evaluatable
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.overevaluate.frames.AthenaFrame

object OpAthena : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		val iota = stack.removeLastOrNull() ?: throw MishapNotEnoughArgs(1, 0)
		return exec(image, continuation, stack, iota)
	}

	private fun exec(image: CastingImage, continuation: SpellContinuation, newStack: MutableList<Iota>, iota: Iota): OperationResult {
		val instrs = evaluatable(iota, 0)
		val newCont =
			if (instrs.left().isPresent || (continuation is SpellContinuation.NotDone && continuation.frame is FrameFinishEval)) {
				continuation
			} else {
				continuation.pushFrame(FrameFinishEval)
			}

		val instructionList = instrs.map({ SpellList.LList(0, listOf(it)) }, { it })
		return OperationResult(
			image.withUsedOp().copy(stack = newStack),
			listOf(),
			newCont
				.pushFrame(AthenaFrame)
				.pushFrame(FrameEvaluate(instructionList, false)),
			HexEvalSounds.HERMES
		)
	}
}