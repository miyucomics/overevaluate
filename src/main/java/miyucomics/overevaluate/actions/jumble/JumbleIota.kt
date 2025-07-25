package miyucomics.overevaluate.actions.jumble

import at.petrak.hexcasting.api.casting.eval.CastResult
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType
import at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect.DoMishap
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.styledWith
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class JumbleIota(jumble: Jumble) : Iota(TYPE, jumble) {
	override fun isTruthy() = true
	val jumble: Jumble = this.payload as Jumble
	override fun toleratesOther(that: Iota) = (typesMatch(this, that) && that is JumbleIota) && this.jumble == that.jumble
	override fun serialize() = jumble.serialize()

	override fun executable() = true
	override fun execute(vm: CastingVM, world: ServerWorld, continuation: SpellContinuation): CastResult {
		val stack = vm.image.stack.toMutableList()
		if (stack.size < jumble.args)
			return CastResult(this, continuation, null, listOf(
				DoMishap(MishapNotEnoughArgs(jumble.args, stack.size), Mishap.Context(null, null))
			), ResolvedPatternType.INVALID, HexEvalSounds.MISHAP)

		val reference = stack.takeLast(jumble.args)
		for (i in 0 until jumble.args)
			stack.removeLast()
		stack.addAll(jumble.jumble.map { reference[it] })

		return CastResult(this, continuation, vm.image.copy(stack = stack), listOf(), ResolvedPatternType.EVALUATED, HexEvalSounds.NORMAL_EXECUTE)
	}

	companion object {
		var TYPE: IotaType<JumbleIota> = object : IotaType<JumbleIota>() {
			override fun color() = 0xff_4b0afc.toInt()
			override fun deserialize(tag: NbtElement, world: ServerWorld) = JumbleIota(Jumble.deserialize(tag.asCompound))
			override fun display(tag: NbtElement) = Jumble.display(tag.asCompound)
		}
	}
}

data class Jumble(val args: Int, val jumble: List<Int>) {
	fun serialize() = NbtCompound().also {
		it.putInt("args", args)
		it.putIntArray("jumble", jumble)
	}

	companion object {
		fun deserialize(compound: NbtCompound) = Jumble(compound.getInt("args"), compound.getIntArray("jumble").toList())
		fun display(compound: NbtCompound): Text {
			val jumble = deserialize(compound)
			return Text.literal("${jumble.args} [${jumble.jumble.joinToString(" ")}]").formatted(Formatting.BLUE)
		}
	}
}

fun List<Iota>.getJumble(idx: Int, argc: Int = 0): Jumble {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is JumbleIota)
		return x.jumble
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "jumble")
}