package net.hootisman.bananas.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hootisman.bananas.entity.DoughBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DoughBlockRenderer implements BlockEntityRenderer<DoughBlockEntity> {
    @Override
    public void render(DoughBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource source, int combinedLight, int combinedOverlay) {

    }
}
