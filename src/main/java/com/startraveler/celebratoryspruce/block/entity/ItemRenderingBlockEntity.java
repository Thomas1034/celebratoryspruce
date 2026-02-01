package com.startraveler.celebratoryspruce.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.startraveler.celebratoryspruce.ModBlockEntityTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ItemRenderingBlockEntity extends ItemHoldingBlockEntity implements ItemOwner {
    public static final String DEFAULT_ITEM_KEY = "default_item";
    public static final String TRANSFORMS_KEY = "transforms";
    protected final @NotNull List<Transform<?>> transforms;
    protected @NotNull ItemStack defaultDisplayStack;
    protected @Nullable Matrix4f bakedTransforms;
    protected float bakedYRotation;


    public ItemRenderingBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.ITEM_RENDERING_BLOCK.get(), pos, blockState);
        this.transforms = new ArrayList<>();
        this.defaultDisplayStack = ItemStack.EMPTY;
        this.bakedTransforms = null;
        this.bakedYRotation = 0;
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        // load item and transforms
        this.defaultDisplayStack = input.read(DEFAULT_ITEM_KEY, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.transforms.clear();
        this.transforms.addAll(input.read(TRANSFORMS_KEY, Transform.TRANSFORM_CODEC.listOf()).orElse(List.of()));
        this.bakedTransforms = null;
    }

    // Save values into the passed CompoundTag here.
    @Override
    public void saveAdditional(@NotNull ValueOutput output) {

        super.saveAdditional(output);

        output.store(DEFAULT_ITEM_KEY, ItemStack.OPTIONAL_CODEC, this.defaultDisplayStack);
        output.store(TRANSFORMS_KEY, Transform.TRANSFORM_CODEC.listOf(), this.transforms);

        // save item and transforms
    }

    @Override
    public void markAsChanged() {
        super.markAsChanged();
        this.bakedTransforms = null;
    }

    @SuppressWarnings("unused")
    public void addTransform(Transform<?> transform) {
        this.transforms.addLast(transform);
        this.markAsChanged();
    }

    @SuppressWarnings("unused")
    public void clearTransforms() {
        this.transforms.clear();
        this.markAsChanged();
    }

    public @NotNull ItemStack getDefaultDisplayStack() {
        return this.defaultDisplayStack;
    }

    public void setDefaultDisplayStack(@NotNull ItemStack stack) {
        this.defaultDisplayStack = stack;
        this.markAsChanged();
    }


    public @NotNull ItemStack getStackForDisplay() {
        ItemStack stack = this.getStoredItemStack();
        return stack.isEmpty() ? this.getDefaultDisplayStack() : stack;
    }

    public void addTransforms(@NotNull List<Transform<?>> transforms) {
        this.transforms.addAll(transforms);
        this.markAsChanged();
    }

    @Override
    public @NotNull Level level() {
        return Objects.requireNonNull(this.level);
    }

    @Override
    public @NotNull Vec3 position() {
        return this.getBlockPos().getCenter();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        if (this.bakedTransforms == null) {
            AxisAngle4f angle = this.bakeTransforms().getRotation(new AxisAngle4f()).normalize();
            Vector3f yAxis = new Vector3f(0, -1, 0);
            this.bakedYRotation = Mth.RAD_TO_DEG * angle.angle * (yAxis.dot(angle.x, angle.y, angle.z));
        }
        return this.bakedYRotation;
    }

    public @NotNull Matrix4f bakeTransforms() {
        if (this.bakedTransforms == null) {
            this.bakedTransforms = new Matrix4f();
            for (Transform<?> transform : this.transforms) {
                transform.apply(this.bakedTransforms);
            }
        }
        return this.bakedTransforms;
    }

    public enum TransformType implements StringRepresentable {

        ROTATION("rotation"), TRANSLATION("translation"), SCALE("scale");

        public static final Codec<TransformType> CODEC = StringRepresentable.fromEnum(TransformType::values);
        public static final Function<String, @Nullable TransformType> NAME_LOOKUP = StringRepresentable.createNameLookup(
                values());
        public static final Function<TransformType, String> TYPE_LOOKUP = TransformType::getSerializedName;
        public static final StreamCodec<ByteBuf, TransformType> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
                NAME_LOOKUP,
                TYPE_LOOKUP
        );
        final String type;

        TransformType(String type) {
            this.type = type;
        }

        public @NotNull Codec<? extends Transform<?>> codec() {
            return TransformTypeCodecRegistry.CODECS.get(this);
        }

        public @NotNull StreamCodec<ByteBuf, ? extends @NotNull Transform<?>> streamCodec() {
            return TransformTypeCodecRegistry.STREAM_CODECS.get(this);
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.type;
        }
    }

    public static class TransformTypeCodecRegistry {
        public static final Map<TransformType, Codec<? extends Transform<?>>> CODECS = new HashMap<>();
        public static final Map<TransformType, StreamCodec<ByteBuf, ? extends Transform<?>>> STREAM_CODECS = new HashMap<>();

        public static void init() {
            initCodecs();
            initStreamCodecs();
        }

        public static void initCodecs() {
            CODECS.put(TransformType.ROTATION, Rotation.CODEC);
            CODECS.put(TransformType.TRANSLATION, Translation.CODEC);
            CODECS.put(TransformType.SCALE, Scale.CODEC);
        }

        public static void initStreamCodecs() {
            STREAM_CODECS.put(TransformType.ROTATION, Rotation.STREAM_CODEC);
            STREAM_CODECS.put(TransformType.TRANSLATION, Translation.STREAM_CODEC);
            STREAM_CODECS.put(TransformType.SCALE, Scale.STREAM_CODEC);
        }
    }

    public abstract static class Transform<T> implements Function<Matrix4f, Matrix4f> {

        @SuppressWarnings("unused")
        public static final Codec<Transform<?>> TRANSFORM_CODEC = TransformType.CODEC.dispatch(
                Transform::getType,
                transformType -> transformType.codec().fieldOf("transform")
        );
        @SuppressWarnings("unused")
        public static final StreamCodec<ByteBuf, Transform<?>> TRANSFORM_STREAM_CODEC = TransformType.STREAM_CODEC.dispatch(
                Transform::getType,
                TransformType::streamCodec
        );

        protected final TransformType type;

        protected Transform(TransformType type) {
            this.type = type;
        }

        public TransformType getType() {
            return this.type;
        }

        /**
         * Mutates the input parameter.
         *
         * @param stack the function argument
         * @return The input matrix as transformed by this transformation.
         */
        public abstract Matrix4f apply(Matrix4f stack);

        @SuppressWarnings("unused")
        public abstract @NotNull StreamCodec<ByteBuf, @NotNull T> streamCodec();

        @SuppressWarnings("unused")
        public abstract @NotNull Codec<@NotNull T> codec();
    }

    public static class Rotation extends Transform<Rotation> {

        public static final Codec<Rotation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("x").forGetter(r -> r.x),
                Codec.FLOAT.fieldOf("y").forGetter(r -> r.y),
                Codec.FLOAT.fieldOf("z").forGetter(r -> r.z),
                Codec.FLOAT.fieldOf("w").forGetter(r -> r.w)
        ).apply(instance, Rotation::new));

        public static final StreamCodec<ByteBuf, Rotation> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                r -> r.x,
                ByteBufCodecs.FLOAT,
                r -> r.y,
                ByteBufCodecs.FLOAT,
                r -> r.z,
                ByteBufCodecs.FLOAT,
                r -> r.w,
                Rotation::new
        );
        protected final float x;
        protected final float y;
        protected final float z;
        protected final float w;
        protected final Quaternionf quaternionf;

        public Rotation(float x, float y, float z, float w) {
            super(TransformType.ROTATION);
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            this.quaternionf = new Quaternionf(this.x, this.y, this.z, this.w);
        }

        public Rotation(Quaternionf quaternion) {
            super(TransformType.ROTATION);
            this.x = quaternion.x;
            this.y = quaternion.y;
            this.z = quaternion.z;
            this.w = quaternion.w;
            this.quaternionf = new Quaternionf(this.x, this.y, this.z, this.w);
        }


        @SuppressWarnings("unused")
        public float getX() {
            return x;
        }

        @SuppressWarnings("unused")
        public float getY() {
            return y;
        }

        @SuppressWarnings("unused")
        public float getZ() {
            return z;
        }

        @SuppressWarnings("unused")
        public float getW() {
            return w;
        }

        @SuppressWarnings("unused")
        public @NotNull Quaternionf quaternion() {
            return this.quaternionf;
        }

        @Override
        public Matrix4f apply(Matrix4f stack) {
            return stack.rotate(this.quaternionf);
        }

        @Override
        public @NotNull StreamCodec<ByteBuf, @NotNull Rotation> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public @NotNull Codec<@NotNull Rotation> codec() {
            return CODEC;
        }
    }

    public static class Translation extends Transform<Translation> {

        public static final Codec<Translation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf(
                        "x").forGetter(t -> t.x),
                Codec.FLOAT.fieldOf("y").forGetter(t -> t.y),
                Codec.FLOAT.fieldOf("z").forGetter(t -> t.z)
        ).apply(instance, Translation::new));

        public static final StreamCodec<ByteBuf, Translation> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                t -> t.x,
                ByteBufCodecs.FLOAT,
                t -> t.y,
                ByteBufCodecs.FLOAT,
                t -> t.z,
                Translation::new
        );
        protected final float x;
        protected final float y;
        protected final float z;
        protected final Vector3f vector3f;

        public Translation(float x, float y, float z) {
            super(TransformType.TRANSLATION);
            this.x = x;
            this.y = y;
            this.z = z;
            this.vector3f = new Vector3f(this.x, this.y, this.z);
        }

        @SuppressWarnings("unused")
        public Translation(Vector3f vector) {
            super(TransformType.ROTATION);
            this.x = vector.x;
            this.y = vector.y;
            this.z = vector.z;
            this.vector3f = new Vector3f(this.x, this.y, this.z);
        }

        @SuppressWarnings("unused")
        public float getX() {
            return x;
        }

        @SuppressWarnings("unused")
        public float getY() {
            return y;
        }

        @SuppressWarnings("unused")
        public float getZ() {
            return z;
        }

        @SuppressWarnings("unused")
        public @NotNull Vector3f vector() {
            return this.vector3f;
        }

        @Override
        public Matrix4f apply(Matrix4f stack) {
            return stack.translate(this.vector3f);
        }

        @Override
        public @NotNull StreamCodec<ByteBuf, @NotNull Translation> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public @NotNull Codec<@NotNull Translation> codec() {
            return CODEC;
        }
    }

    public static class Scale extends Transform<Scale> {

        public static final Codec<Scale> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf(
                        "x").forGetter(s -> s.x),
                Codec.FLOAT.fieldOf("y").forGetter(s -> s.y),
                Codec.FLOAT.fieldOf("z").forGetter(s -> s.z)
        ).apply(instance, Scale::new));

        public static final StreamCodec<ByteBuf, Scale> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                s -> s.x,
                ByteBufCodecs.FLOAT,
                s -> s.y,
                ByteBufCodecs.FLOAT,
                s -> s.z,
                Scale::new
        );
        protected final float x;
        protected final float y;
        protected final float z;

        @SuppressWarnings("unused")
        public Scale(float s) {
            super(TransformType.SCALE);
            this.x = s;
            this.y = s;
            this.z = s;
        }

        @SuppressWarnings("unused")
        public Scale(float x, float y, float z) {
            super(TransformType.SCALE);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @SuppressWarnings("unused")
        public Scale(Vector3f vector) {
            super(TransformType.ROTATION);
            this.x = vector.x;
            this.y = vector.y;
            this.z = vector.z;
        }

        @SuppressWarnings("unused")
        public float getX() {
            return x;
        }

        @SuppressWarnings("unused")
        public float getY() {
            return y;
        }

        @SuppressWarnings("unused")
        public float getZ() {
            return z;
        }

        @SuppressWarnings("unused")
        public @NotNull Vector3f vector() {
            return new Vector3f(this.x, this.y, this.z);
        }

        @Override
        public Matrix4f apply(Matrix4f stack) {
            return stack.scale(this.x, this.y, this.z);
        }

        @Override
        public @NotNull StreamCodec<ByteBuf, @NotNull Scale> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public @NotNull Codec<@NotNull Scale> codec() {
            return CODEC;
        }
    }
}
