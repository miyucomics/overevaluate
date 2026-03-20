package miyucomics.overevaluate.mixin;

import at.petrak.hexcasting.api.casting.castables.SpellAction;
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

@Mixin(value = SpellAction.DefaultImpls.class, remap = false)
public class SpellActionMixin {
	@Inject(method = "operate", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/vm/CastingImage;copy$default(Lat/petrak/hexcasting/api/casting/eval/vm/CastingImage;Ljava/util/List;ILjava/util/List;ZJLnet/minecraft/nbt/NbtCompound;ILjava/lang/Object;)Lat/petrak/hexcasting/api/casting/eval/vm/CastingImage;"))
	private static void dontEatArguments(SpellAction $this, CastingEnvironment env, CastingImage image, SpellContinuation continuation, CallbackInfoReturnable<OperationResult> cir, @Local(name = "args") List<Iota> args, @Local(name = "stack") List<Iota> stack) {
		if (OpKeepArguments.shouldKeepArguments(image))
			stack.addAll(args);
	}
}