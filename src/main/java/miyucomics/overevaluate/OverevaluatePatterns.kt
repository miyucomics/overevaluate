package miyucomics.overevaluate

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.actions.stack.OpTwiddling
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.overevaluate.handlers.NephthysSpecialHandler
import miyucomics.overevaluate.handlers.SekhmetSpecialHandler
import miyucomics.overevaluate.patterns.*
import miyucomics.overevaluate.patterns.metaevals.*
import miyucomics.overevaluate.patterns.soroban.OpSorobanDecrement
import miyucomics.overevaluate.patterns.soroban.OpSorobanIncrement
import miyucomics.overevaluate.patterns.soroban.OpSorobanReset
import net.minecraft.registry.Registry

object OverevaluatePatterns {
	@JvmStatic
	fun init() {
		register("swap_one_three", "ddwqaq", HexDir.NORTH_EAST, OpTwiddling(3, intArrayOf(2, 1, 0)))
		register("swap_two_three", "aawede", HexDir.EAST, OpTwiddling(3, intArrayOf(1, 0, 2)))
		register("dup_many", "waadadaa", HexDir.EAST, OpDupMany())

		register("soroban_decrement", "waqdee", HexDir.SOUTH_EAST, OpSorobanDecrement())
		register("soroban_increment", "wdeaqq", HexDir.NORTH_EAST, OpSorobanIncrement())
		register("soroban_reset", "qdeeaae", HexDir.NORTH_EAST, OpSorobanReset())

		register("atalanta", "aqdea", HexDir.SOUTH_WEST, OpAtalanta)
		register("castor", "adadee", HexDir.NORTH_WEST, OpCastor)
		register("pollux", "dadaqq", HexDir.NORTH_EAST, OpPollux)
		register("janus", "aadee", HexDir.SOUTH_WEST, OpJanus)
		register("maat", "qed", HexDir.NORTH_EAST, OpMaat())
		register("sisyphus", "qaqwede", HexDir.NORTH_EAST, OpSisyphus)
		register("themis", "dwaad", HexDir.WEST, OpThemis)
		register("tutu", "eedqa", HexDir.WEST, OpTutu)

		registerSpecialHandler("nephthys", NephthysSpecialHandler.Factory())
		registerSpecialHandler("sekhmet", SekhmetSpecialHandler.Factory())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, OverevaluateMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
	private fun <T : SpecialHandler> registerSpecialHandler(name: String, handler: SpecialHandler.Factory<T>) =
		Registry.register(IXplatAbstractions.INSTANCE.specialHandlerRegistry, OverevaluateMain.id(name), handler)
}