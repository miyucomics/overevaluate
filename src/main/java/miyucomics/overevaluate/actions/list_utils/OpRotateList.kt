package miyucomics.overevaluate.actions.list_utils

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.iota.Iota
import com.ibm.icu.text.PluralRules

object OpRotateList : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val list = args.getList(0, argc).toList()
		if (list.isEmpty())
			return list.asActionResult
		val n = args.getInt(1, argc)
		val k = ((n % list.size) + list.size) % list.size
		if (k == 0)
			return list
		return (list.drop(k) + list.take(k)).asActionResult
	}
}