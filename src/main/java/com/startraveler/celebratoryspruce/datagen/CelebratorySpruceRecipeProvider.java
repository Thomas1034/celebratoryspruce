package com.startraveler.celebratoryspruce.datagen;

import com.startraveler.celebratoryspruce.CelebratorySpruce;
import com.startraveler.celebratoryspruce.ModBlocks;
import com.startraveler.celebratoryspruce.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CelebratorySpruceRecipeProvider extends RecipeProvider {
    protected final String modid;

    public CelebratorySpruceRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
        this.modid = CelebratorySpruce.MODID;
    }

    @Override
    protected void buildRecipes() {

        shapeless(
                List.of(Blocks.SPRUCE_LEAVES, ModItems.ORNAMENT.get()),
                List.of(1, 1),
                RecipeCategory.DECORATIONS,
                ModBlocks.DECORATED_SPRUCE_LEAVES.get(),
                1,
                "celebratory_spruce_leaves"
        );
        shapeless(
                List.of(Blocks.SPRUCE_LEAVES, ModItems.FESTIVE_LIGHT.get()),
                List.of(1, 1),
                RecipeCategory.DECORATIONS,
                ModBlocks.FESTIVE_SPRUCE_LEAVES.get(),
                1,
                "celebratory_spruce_leaves"
        );
        shaped(
                List.of("oio", "isi", "oio"),
                List.of('i', 's', 'o'),
                List.of(Items.GOLD_INGOT, Blocks.GLOWSTONE, ModItems.FESTIVE_LIGHT.get()),
                RecipeCategory.DECORATIONS,
                ModItems.GOLD_STAR.get(),
                1
        );
        shaped(
                List.of("oio", "isi", "oio"),
                List.of('i', 's', 'o'),
                List.of(Items.COPPER_INGOT, Blocks.GLASS, ModItems.FESTIVE_LIGHT.get()),
                RecipeCategory.DECORATIONS,
                ModItems.ITEM_DISPLAY.get(),
                4
        );
        shaped(
                List.of("oso", "oio", "oso"),
                List.of('i', 's', 'o'),
                List.of(Items.COPPER_INGOT, Items.STRING, ModItems.FESTIVE_LIGHT.get()),
                RecipeCategory.DECORATIONS,
                ModBlocks.LIGHT_NET.get(),
                6
        );
        shaped(
                List.of(" l ", "l l", " l "),
                List.of('l'),
                List.of(Items.SPRUCE_LEAVES),
                RecipeCategory.DECORATIONS,
                ModItems.WREATH.get(),
                8
        );
        shapeless(
                List.of(ModItems.FESTIVE_LIGHT, ModItems.ORNAMENT, ModItems.WREATH),
                List.of(1, 1, 1),
                RecipeCategory.DECORATIONS,
                ModItems.DECORATED_WREATH.get(),
                1
        );
        shapeless(
                List.of(ItemTags.CREEPER_DROP_MUSIC_DISCS, ModItems.ORNAMENT, Items.PAPER, Items.SLIME_BALL),
                List.of(1, 1, 1, 1),
                RecipeCategory.MISC,
                ModItems.BLANK_CAROL_DISC.get(),
                1
        );
        shaped(
                List.of(" r ", "ror", " r "),
                List.of('r', 'o'),
                List.of(Items.RESIN_CLUMP, ModItems.ORNAMENT.get()),
                RecipeCategory.MISC,
                ModItems.BLANK_CAROL_DISC.get(),
                2
        );
        shapeless(
                List.of(ModItems.BLANK_CAROL_DISC.get(), ItemTags.WOOL, Tags.Items.DYES_BLUE),
                List.of(1, 1, 1),
                RecipeCategory.MISC,
                ModItems.MUSIC_DISC_SILENT_NIGHT.get(),
                1
        );
        shapeless(
                List.of(ModItems.BLANK_CAROL_DISC.get(), ItemTags.PLANKS, Tags.Items.DYES_GREEN),
                List.of(1, 1, 1),
                RecipeCategory.MISC,
                ModItems.MUSIC_DISC_WHAT_CHILD.get(),
                1
        );
        shapeless(
                List.of(ModItems.BLANK_CAROL_DISC.get(), Items.COPPER_INGOT, Tags.Items.DYES_RED),
                List.of(1, 1, 1),
                RecipeCategory.MISC,
                ModItems.MUSIC_DISC_CHRISTMAS_DAY_BELLS.get(),
                1
        );
        shapeless(
                List.of(ModItems.BLANK_CAROL_DISC.get(), Items.IRON_INGOT, Tags.Items.DYES_PURPLE),
                List.of(1, 1, 1),
                RecipeCategory.MISC,
                ModItems.MUSIC_DISC_CAROL_OF_THE_BELLS.get(),
                1
        );

        shaped(
                List.of(" i ", "ioi", " i "),
                List.of('i', 'o'),
                List.of(Items.PAPER, ModItems.ORNAMENT.get()),
                RecipeCategory.DECORATIONS,
                ModItems.PRESENT.get(),
                2
        );

        shaped(
                List.of("ioi", "i i"),
                List.of('i', 'o'),
                List.of(ItemTags.WOOL, ModItems.ORNAMENT.get()),
                RecipeCategory.MISC,
                ModItems.STOCKING.get(),
                2
        );

        shaped(
                List.of("afa", "beb", "cfc"),
                List.of('a', 'b', 'c', 'e', 'f'),
                List.of(Items.MILK_BUCKET, Items.HONEY_BOTTLE, Items.WHEAT, ItemTags.EGGS, Tags.Items.FOODS_FRUIT),
                RecipeCategory.MISC,
                ModItems.FRUIT_CAKE.get(),
                1
        );

        shaped(
                List.of("afa", "beb", "cfc"),
                List.of('a', 'b', 'c', 'e', 'f'),
                List.of(Items.MILK_BUCKET, Items.SUGAR, Items.WHEAT, ItemTags.EGGS, Items.COCOA_BEANS),
                RecipeCategory.MISC,
                ModItems.YULE_LOG_CAKE.get(),
                1
        );
    }

    protected void shaped(List<String> pattern, List<Character> tokens, List<Object> ingredients, RecipeCategory recipeCategory, ItemLike result, int count) {
        shaped(pattern, tokens, ingredients, recipeCategory, result, count, null);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    protected void shaped(List<String> pattern, List<Character> tokens, List<Object> ingredients, RecipeCategory recipeCategory, ItemLike result, int count, String group) {

        ShapedRecipeBuilder recipe = shaped(recipeCategory, result, count);
        StringBuilder recipeName = new StringBuilder(this.modid + ":" + getItemName(result) + "_from");

        // Adds in the pattern.
        for (String row : pattern) {
            recipe = recipe.pattern(row);
        }

        // Check if the ingredients match the tokens.
        if (tokens.size() != ingredients.size()) {
            throw new IllegalArgumentException("Token count does not match ingredient count.");
        }

        // Defines the tokens.
        for (int i = 0; i < tokens.size(); i++) {
            if (ingredients.get(i) instanceof ItemLike) {
                recipeName.append("_").append(getItemName((ItemLike) ingredients.get(i)));
                recipe = recipe.define(tokens.get(i), (ItemLike) ingredients.get(i));
            } else if (ingredients.get(i) instanceof TagKey<?> tag) {
                Ingredient ingredient = Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM)
                        .getOrThrow((TagKey<Item>) tag));
                recipeName.append("_tag_").append(tag.location().toDebugFileName());
                recipe = recipe.define(tokens.get(i), ingredient);
            } else {
                throw new IllegalArgumentException("Unrecognized item or tag type: " + ingredients.get(i));
            }
        }

        // Adds in the unlock trigger for the ingredients.
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike) {
                String name = getHasName((ItemLike) ingredient);
                recipe = recipe.unlockedBy(name, has((ItemLike) ingredient));
            } else if (ingredient instanceof TagKey) {
                String name = "has_" + ((TagKey<Item>) ingredient).registry().registry().toDebugFileName();
                recipe = recipe.unlockedBy(name, has((TagKey<Item>) ingredient));
            } else {
                throw new IllegalArgumentException("Unrecognized item or tag type: " + ingredient);
            }
        }
        // Adds in the unlock trigger for the result.
        recipe = recipe.unlockedBy(getHasName(result), has(result));

        recipe.group(group == null ? group(result) : group);

        // Saves the recipe.
        recipe.save(this.output, recipeName.toString());
    }

    @SuppressWarnings({"unchecked", "SameParameterValue", "unused"})
    protected void stonecutting(Object ingredient, RecipeCategory recipeCategory, ItemLike result, int count) {

        // The name of the recipe.
        String recipeName = this.modid + ":" + getItemName(result) + "_from_stonecutting_";

        Ingredient toAdd;

        // Determines what type of ingredient is there.
        if (ingredient instanceof ItemLike item) {
            toAdd = Ingredient.of(item);
            recipeName += getItemName(item);

        } else if (ingredient instanceof TagKey<?> tag) {
            toAdd = Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM).getOrThrow((TagKey<Item>) tag));
            recipeName += tag.location().toDebugFileName();

        } else {
            throw new IllegalArgumentException("Cannot create a stonecutting recipe with a non-Item, non-Tag object.");
        }

        // Creates the recipe.
        SingleItemRecipeBuilder recipe = SingleItemRecipeBuilder.stonecutting(toAdd, recipeCategory, result, count);

        // Adds in the unlock trigger for the ingredient.
        if (ingredient instanceof ItemLike) {
            recipe = recipe.unlockedBy(getHasName((ItemLike) ingredient), has((ItemLike) ingredient));
        } else if (ingredient instanceof TagKey) {
            String name = "has" + ((TagKey<Item>) ingredient).registry().registry().toDebugFileName();
            recipe = recipe.unlockedBy(name, has((TagKey<Item>) ingredient));
        } else {
            throw new IllegalArgumentException("Unrecognized item or tag type: " + ingredient);
        }

        // Adds in the unlock trigger for the result.
        recipe = recipe.unlockedBy(getHasName(result), has(result));

        recipe.group(group(result));

        // Prints out the result.
        recipe.save(this.output, recipeName);
    }

    @SuppressWarnings("SameParameterValue")
    protected void shapeless(List<Object> ingredients, List<Integer> counts, RecipeCategory recipeCategory, ItemLike result, int count) {
        shapeless(ingredients, counts, recipeCategory, result, count, null);
    }

    // Shapeless recipe. Item at n must correspond to item count at n.
    @SuppressWarnings({"unchecked"})
    protected void shapeless(List<Object> ingredients, List<Integer> counts, RecipeCategory recipeCategory, ItemLike result, int count, String group) {

        ShapelessRecipeBuilder recipe = shapeless(recipeCategory, result, count);
        StringBuilder recipeName = new StringBuilder(group(result) + "_from");

        // Check if the ingredients match the count length.
        if (counts.size() != ingredients.size()) {
            throw new IllegalArgumentException("Token count does not match ingredient count.");
        }

        // Adds in the ingredients.
        for (int i = 0; i < counts.size(); i++) {
            if (ingredients.get(i) instanceof ItemLike) {
                recipeName.append("_").append(getItemName((ItemLike) ingredients.get(i)));
                recipe = recipe.requires((ItemLike) ingredients.get(i), counts.get(i));
            } else if (ingredients.get(i) instanceof TagKey<?> tag) {
                recipeName.append("_tag_").append(tag.location().toDebugFileName());
                Ingredient ingredient = Ingredient.of(this.registries.lookupOrThrow(Registries.ITEM)
                        .getOrThrow((TagKey<Item>) tag));
                recipe = recipe.requires(ingredient, counts.get(i));
            } else {
                throw new IllegalArgumentException("Unrecognized item or tag type: " + ingredients.get(i));
            }
        }

        // Adds in the unlock triggers for the ingredients.
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike) {
                recipe = recipe.unlockedBy(getHasName((ItemLike) ingredient), has((ItemLike) ingredient));
            } else if (ingredient instanceof TagKey) {
                String name = "has" + ((TagKey<Item>) ingredient).registry().registry().toDebugFileName();
                recipe = recipe.unlockedBy(name, has((TagKey<Item>) ingredient));
            } else {
                throw new IllegalArgumentException("Unrecognized item or tag type: " + ingredient);
            }
        }
        // Adds in the unlock trigger for the result.
        recipe = recipe.unlockedBy(getHasName(result), has(result));

        recipe.group(group == null ? group(result) : group);

        // Saves the recipe.
        recipe.save(this.output, recipeName.toString());

    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    protected void foodCooking(List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime) {
        String group = (namespace(result) + ":" + getItemName(result));
        campfire(ingredients, category, result, experience, 2 * cookingTime, group);
        smelting(ingredients, category, result, experience, cookingTime, group);
        smoking(ingredients, category, result, experience, cookingTime / 2, group);
    }

    @SuppressWarnings("unused")
    protected String hasName(ItemLike item) {
        return "has_" + name(item);
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    protected void shapeless(RecipeCategory recipeCategory, ItemLike input, int inCount, ItemLike output, int count) {
        shapeless(recipeCategory, input, inCount, output, count, null);
    }

    @SuppressWarnings("SameParameterValue")
    protected void shapeless(RecipeCategory recipeCategory, ItemLike input, int inCount, ItemLike output, int count, String group) {
        shapeless(List.of(input), List.of(inCount), recipeCategory, output, count, group);
    }

    protected String group(ItemLike item) {
        return identifier(item).toString();
    }

    protected String name(ItemLike item) {
        return identifier(item).getPath();
    }

    protected String namespace(ItemLike item) {
        return identifier(item).getNamespace();
    }

    @SuppressWarnings("deprecation")
    protected Identifier identifier(ItemLike item) {
        return item.asItem().builtInRegistryHolder().key().identifier();
    }

    protected void campfire(List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        cooking(
                RecipeSerializer.CAMPFIRE_COOKING_RECIPE,
                CampfireCookingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime * 3,
                group,
                "_from_campfire"
        );
    }

    protected void smoking(List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        cooking(
                RecipeSerializer.SMOKING_RECIPE,
                SmokingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime / 2,
                group,
                "_from_smoking"
        );
    }

    protected void smelting(List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        cooking(
                RecipeSerializer.SMELTING_RECIPE,
                SmeltingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime,
                group,
                "_from_smelting"
        );
    }

    @SuppressWarnings("unused")
    protected void blasting(List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        cooking(
                RecipeSerializer.BLASTING_RECIPE,
                BlastingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime / 2,
                group,
                "_from_blasting"
        );
    }

    protected <T extends AbstractCookingRecipe> void cooking(RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> factory, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String recipeName) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(
                            Ingredient.of(itemlike),
                            category,
                            result,
                            experience,
                            cookingTime,
                            cookingSerializer,
                            factory
                    )
                    .group(group)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(
                            this.output,
                            this.modid + ":" + getItemName(result) + recipeName + "_" + getItemName(itemlike)
                    );
        }
    }

    // The runner to add to the data generator
    public static class Runner extends RecipeProvider.Runner {
        // Get the parameters from GatherDataEvent.
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput output) {
            return new CelebratorySpruceRecipeProvider(provider, output);
        }

        @Override
        public @NotNull String getName() {
            return "Verdant Recipe Provider";
        }
    }

}