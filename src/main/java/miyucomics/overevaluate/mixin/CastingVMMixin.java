package miyucomics.overevaluate.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import miyucomics.overevaluate.frames.AthenaFrame;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CastingVM.class)
public class CastingVMMixin {
	@WrapMethod(method = "executeInner")
	private CastResult onMishapCatch(Iota iota, ServerWorld world, SpellContinuation continuation, Operation<CastResult> original) {
		return AthenaFrame.handleAthena(iota, (CastingVM) (Object) this, world, continuation, original);
	}

	@Inject(method = "queueExecuteAndWrapIotas", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;postExecution(Lat/petrak/hexcasting/api/casting/eval/CastResult;)V"))
	private void decrementBelowness(List<? extends Iota> iotas, ServerWorld world, CallbackInfoReturnable<ExecutionClientView> cir) {
		NbtCompound userData = ((CastingVM) (Object) this).getImage().getUserData();
		if (userData.contains("keep_arguments", NbtElement.INT_TYPE)) {
			switch (userData.getInt("keep_arguments")) {
				case 2 -> userData.putInt("keep_arguments", 1);
				case 1 -> userData.remove("keep_arguments");
			}
		}
	}
}