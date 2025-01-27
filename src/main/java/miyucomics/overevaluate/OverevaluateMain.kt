package miyucomics.overevaluate

import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.overevaluate.patterns.blackbox.BlackboxArithmetic
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class OverevaluateMain : ModInitializer {
	override fun onInitialize() {
		OverevaluatePatterns.init()
		Registry.register(HexIotaTypes.REGISTRY, id("property"), BlackboxIota.TYPE)
		Registry.register(HexArithmetics.REGISTRY, BlackboxArithmetic.arithName(), BlackboxArithmetic)
	}

	companion object {
		const val MOD_ID: String = "overevaluate"
		fun id(string: String) = Identifier(MOD_ID, string)
	}
}