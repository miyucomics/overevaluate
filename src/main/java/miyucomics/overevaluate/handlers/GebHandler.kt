package miyucomics.overevaluate.handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.overevaluate.actions.handlers.OpGeb

class GebHandler(private val amount: Int) : SpecialHandler {
	override fun act() = OpGeb(amount)
	override fun getName() = "hexcasting.special.overevaluate:geb".asTranslatedComponent(amount).lightPurple

	class Factory : SpecialHandler.Factory<GebHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): GebHandler? {
			val sig = pattern.anglesSignature()
			if (sig.startsWith("aaeaad")) {
				var amount = 1
				sig.substring(6).forEach { char ->
					if (char != 'w')
						return null
					amount += 1
				}
				return GebHandler(amount)
			} else {
				return null
			}
		}
	}
}