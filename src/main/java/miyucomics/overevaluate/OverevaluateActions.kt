package miyucomics.overevaluate

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.casting.actions.stack.OpTwiddling
import at.petrak.hexcasting.common.lib.hex.HexActions
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.overevaluate.actions.OpDioscuriII
import miyucomics.overevaluate.actions.jumble.OpCreateJumble
import miyucomics.overevaluate.actions.jumble.OpDissolveJumble
import miyucomics.overevaluate.actions.list_utils.OpRotateList
import miyucomics.overevaluate.actions.list_utils.OpSnapList
import miyucomics.overevaluate.actions.metaevals.*
import miyucomics.overevaluate.actions.soroban.OpSorobanPop
import miyucomics.overevaluate.actions.soroban.OpSorobanPush
import miyucomics.overevaluate.actions.soroban.OpSorobanRetrieve
import miyucomics.overevaluate.handlers.*
import net.minecraft.registry.Registry

object OverevaluateActions {
	fun init() {
		register("rotate_list", "wwaadaqadae", HexDir.EAST, OpRotateList)
		register("snap_list", "eawdq", HexDir.EAST, OpSnapList)

		register("swap_one_three", "ddwqaq", HexDir.NORTH_EAST, OpTwiddling(3, intArrayOf(2, 1, 0)))
		register("swap_two_three", "aawede", HexDir.EAST, OpTwiddling(3, intArrayOf(1, 0, 2)))
		register("dup_many", "waadadaa", HexDir.EAST, OpDioscuriII)

		register("create_jumble", "deaqd", HexDir.WEST, OpCreateJumble)
		register("dissolve_jumble", "aedqa", HexDir.SOUTH_WEST, OpDissolveJumble)

		register("soroban_retrieve", "waqdee", HexDir.SOUTH_EAST, OpSorobanRetrieve)
		register("soroban_push", "wdeaqq", HexDir.NORTH_EAST, OpSorobanPush)
		register("soroban_pop", "qdeeaae", HexDir.NORTH_EAST, OpSorobanPop)

		register("apep", "dqd", HexDir.EAST, OpApep)
		register("athena", "dweaqqw", HexDir.SOUTH_EAST, OpAthena)
		register("athena_print", "dweaqqqqa", HexDir.SOUTH_EAST, OpAthenaPrint)
		register("athena_log", "dweaqqqqd", HexDir.SOUTH_EAST, OpAthenaLog)
		register("atalanta", "aqdea", HexDir.SOUTH_WEST, OpAtalanta)
		register("castor", "adadee", HexDir.NORTH_WEST, OpCastor)
		register("pollux", "dadaqq", HexDir.NORTH_EAST, OpPollux)
		register("janus", "aadee", HexDir.SOUTH_WEST, OpJanus)
		register("maat", "qed", HexDir.NORTH_EAST, OpMaat)
		register("sisyphus", "qaqwede", HexDir.NORTH_EAST, OpSisyphus)
		register("themis", "dwaad", HexDir.WEST, OpThemis)
		register("tutu", "eedqa", HexDir.WEST, OpTutu)

		registerSpecialHandler("geb", GebHandler.Factory())
		registerSpecialHandler("nut", NutHandler.Factory())
		registerSpecialHandler("nephthys", NephthysHandler.Factory())
		registerSpecialHandler("sekhmet", SekhmetHandler.Factory())
		registerSpecialHandler("root", RootHandler.Factory())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(
			HexActions.REGISTRY, OverevaluateMain.id(name),
			ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action)
		)
	private fun <T : SpecialHandler> registerSpecialHandler(name: String, handler: SpecialHandler.Factory<T>) =
		Registry.register(
			IXplatAbstractions.INSTANCE.specialHandlerRegistry, OverevaluateMain.id(name),
			handler
		)
}