package miyucomics.overevaluate.mixin;

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.overevaluate.actions.modifiers.OpKeepArguments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ConstMediaAction.DefaultImpls.class, remap = false)
public class ConstMediaActionMixin {
	@Inject(method = "operate", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction;executeWithOpCount(Ljava/util/List;Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)Lat/petrak/hexcasting/api/casting/castables/ConstMediaAction$CostMediaActionResult;"))
	private static void dontEatArguments(ConstMediaAction $this, CastingEnvironment env, CastingImage image, SpellContinuation continuation, CallbackInfoReturnable<OperationResult> cir, @Local(name = "args") List<Iota> args, @Local(name = "stack") List<Iota> stack) {
		if (OpKeepArguments.shouldKeepArguments(image))
			stack.addAll(args);
	}
}