package miyucomics.overevaluate.patterns.blackbox

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.overevaluate.BlackboxIota

class OpBlackboxCreate : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment) =
		listOf(BlackboxIota(hashSetOf()))
}