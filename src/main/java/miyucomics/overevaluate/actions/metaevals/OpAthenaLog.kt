package miyucomics.overevaluate.actions.metaevals

import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.GarbageIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import miyucomics.hexpose.iotas.TextIota
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text

object OpAthenaLog : Action {
	override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
		if (!image.userData.contains("last_mishap"))
			return OperationResult(image.withUsedOp().copy(stack = image.stack.plus(NullIota())), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)

		val newIota = if (FabricLoader.getInstance().isModLoaded("hexpose")) TextIota(Text.Serializer.fromJson(image.userData.getString("last_mishap"))!!) else GarbageIota()
		return OperationResult(image.withUsedOp().copy(stack = image.stack.plus(newIota)), listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE)
	}
}