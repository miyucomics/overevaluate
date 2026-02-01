package miyucomics.overevaluate.handlers

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import at.petrak.hexcasting.api.utils.lightPurple
import miyucomics.overevaluate.actions.handlers.OpRoot

class RootHandler(private val n: Int) : SpecialHandler {
	override fun act() = OpRoot(n)
	override fun getName() = "hexcasting.special.overevaluate:root".asTranslatedComponent(n).lightPurple

	class Factory : SpecialHandler.Factory<RootHandler> {
		override fun tryMatch(pattern: HexPattern, env: CastingEnvironment): RootHandler? {
			val prefix = "awe"
			val sig = pattern.anglesSignature()
			if (sig.startsWith(prefix)) {
				val suffix = sig.substring(prefix.length)
				if (suffix.all { it == 'w' })
					return RootHandler(1 + suffix.length)
			}
			return null
		}
	}
}