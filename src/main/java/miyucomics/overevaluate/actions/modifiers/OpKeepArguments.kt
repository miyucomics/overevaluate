package miyucomics.overevaluate.actions.modifiers

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import net.minecraft.nbt.NbtElement

object OpKeepArguments : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation) = OperationResult(
		image.copy(
			userData = image.userData.copy().apply { putInt("keep_arguments", 2) },
			opsConsumed = image.opsConsumed + 1
		), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE
	)

	@JvmStatic
	fun shouldKeepArguments(image: CastingImage) = image.userData.contains("keep_arguments", NbtElement.INT_TYPE.toInt()) && image.userData.getInt("keep_arguments") == 1
}