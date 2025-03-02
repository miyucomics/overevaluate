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
import at.petrak.hexcasting.api.casting.math.HexPattern
import miyucomics.overevaluate.iotas.DoubleCacheIota

object DoubleCacheArithmetic : Arithmetic {
	override fun arithName() = "double_cache"
	override fun opTypes() = listOf(APPEND, REMOVE, INDEX_OF, ABS, AND, XOR, OR)
	override fun getOperator(pattern: HexPattern): Operator {
		return when (pattern) {
			APPEND -> OpDoubleCacheAdd
			REMOVE -> OpDoubleCacheRemove
			INDEX_OF -> OpDoubleCacheTest
			ABS -> OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(DoubleCacheIota.TYPE))) { cache: Iota -> DoubleIota(downcast(cache, DoubleCacheIota.TYPE).contents.size.toDouble()) }
			OR -> OpDoubleCacheBinary { a, b -> HashSet(a union b) }
			AND -> OpDoubleCacheBinary { a, b -> HashSet(a.intersect(b)) }
			XOR -> OpDoubleCacheBinary { a, b -> HashSet((a subtract b) union (b subtract a)) }
			else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
		}
	}
}

object OpDoubleCacheAdd : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(DoubleCacheIota.TYPE), IotaPredicate.ofType(DoubleIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as DoubleCacheIota).contents.clone() as HashSet<Double>
		list.add((iterator.next() as DoubleIota).double)
		return listOf(DoubleCacheIota(list))
	}
}

object OpDoubleCacheRemove : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(DoubleCacheIota.TYPE), IotaPredicate.ofType(DoubleIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as DoubleCacheIota).contents.clone() as HashSet<Double>
		list.remove((iterator.next() as DoubleIota).double)
		return listOf(DoubleCacheIota(list))
	}
}

object OpDoubleCacheTest : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(DoubleCacheIota.TYPE), IotaPredicate.ofType(DoubleIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val list = (iterator.next() as DoubleCacheIota).contents
		val test = (iterator.next() as DoubleIota).double
		return list.contains(test).asActionResult
	}
}

class OpDoubleCacheBinary(private val process: (HashSet<Double>, HashSet<Double>) -> HashSet<Double>) : OperatorBasic(2, IotaMultiPredicate.pair(IotaPredicate.ofType(DoubleCacheIota.TYPE), IotaPredicate.ofType(DoubleCacheIota.TYPE))) {
	override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
		val iterator = iotas.iterator()
		val a = (iterator.next() as DoubleCacheIota).contents
		val b = (iterator.next() as DoubleCacheIota).contents
		return listOf(DoubleCacheIota(process(a, b)))
	}
}