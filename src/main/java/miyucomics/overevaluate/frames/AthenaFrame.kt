package miyucomics.overevaluate.frames

import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.iota.BooleanIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld

object AthenaFrame : ContinuationFrame {
	@JvmField
	val TYPE: ContinuationFrame.Type<AthenaFrame> = object : ContinuationFrame.Type<AthenaFrame> {
		override fun deserializeFromNBT(tag: NbtCompound, world: ServerWorld) = AthenaFrame
	}

	override val type: ContinuationFrame.Type<*> = TYPE
	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		val newStack = stack.toMutableList()
		newStack.add(BooleanIota(false))
		return false to newStack
	}

	override fun evaluate(continuation: SpellContinuation, world: ServerWorld, harness: CastingVM): CastResult {
		return CastResult(
			NullIota(),
			continuation,
			null,
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.NOTHING,
		)
	}

	override fun size() = 0
	override fun serializeToNBT() = NbtCompound()
}