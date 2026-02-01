package miyucomics.overevaluate.actions.math

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.util.math.Vec3d

object OpHadamard : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val a = args.getVec3(0, argc)
		val b = args.getVec3(1, argc)
		return Vec3d(a.x * b.x, a.y * b.y, a.z * b.z).asActionResult
	}
}