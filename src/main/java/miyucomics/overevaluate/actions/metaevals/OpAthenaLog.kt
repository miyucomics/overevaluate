package miyucomics.overevaluate.actions.metaevals

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.TextIota
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text

object OpAthenaLog : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stack = image.stack.toMutableList()

		if (FabricLoader.getInstance().isModLoaded("hexpose")) {
			var output = Text.literal("No mishap.")
			if (image.userData.contains("last_mishap"))
				output = Text.Serializer.fromJson(image.userData.getString("last_mishap"))!!
			stack.add(TextIota(output))
		} else {
			stack.add(GarbageIota())
		}

		return OperationResult(image.withUsedOp().copy(stack = stack), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}