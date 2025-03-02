package miyucomics.overevaluate.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator.Companion.downcast
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import miyucomics.overevaluate.iotas.EntityCacheIota
import java.util.*
import kotlin.collections.HashMap

object EntityCacheArithmetic : Arithmetic {
	override fun arithName() = "entity_cache"
	override fun opTypes() = listOf(APPEND, REMOVE, INDEX_OF, ABS, AND, XOR, OR)
	override fun getOperator(pattern: HexPattern): Operator {
		return when (pattern) {
			APPEND -> OpEntityCacheAdd
			REMOVE -> OpEntityCacheRemove
			INDEX_OF -> OpEntityCacheTest
			ABS -> OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(EntityCacheIota.TYPE))) { cache: Iota -> DoubleIota(downcast(cache, EntityCacheIota.TYPE).contents.size.toDouble()) }
			OR -> OpEntityCacheBinary { a, b -> HashMap(a + b) }
			AND -> OpEntityCacheBinary { a, b -> HashMap(a.filter { (key, value) -> b[key] == value }) }
			XOR -> OpEntityCacheBinary { a, b ->
				val onlyInThis = a.filterKeys { it !in b }
				val onlyInOther = b.filterKeys { it !in a }
				HashMap(onlyInThis + onlyInOther)
			}
			else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
		}
	}
}

object OpEntityCacheAdd : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(EntityCacheIota.TYPE), IotaPredicate.ofType(EntityIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as EntityCacheIota).contents.clone() as HashMap<UUID, String>
		val entity = (iterator.next() as EntityIota).entity
		list[entity.uuid] = entity.entityName
		return listOf(EntityCacheIota(list))
	}
}

object OpEntityCacheRemove : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(EntityCacheIota.TYPE), IotaPredicate.ofType(DoubleIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as EntityCacheIota).contents.clone() as HashMap<UUID, String>
		list.remove((iterator.next() as EntityIota).entity.uuid)
		return listOf(EntityCacheIota(list))
	}
}

object OpEntityCacheTest : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(EntityCacheIota.TYPE), IotaPredicate.ofType(DoubleIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as EntityCacheIota).contents
		return list.contains((iterator.next() as EntityIota).entity.uuid).asActionResult
	}
}

class OpEntityCacheBinary(private val process: (HashMap<UUID, String>, HashMap<UUID, String>) -> HashMap<UUID, String>) : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(EntityCacheIota.TYPE), IotaPredicate.ofType(EntityCacheIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val a = (iterator.next() as EntityCacheIota).contents
		val b = (iterator.next() as EntityCacheIota).contents
		return listOf(EntityCacheIota(process(a, b)))
	}
}