package YOUR_PACKAGE.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.particle.ParticleTypes;

public class VillagerStaffItem extends Item {
    private static final int COOLDOWN_TICKS = 6000; // 120 seconds (20 ticks per sec)

    public VillagerStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        
        if (player.getItemCooldownManager().isCoolingDown(this)) {
            return ActionResult.FAIL; // Prevent use if cooldown is active
        }

        ActionResult result = this.spawnTNT(world, player);

        if (result.isAccepted()) {
            player.getItemCooldownManager().set(this, COOLDOWN_TICKS); // Apply cooldown
        }

        return result;
    }

    private ActionResult spawnTNT(World world, PlayerEntity player) {
        if (world.isClient) return ActionResult.SUCCESS;

        HitResult hit = player.raycast(50, 0, false);
        Vec3d pos = hit.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) hit).getPos()
                : hit.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hit).getEntity().getPos()
                : player.getPos().add(player.getRotationVec(1).multiply(5));

        for (int i = 0; i < 5; i++) { // Spawn 5 instant-exploding TNTs
            TntEntity tnt = new TntEntity(world, pos.x, pos.y + i * 0.5, pos.z, player);
            tnt.setFuse(0);
            world.spawnEntity(tnt);
        }

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 20, 0.5, 0.5, 0.5, 0.1);
        }

        return ActionResult.SUCCESS;
    }
}
