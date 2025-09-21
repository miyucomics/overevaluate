package miyucomics.overevaluate.actions.metaevals

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.evaluatable
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.overevaluate.frames.ApepFrame

object OpApep : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()
		if (stack.size < 2)
			throw MishapNotEnoughArgs(2, stack.size)
		val code = evaluatable(stack[stack.lastIndex - 1], 1).map({ SpellList.LList(0, listOf(it)) }, { it })
		val data = stack.getList(stack.lastIndex)
		stack.removeAt(stack.lastIndex)
		stack.removeAt(stack.lastIndex)

		if (data.size() == 0) {
			stack.add(ListIota(listOf()))
			return OperationResult(image.withUsedOp().withResetEscape().copy(stack = stack), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
		}

		return OperationResult(
			image.withUsedOp().withResetEscape().copy(stack = stack),
			listOf(),
			continuation.pushFrame(ApepFrame(data.cdr, code, data.car, null)),
			HexEvalSounds.THOTH
		)
	}
}