package miyucomics.overevaluate.frames

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.eval.vm.FrameEvaluate
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.api.utils.hasList
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld

data class ApepFrame(val data: SpellList, val code: SpellList, var acc: Iota, val baseStack: List<Iota>?) : ContinuationFrame {
	override val type: ContinuationFrame.Type<*> = TYPE

	override fun breakDownwards(stack: List<Iota>): Pair<Boolean, List<Iota>> {
		val newStack = baseStack?.toMutableList() ?: mutableListOf()
		acc = stack.lastOrNull() ?: NullIota()
		newStack.add(acc)
		return true to newStack
	}

	override fun evaluate(continuation: SpellContinuation, level: ServerWorld, vm: CastingVM): CastResult {
		val stack = if (baseStack == null) {
			vm.image.stack.toList()
		} else {
			acc = vm.image.stack.lastOrNull() ?: NullIota()
			baseStack
		}

		val newStack = stack.toMutableList()
		val (newImage, newCont) = if (data.nonEmpty) {
			newStack.add(acc)
			newStack.add(data.car)
			Pair(vm.image.withUsedOp(), continuation
				.pushFrame(ApepFrame(data.cdr, code, acc, stack))
				.pushFrame(FrameEvaluate(code, true)))
		} else {
			newStack.add(acc)
			Pair(vm.image, continuation)
		}

		return CastResult(
			ListIota(code),
			newCont,
			newImage.withResetEscape().copy(stack = newStack),
			listOf(),
			ResolvedPatternType.EVALUATED,
			HexEvalSounds.THOTH,
		)
	}

	override fun serializeToNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.put("data", data.serializeToNBT())
		compound.put("code", code.serializeToNBT())
		compound.put("acc", IotaType.serialize(acc))
		if (baseStack != null)
			compound.put("base", baseStack.serializeToNBT())
		return compound
	}

	override fun size() = data.size() + code.size() + acc.size() + (baseStack?.size ?: 0)

	companion object {
		@JvmField
		val TYPE: ContinuationFrame.Type<ApepFrame> = object : ContinuationFrame.Type<ApepFrame> {
			override fun deserializeFromNBT(tag: NbtCompound, world: ServerWorld): ApepFrame {
				return ApepFrame(
					HexIotaTypes.LIST.deserialize(tag.getList("data", NbtCompound.COMPOUND_TYPE), world)!!.list,
					HexIotaTypes.LIST.deserialize(tag.getList("code", NbtCompound.COMPOUND_TYPE), world)!!.list,
					IotaType.deserialize(tag.getCompound("acc"), world),
					if (tag.hasList("base", NbtCompound.COMPOUND_TYPE)) HexIotaTypes.LIST.deserialize(tag.getList("base", NbtCompound.COMPOUND_TYPE), world)!!.list.toList() else null
				)
			}
		}
	}
}