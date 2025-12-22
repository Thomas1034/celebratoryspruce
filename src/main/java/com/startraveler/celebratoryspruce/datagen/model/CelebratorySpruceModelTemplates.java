package com.startraveler.celebratoryspruce.datagen.model;


import com.startraveler.celebratoryspruce.CelebratorySpruce;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;

public class CelebratorySpruceModelTemplates {

    public static final ModelTemplate ASTERISK = ModelTemplates.create(
            CelebratorySpruce.id("asterisk").toString(),
            TextureSlot.CROSS,
            CelebratorySpruceTextureSlot.PLUS
    );

    public static final ModelTemplate DOUBLE_SIDED_CUBE_COLUMN = ModelTemplates.create(
            CelebratorySpruce.id("double_sided_cube_column").toString(),
            TextureSlot.END,
            TextureSlot.SIDE
    );

    public static final ModelTemplate OVERLAID_CUBE = ModelTemplates.create(
            CelebratorySpruce.id("overlaid_cube").toString(),
            TextureSlot.PARTICLE,
            CelebratorySpruceTextureSlot.BASE,
            CelebratorySpruceTextureSlot.OVERLAY
    );



    public static final ModelTemplate TOP_OVERLAID_CUBE = ModelTemplates.create(
            CelebratorySpruce.id("top_overlaid_cube").toString(),
            TextureSlot.PARTICLE,
            CelebratorySpruceTextureSlot.BASE,
            CelebratorySpruceTextureSlot.OVERLAY,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            TextureSlot.BOTTOM
    );
    public static final ModelTemplate OVERLAID_LEAVES = ModelTemplates.create(
            CelebratorySpruce.id("overlaid_leaves").toString(),
            TextureSlot.PARTICLE,
            TextureSlot.ALL,
            CelebratorySpruceTextureSlot.OVERLAY
    );
    public static final ModelTemplate OVERLAID_LEAVES_EMISSIVE = ModelTemplates.create(
            CelebratorySpruce.id("overlaid_leaves_emissive").toString(),
            TextureSlot.PARTICLE,
            TextureSlot.ALL,
            CelebratorySpruceTextureSlot.OVERLAY
    );
    public static final ModelTemplate OVERLAID_LEAVES_LOW_EMISSIVE = ModelTemplates.create(
            CelebratorySpruce.id("overlaid_leaves_low_emissive").toString(),
            TextureSlot.PARTICLE,
            TextureSlot.ALL,
            CelebratorySpruceTextureSlot.OVERLAY
    );

    public static ModelTemplate boxPile(Integer bombs) {
        return ModelTemplates.create(
                CelebratorySpruce.id("box_pile_stack" + bombs).toString(),
                TextureSlot.PARTICLE,
                CelebratorySpruceTextureSlot.FLOWER,
                TextureSlot.SIDE
        );
    }
}

