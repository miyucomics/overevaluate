package miyucomics.overevaluate.frames

import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

data class AthenaFrame(val code: SpellList) : ContinuationFrame {
	override val type: ContinuationFrame.Type<*> = TYPE
	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		val newStack = stack.toMutableList()
		newStack.add(BooleanIota(false))
		return false to newStack
	}

	override fun evaluate(continuation: SpellContinuation, world: ServerWorld, harness: CastingVM): CastResult {
		if (!code.nonEmpty)
			return CastResult(ListIota(code), continuation, null, listOf(), ResolvedPatternType.EVALUATED, HexEvalSounds.HERMES)

		val moreToExecute = code.cdr.nonEmpty
		val update = harness.executeInner(code.car, world, continuation.pushFrame(FrameEvaluate(code.cdr, true)))
		val safeSideEffects = update.sideEffects.toMutableList()
		val mishapped = safeSideEffects.removeIf { sideEffect ->
			if (sideEffect is OperatorSideEffect.DoMishap) {
				harness.image.userData.putString("last_mishap", Text.Serializer.toJson(sideEffect.mishap.errorMessageWithName(harness.env, sideEffect.errorCtx)))
				return@removeIf true
			}
			false
		}

		val safeCastData = update.copy(sideEffects = safeSideEffects, sound = HexEvalSounds.HERMES, resolutionType = ResolvedPatternType.EVALUATED)
		val image = safeCastData.newData ?: harness.image

		if (mishapped) {
			var done = false
			var newStack = image.stack.toList()
			var newContinuationStack = continuation
			while (!done && newContinuationStack is SpellContinuation.NotDone) {
				val newInfo = newContinuationStack.frame.breakDownwards(newStack)
				done = newInfo.first
				newStack = newInfo.second
				newContinuationStack = newContinuationStack.next
			}
			val finalStack = newStack.toMutableList()
			finalStack.add(BooleanIota(true))
			return safeCastData.copy(continuation = newContinuationStack, newData = image.copy(stack = finalStack))
		}

		if (moreToExecute)
			return safeCastData

		val newStack = image.stack.toMutableList()
		newStack.add(BooleanIota(false))
		return safeCastData.copy(newData = image.copy(stack = newStack))
	}

	override fun size() = code.size()
	override fun serializeToNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.put("code", code.serializeToNBT())
		return compound
	}

	companion object {
		val TYPE: ContinuationFrame.Type<AthenaFrame> = object : ContinuationFrame.Type<AthenaFrame> {
			override fun deserializeFromNBT(tag: NbtCompound, world: ServerWorld) =
				AthenaFrame(HexIotaTypes.LIST.deserialize(tag.getList("code", NbtElement.COMPOUND_TYPE), world)!!.list)
		}
	}
}