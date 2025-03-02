package miyucomics.overevaluate

import at.petrak.hexcasting.common.lib.hex.HexArithmetics
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import miyucomics.overevaluate.arithmetic.DoubleCacheArithmetic
import miyucomics.overevaluate.arithmetic.EntityCacheArithmetic
import miyucomics.overevaluate.arithmetic.VectorCacheArithmetic
import miyucomics.overevaluate.iotas.DoubleCacheIota
import miyucomics.overevaluate.iotas.EntityCacheIota
import miyucomics.overevaluate.iotas.VectorCacheIota
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class OverevaluateMain : ModInitializer {
	override fun onInitialize() {
		OverevaluatePatterns.init()
		Registry.register(HexIotaTypes.REGISTRY, id("cache_double"), DoubleCacheIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("cache_entity"), EntityCacheIota.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("cache_vector"), VectorCacheIota.TYPE)
		Registry.register(HexArithmetics.REGISTRY, DoubleCacheArithmetic.arithName(), DoubleCacheArithmetic)
		Registry.register(HexArithmetics.REGISTRY, EntityCacheArithmetic.arithName(), EntityCacheArithmetic)
		Registry.register(HexArithmetics.REGISTRY, VectorCacheArithmetic.arithName(), VectorCacheArithmetic)
	}

	companion object {
		const val MOD_ID: String = "overevaluate"
		fun id(string: String) = Identifier(MOD_ID, string)
	}
}