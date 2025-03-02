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
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import miyucomics.overevaluate.iotas.VectorCacheIota
import net.minecraft.util.math.Vec3d

object VectorCacheArithmetic : Arithmetic {
	override fun arithName() = "vector_cache"
	override fun opTypes() = listOf(APPEND, REMOVE, INDEX_OF, ABS, AND, XOR, OR)
	override fun getOperator(pattern: HexPattern): Operator {
		return when (pattern) {
			APPEND -> OpVectorCacheAdd
			REMOVE -> OpVectorCacheRemove
			INDEX_OF -> OpVectorCacheTest
			ABS -> OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(VectorCacheIota.TYPE))) { cache: Iota -> DoubleIota(downcast(cache, VectorCacheIota.TYPE).contents.size.toDouble()) }
			OR -> OpVectorCacheBinary { a, b -> HashSet(a union b) }
			AND -> OpVectorCacheBinary { a, b -> HashSet(a.intersect(b)) }
			XOR -> OpVectorCacheBinary { a, b -> HashSet((a subtract b) union (b subtract a)) }
			else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
		}
	}
}

object OpVectorCacheAdd : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(VectorCacheIota.TYPE), IotaPredicate.ofType(Vec3Iota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as VectorCacheIota).contents.clone() as HashSet<Vec3d>
		list.add((iterator.next() as Vec3Iota).vec3)
		return listOf(VectorCacheIota(list))
	}
}

object OpVectorCacheRemove : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(VectorCacheIota.TYPE), IotaPredicate.ofType(Vec3Iota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as VectorCacheIota).contents.clone() as HashSet<Vec3d>
		list.remove((iterator.next() as Vec3Iota).vec3)
		return listOf(VectorCacheIota(list))
	}
}

object OpVectorCacheTest : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(VectorCacheIota.TYPE), IotaPredicate.ofType(Vec3Iota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as VectorCacheIota).contents
		val test = (iterator.next() as Vec3Iota).vec3
		return list.contains(test).asActionResult
	}
}

class OpVectorCacheBinary(private val process: (HashSet<Vec3d>, HashSet<Vec3d>) -> HashSet<Vec3d>) : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(VectorCacheIota.TYPE), IotaPredicate.ofType(VectorCacheIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val a = (iterator.next() as VectorCacheIota).contents
		val b = (iterator.next() as VectorCacheIota).contents
		return listOf(VectorCacheIota(process(a, b)))
	}
}