package miyucomics.overevaluate.actions.jumble

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import kotlin.math.abs
import kotlin.math.roundToInt

object OpCreateJumble : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val argCount = args.getPositiveInt(0, argc)
		val jumble = args.getList(1, argc).map { it ->
			if (it !is DoubleIota)
				throw MishapInvalidIota.of(args[0], 0, "jumble_list")
			val double = it.double
			val rounded = double.roundToInt()
			if (abs(double - rounded) > DoubleIota.TOLERANCE)
				throw MishapInvalidIota.of(args[0], 0, "jumble_list")
			if (rounded >= argCount)
				throw MishapInvalidIota.of(args[0], 0, "jumble_list")
			rounded
		}
		return listOf(JumbleIota(Jumble(argCount, jumble)))
	}
}
