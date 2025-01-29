package miyucomics.overevaluate.patterns.metaevals

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.minecraft.text.Text

object OpAthenaPrint : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		if (image.userData.contains("last_mishap"))
			env.printMessage(Text.Serializer.fromJson(image.userData.getString("last_mishap")))
		return OperationResult(image, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}