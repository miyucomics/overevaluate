package miyucomics.overevaluate.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.overevaluate.frames.AthenaFrame;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CastingVM.class)
public class CastingVMMixin {
	@WrapMethod(method = "executeInner")
	private CastResult onMishapCatch(Iota iota, ServerWorld world, SpellContinuation continuation, Operation<CastResult> original) {
		return AthenaFrame.handleAthena(iota, (CastingVM) (Object) this, world, continuation, original);
	}
}