package miyucomics.overevaluate.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.serializeToNBT
import at.petrak.hexcasting.api.utils.vecFromNBT
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Vec3d

class VectorCacheIota(contents: HashSet<Vec3d>) : Iota(TYPE, contents) {
	val contents: HashSet<Vec3d> = this.payload as HashSet<Vec3d>
	override fun isTruthy() = contents.isNotEmpty()
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.contents == (that as VectorCacheIota).contents
	override fun serialize(): NbtElement {
		val list = NbtList()
		contents.forEach { list.add(it.serializeToNBT()) }
		return list
	}

	companion object {
		var TYPE: IotaType<VectorCacheIota> = object : IotaType<VectorCacheIota>() {
			override fun color() = -0xcfd0

			override fun display(tag: NbtElement): Text {
				val contents = Text.literal("{ ")

				(tag as NbtList).forEach {
					val x = it.asCompound.getDouble("x")
					val y = it.asCompound.getDouble("y")
					val z = it.asCompound.getDouble("z")
					contents.append(Text.literal(String.format("(%.2f, %.2f, %.2f) ", x, y, z)))
				}

				return contents.append(Text.literal("}")).formatted(Formatting.RED)
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld): VectorCacheIota {
				val contents = hashSetOf<Vec3d>()
				(tag as NbtList).forEach { contents.add(vecFromNBT(it as NbtCompound)) }
				return VectorCacheIota(contents)
			}
		}
	}
}