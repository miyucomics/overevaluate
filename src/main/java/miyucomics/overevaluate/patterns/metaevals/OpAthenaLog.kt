package miyucomics.overevaluate.patterns.metaevals

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text
import ram.talia.moreiotas.api.casting.iota.StringIota

object OpAthenaLog : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		val stackCopy = image.stack.toMutableList()

		if (FabricLoader.getInstance().isModLoaded("moreiotas")) {
			var output = "No mishap."
			if (image.userData.contains("last_mishap"))
				output = Text.Serializer.fromJson(image.userData.getString("last_mishap"))!!.string
			stackCopy.add(StringIota.make(output))
		} else {
			stackCopy.add(GarbageIota())
		}

		return OperationResult(image.withUsedOp().copy(stack = stackCopy), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}