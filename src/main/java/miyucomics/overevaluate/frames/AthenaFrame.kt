package miyucomics.overevaluate.frames

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.FrameFinishEval
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation.NotDone
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

object AthenaFrame : ContinuationFrame {
	private val TYPE: ContinuationFrame.Type<AthenaFrame> = object : ContinuationFrame.Type<AthenaFrame> {
		override fun deserializeFromNBT(compound: NbtCompound, world: ServerWorld) = AthenaFrame
	}

	override val type: ContinuationFrame.Type<*> = TYPE

	override fun evaluate(continuation: SpellContinuation, level: ServerWorld, vm: CastingVM): CastResult {
		val stack = vm.image.stack.toMutableList()
		stack.add(BooleanIota(false))
		return CastResult(
			NullIota(),
			continuation,
			vm.image.copy(stack = stack),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.NOTHING,
		)
	}

	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		val oldStack = stack.toMutableList()
		oldStack.add(BooleanIota(false))
		return false to oldStack
	}

	override fun serializeToNBT() = NbtCompound()
	override fun size() = 0

	@JvmStatic
	fun handleAthena(pattern: PatternIota, vm: CastingVM, world: ServerWorld, continuation: SpellContinuation, originalMethod: Operation<CastResult>): CastResult {
		val original = originalMethod.call(vm, world, continuation)
		if (original.resolutionType == ResolvedPatternType.EVALUATED)
			return original
		val reduced = reduceAthena(continuation) ?: return original

		val stack = vm.image.stack.toMutableList()
		stack.add(BooleanIota(true))
		val newImage = vm.image.copy(stack = stack)

		val mishap = original.sideEffects.find { it is OperatorSideEffect.DoMishap }
		if (mishap != null)
			newImage.userData.putString("last_mishap", Text.Serializer.toJson((mishap as OperatorSideEffect.DoMishap).mishap.errorMessageWithName(vm.env, mishap.errorCtx)))

		return CastResult(pattern, reduced, newImage, listOf(), ResolvedPatternType.EVALUATED, HexEvalSounds.NORMAL_EXECUTE)
	}

	private fun reduceAthena(continuation: SpellContinuation): SpellContinuation? {
		var cont = continuation
		while (cont is NotDone) {
			if (cont.frame is AthenaFrame) {
				cont = cont.next
				while (cont is NotDone) {
					if (cont.frame is FrameFinishEval)
						return cont.next
					cont = cont.next
				}
			}

			cont = (cont as NotDone).next
		}
		return null
	}
}