package miyucomics.overevaluate

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

class OverevaluateMain : ModInitializer {
	override fun onInitialize() {
		OverevaluateActions.init()
	}

	companion object {
		const val MOD_ID: String = "overevaluate"
		fun id(string: String) = Identifier(MOD_ID, string)
	}
}