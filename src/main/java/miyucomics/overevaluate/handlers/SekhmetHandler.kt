package miyucomics.overevaluate.handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.overevaluate.actions.handlers.OpSekhmet

class SekhmetHandler(private val perserve: Int) : SpecialHandler {
	override fun act() = OpSekhmet(perserve)
	override fun getName() = "special.overevaluate.sekhmet".asTranslatedComponent(perserve).lightPurple

	class Factory : SpecialHandler.Factory<SekhmetHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): SekhmetHandler? {
			val sig = pattern.anglesSignature()
			if (sig.startsWith("qaqdd")) {
				var perserve = 0
				sig.substring(5).forEachIndexed { index, char ->
					if (char != "qe"[index % 2])
						return@tryMatch null
					perserve += 1
				}
				return SekhmetHandler(perserve)
			}
			return null
		}
	}
}