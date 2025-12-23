package com.startraveler.celebratoryspruce.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.startraveler.celebratoryspruce.CelebratorySpruce;
import com.startraveler.celebratoryspruce.ModBlockEntityTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ItemRenderingBlockEntity extends BlockEntity {
    public static final String ITEM_KEY = "item";
    public static final String TRANSFORMS_KEY = "transforms";
    protected final @NotNull List<Transform<?>> transforms;
    protected @NotNull ItemStack item;

    public ItemRenderingBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.ITEM_RENDERING_BLOCK.get(), pos, blockState);
        this.transforms = new ArrayList<>();
        this.item = ItemStack.EMPTY;
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        // load item and transforms
        this.item = input.read(ITEM_KEY, ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.transforms.clear();
        this.transforms.addAll(input.read(TRANSFORMS_KEY, Transform.TRANSFORM_CODEC.listOf()).orElse(List.of()));
    }

    // Save values into the passed CompoundTag here.
    @Override
    public void saveAdditional(@NotNull ValueOutput output) {

        super.saveAdditional(output);

        output.store(ITEM_KEY, ItemStack.CODEC, this.item);
        output.store(TRANSFORMS_KEY, Transform.TRANSFORM_CODEC.listOf(), this.transforms);

        // save item and transforms
    }

    @Override
    public Packet<@NotNull ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(
                this::toString,
                CelebratorySpruce.LOGGER
        )) {
            TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
            this.saveAdditional(output);
            tag.merge(output.buildResult());
        }
        return tag;
    }

    public void addTransform(Transform<?> transform) {
        this.transforms.addLast(transform);
        this.markAsChanged();
    }

    public void clearTransforms() {
        this.transforms.clear();
        this.markAsChanged();
    }

    public @NotNull ItemStack getDisplayItem() {
        return item;
    }

    public void setDisplayItem(@NotNull ItemLike item) {
        this.setDisplayItem(item.asItem().getDefaultInstance());
    }

    public void setDisplayItem(ItemStack item) {
        this.item = item;
        this.markAsChanged();
    }

    public void markAsChanged() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(
                    this.getBlockPos(),
                    this.getBlockState(),
                    this.getBlockState(),
                    Block.UPDATE_ALL
            );
        }
    }

    public @NotNull List<Transform<?>> getTransforms() {
        return this.transforms;
    }

    public void addTransforms(@NotNull List<Transform<?>> transforms) {
        this.transforms.addAll(transforms);
        this.markAsChanged();
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

    public abstract static class Transform<T> {

        public static final Codec<Transform<?>> TRANSFORM_CODEC = TransformType.CODEC.dispatch(
                Transform::getType,
                transformType -> transformType.codec().fieldOf("transform")
        );
        public static final StreamCodec<ByteBuf, Transform<?>> TRANSFORM_STREAM_CODEC = TransformType.STREAM_CODEC.dispatch(Transform::getType,
                TransformType::streamCodec
        );

        protected final TransformType type;

        protected Transform(TransformType type) {
            this.type = type;
        }

        public TransformType getType() {
            return this.type;
        }

        public abstract @NotNull StreamCodec<ByteBuf, @NotNull T> streamCodec();

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

        public Rotation(float x, float y, float z, float w) {
            super(TransformType.ROTATION);
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public Rotation(Quaternionf quaternion) {
            super(TransformType.ROTATION);
            this.x = quaternion.x;
            this.y = quaternion.y;
            this.z = quaternion.z;
            this.w = quaternion.w;
        }


        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float getW() {
            return w;
        }

        public @NotNull Quaternionf quaternion() {
            return new Quaternionf(this.x, this.y, this.z, this.w);
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

        public Translation(float x, float y, float z) {
            super(TransformType.TRANSLATION);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Translation(Vector3f vector) {
            super(TransformType.ROTATION);
            this.x = vector.x;
            this.y = vector.y;
            this.z = vector.z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public @NotNull Vector3f vector() {
            return new Vector3f(this.x, this.y, this.z);
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

        public Scale(float s) {
            super(TransformType.SCALE);
            this.x = s;
            this.y = s;
            this.z = s;
        }

        public Scale(float x, float y, float z) {
            super(TransformType.SCALE);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Scale(Vector3f vector) {
            super(TransformType.ROTATION);
            this.x = vector.x;
            this.y = vector.y;
            this.z = vector.z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public @NotNull Vector3f vector() {
            return new Vector3f(this.x, this.y, this.z);
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
