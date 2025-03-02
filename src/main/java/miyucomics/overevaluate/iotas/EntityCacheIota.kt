package miyucomics.overevaluate.iotas

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*

class EntityCacheIota(contents: HashMap<UUID, String>) : Iota(TYPE, contents) {
	val contents: HashMap<UUID, String> = this.payload as HashMap<UUID, String>
	override fun isTruthy() = contents.isNotEmpty()
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.contents == (that as EntityCacheIota).contents
	override fun serialize(): NbtElement {
		val compound = NbtCompound()
		contents.forEach { uuid, name -> compound.putString(uuid.toString(), name) }
		return compound
	}

	companion object {
		var TYPE: IotaType<EntityCacheIota> = object : IotaType<EntityCacheIota>() {
			override fun color() = -0xaa0001

			override fun display(tag: NbtElement): Text {
				val contents = Text.literal("{ ")
				val compound = tag as NbtCompound
				compound.keys.forEach { contents.append(compound.getString(it) + " ") }
				return contents.append(Text.literal("}")).formatted(Formatting.AQUA)
			}

			override fun deserialize(tag: NbtElement, world: ServerWorld): EntityCacheIota {
				val contents = hashMapOf<UUID, String>()
				val compound = tag as NbtCompound
				compound.keys.forEach() { contents[UUID.fromString(it)] = compound.getString(it) }
				return EntityCacheIota(contents)
			}
		}
	}
}