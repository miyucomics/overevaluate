package miyucomics.overevaluate

import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.overevaluate.actions.jumble.JumbleIota
import miyucomics.overevaluate.frames.ApepFrame
import miyucomics.overevaluate.frames.AthenaFrame
import miyucomics.overevaluate.frames.NephthysFrame
import miyucomics.overevaluate.frames.SisyphusFrame
import miyucomics.overevaluate.frames.ThemisFrame
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class OverevaluateMain : ModInitializer {
	val continuationRegistry: Registry<ContinuationFrame.Type<*>> = IXplatAbstractions.INSTANCE.continuationTypeRegistry

	override fun onInitialize() {
		OverevaluateActions.init()
		Registry.register(continuationRegistry, id("apep"), ApepFrame.TYPE)
		Registry.register(continuationRegistry, id("athena"), AthenaFrame.TYPE)
		Registry.register(continuationRegistry, id("nephthys"), NephthysFrame.TYPE)
		Registry.register(continuationRegistry, id("sisyphus"), SisyphusFrame.TYPE)
		Registry.register(continuationRegistry, id("themis"), ThemisFrame.TYPE)
		Registry.register(HexIotaTypes.REGISTRY, id("jumble"), JumbleIota.TYPE)
	}

	companion object {
		const val MOD_ID: String = "overevaluate"
		fun id(string: String) = Identifier(MOD_ID, string)
	}
}