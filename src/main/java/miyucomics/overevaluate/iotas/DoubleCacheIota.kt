package miyucomics.overevaluate.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.asDouble
import net.minecraft.nbt.NbtDouble
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class DoubleCacheIota(contents: HashSet<Double>) : Iota(TYPE, contents) {
	val contents: HashSet<Double> = this.payload as HashSet<Double>
	override fun isTruthy() = contents.isNotEmpty()
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.contents == (that as DoubleCacheIota).contents
	override fun serialize(): NbtElement {
		val list = NbtList()
		contents.forEach { list.add(NbtDouble.of(it)) }
		return list
	}

	companion object {
		var TYPE: IotaType<DoubleCacheIota> = object : IotaType<DoubleCacheIota>() {
			override fun color() = -0xaa00ab

			override fun display(tag: NbtElement): Text {
				val contents = Text.literal("{ ")
				(tag as NbtList).forEach { contents.append(Text.literal(String.format("%.2f ", it.asDouble))) }
				return contents.append(Text.literal("}")).formatted(Formatting.GREEN)
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld): DoubleCacheIota {
				val contents = hashSetOf<Double>()
				(tag as NbtList).forEach { contents.add(it.asDouble) }
				return DoubleCacheIota(contents)
			}
		}
	}
}