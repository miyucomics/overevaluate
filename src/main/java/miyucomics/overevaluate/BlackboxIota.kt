package miyucomics.overevaluate

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class BlackboxIota(contents: List<Iota>) : Iota(TYPE, contents) {
	val contents: MutableList<Iota> = this.payload as MutableList<Iota>
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.contents == (that as BlackboxIota).contents

	override fun isTruthy() = contents.isNotEmpty()
	override fun size() = 1 + contents.fold(0) { acc, iota -> acc + iota.size() }
	override fun serialize(): NbtElement {
		val list = NbtList()
		contents.forEach { list.add(IotaType.serialize(it)) }
		return list
	}

	companion object {
		var TYPE: IotaType<BlackboxIota> = object : IotaType<BlackboxIota>() {
			override fun color() = 0xff_000000.toInt()
			override fun display(tag: NbtElement) = Text.translatable("iota.overevaluate.blackbox").formatted(Formatting.BLACK)
			override fun deserialize(tag: NbtElement, world: ServerWorld): BlackboxIota {
				val contents = mutableListOf<Iota>()
				(tag as NbtList).forEach { contents.add(IotaType.deserialize(it as NbtCompound, world)) }
				return BlackboxIota(contents)
			}
		}
	}
}