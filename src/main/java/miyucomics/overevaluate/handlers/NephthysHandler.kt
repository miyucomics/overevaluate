package miyucomics.overevaluate.handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.overevaluate.actions.handlers.OpNephthys

class NephthysHandler(private val depth: Int) : SpecialHandler {
	override fun act() = OpNephthys(depth)
	override fun getName() = "hexcasting.special.overevaluate:nephthys".asTranslatedComponent(depth).lightPurple

	class Factory : SpecialHandler.Factory<NephthysHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): NephthysHandler? {
			val sig = pattern.anglesSignature()
			if (sig.startsWith("deaqqd")) {
				var depth = 1
				sig.substring(6).forEachIndexed { index, char ->
					if (char != "qe"[index % 2])
						return null
					depth += 1
				}
				return NephthysHandler(depth)
			} else {
				return null
			}
		}
	}
}