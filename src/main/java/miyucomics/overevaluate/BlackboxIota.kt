package miyucomics.overevaluate

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class BlackboxIota(set: HashSet<Iota>) : Iota(TYPE, set) {
	override fun isTruthy() = set.isNotEmpty()
	val set: HashSet<Iota> = this.payload as HashSet<Iota>
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.set == (that as BlackboxIota).set
	override fun size() = 1 + set.fold(0) { acc, iota -> acc + iota.size() }

	override fun serialize(): NbtElement {
		val list = NbtList()
		set.forEach { list.add(IotaType.serialize(it)) }
		return list
	}

	companion object {
		var TYPE: IotaType<BlackboxIota> = object : IotaType<BlackboxIota>() {
			override fun color() = 0xff_000000.toInt()
			override fun display(tag: NbtElement) = Text.translatable("iota.overevaluate.blackbox").formatted(Formatting.BLACK)

			override fun deserialize(tag: NbtElement, world: ServerWorld): BlackboxIota {
				val set = HashSet<Iota>()
				(tag as NbtList).forEach { set.add(IotaType.deserialize(it as NbtCompound, world)) }
				return BlackboxIota(set)
			}
		}
	}
}