package miyucomics.overevaluate.handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.overevaluate.actions.handlers.OpPluto

class PlutoHandler(private val frameCount: Int) : SpecialHandler {
	override fun act() = OpPluto(frameCount)
	override fun getName() = "hexcasting.special.overevaluate:pluto".asTranslatedComponent(frameCount).lightPurple

	class Factory : SpecialHandler.Factory<PlutoHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): PlutoHandler? {
			val sig = pattern.anglesSignature()
			if (sig.startsWith("aqdeea")) {
				var frameCount = 1
				sig.substring(6).forEachIndexed { index, char ->
					if (char != "eq"[index % 2])
						return null
					frameCount += 1
				}
				return PlutoHandler(frameCount)
			} else {
				return null
			}
		}
	}
}