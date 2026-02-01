package com.startraveler.celebratoryspruce.datagen.model;


import com.startraveler.celebratoryspruce.CelebratorySpruce;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;

public class CelebratorySpruceModelTemplates {

    public static final ModelTemplate LOG_FIRE = ModelTemplates.create(
            CelebratorySpruce.id("template_log_fire").toString(),
            TextureSlot.FIRE,
            TextureSlot.LIT_LOG,
            CelebratorySpruceTextureSlot.LOG,
            CelebratorySpruceTextureSlot.BASE
    );

    public static final ModelTemplate OVERLAID_WREATH = ModelTemplates.create(
            CelebratorySpruce.id("template_overlaid_wreath").toString(),
            TextureSlot.FRONT,
            TextureSlot.ALL,
            CelebratorySpruceTextureSlot.OVERLAY,
            CelebratorySpruceTextureSlot.OVERLAY_FRONT,
            TextureSlot.PARTICLE
    );

    public static final ModelTemplate OVERLAID_WALL_WREATH = ModelTemplates.create(
            CelebratorySpruce.id("template_overlaid_wall_wreath").toString(),
            TextureSlot.FRONT,
            TextureSlot.ALL,
            CelebratorySpruceTextureSlot.OVERLAY,
            CelebratorySpruceTextureSlot.OVERLAY_FRONT,
            TextureSlot.PARTICLE
    );

    public static final ModelTemplate WREATH = ModelTemplates.create(
            CelebratorySpruce.id("template_wreath").toString(),
            TextureSlot.FRONT,
            TextureSlot.ALL,
            TextureSlot.PARTICLE
    );

    public static final ModelTemplate WALL_WREATH = ModelTemplates.create(
            CelebratorySpruce.id("template_wall_wreath")
                    .toString(),
            TextureSlot.FRONT,
            TextureSlot.ALL,
            TextureSlot.PARTICLE
    );

    public static final ModelTemplate MULTIFACE_FACE = ModelTemplates.create(
            CelebratorySpruce.id("multiface_face")
                    .toString(), CelebratorySpruceTextureSlot.FACE, TextureSlot.PARTICLE
    );

    public static final ModelTemplate MULTIFACE_FACE_EMISSIVE = ModelTemplates.create(
            CelebratorySpruce.id(
                    "multiface_face_emissive").toString(),
            CelebratorySpruceTextureSlot.FACE,
            TextureSlot.PARTICLE
    );


    public static final ModelTemplate ASTERISK = ModelTemplates.create(
            CelebratorySpruce.id("asterisk").toString(),
            TextureSlot.CROSS,
            CelebratorySpruceTextureSlot.PLUS
    );

    public static final ModelTemplate DOUBLE_SIDED_CUBE_COLUMN = ModelTemplates.create(
            CelebratorySpruce.id(
                    "double_sided_cube_column").toString(), TextureSlot.END, TextureSlot.SIDE
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

    public static ModelTemplate boxPile(Integer boxes) {
        return ModelTemplates.create(
                CelebratorySpruce.id("box_pile_stack" + boxes).toString(),
                TextureSlot.PARTICLE,
                CelebratorySpruceTextureSlot.FLOWER,
                TextureSlot.SIDE
        );
    }

    public static ModelTemplate tintedBoxPile(Integer boxes) {
        return ModelTemplates.create(
                CelebratorySpruce.id("tinted_box_pile_stack" + boxes).toString(),
                TextureSlot.PARTICLE,
                CelebratorySpruceTextureSlot.LAYER0_FLOWER,
                CelebratorySpruceTextureSlot.LAYER0_SIDE,
                CelebratorySpruceTextureSlot.LAYER1_FLOWER,
                CelebratorySpruceTextureSlot.LAYER1_SIDE
        );
    }
}

