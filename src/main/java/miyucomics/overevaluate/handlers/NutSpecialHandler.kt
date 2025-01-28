package miyucomics.overevaluate.handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.overevaluate.patterns.OpNut

class NutSpecialHandler(private val amount: Int) : SpecialHandler {
	override fun act() = OpNut(amount)
	override fun getName() = "special.overevaluate.nut".asTranslatedComponent(amount).lightPurple

	class Factory : SpecialHandler.Factory<NutSpecialHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): NutSpecialHandler? {
			val sig = pattern.anglesSignature()
			if (sig.startsWith("aawdde")) {
				var amount = 1
				sig.substring(6).forEach { char ->
					if (char != 'w')
						return null
					amount += 1
				}
				return NutSpecialHandler(amount)
			} else {
				return null
			}
		}
	}
}