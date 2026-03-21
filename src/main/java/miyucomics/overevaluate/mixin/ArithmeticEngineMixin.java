package miyucomics.overevaluate.mixin;

import at.petrak.hexcasting.api.casting.arithmetic.engine.ArithmeticEngine;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.llamalad7.mixinextras.sugar.Local;
import miyucomics.overevaluate.actions.modifiers.OpKeepArguments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(value = ArithmeticEngine.class, remap = false)
public class ArithmeticEngineMixin {
	@ModifyArg(method = "run", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/arithmetic/operator/Operator;operate(Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;Lat/petrak/hexcasting/api/casting/eval/vm/CastingImage;Lat/petrak/hexcasting/api/casting/eval/vm/SpellContinuation;)Lat/petrak/hexcasting/api/casting/eval/OperationResult;"), index = 1)
	private static CastingImage dontEatArguments(CastingImage original, @Local(name = "args") ArrayList<Iota> args) {
		if (OpKeepArguments.shouldKeepArguments(original)) {
			ArrayList<Iota> newStack = new ArrayList<>(original.getStack());
			List<Iota> shallowCopy = args.subList(0, args.size());
			Collections.reverse(shallowCopy);
			newStack.addAll(shallowCopy);
			return original.copy(newStack, original.getParenCount(), original.getParenthesized(), original.getEscapeNext(), original.getOpsConsumed(), original.getUserData());
		}
		return original;
	}
}