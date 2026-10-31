package com.github.alexthe668.alexsmobs.misc;

import com.github.alexthe668.alexsmobs.AlexsMobs;
import com.github.alexthe668.citadel.client.model.container.JsonUtils;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CapsidRecipe {
    private final NonNullList<Ingredient> ingredients;
    private final List<TagKey<Item>> tags;
    private final Item resultItem;
    private final int resultCount;
    private final int time;

    public CapsidRecipe(NonNullList<Ingredient> ingredients, ItemStack result, int time) {
        this(ingredients, List.of(), result.getItem(), result.getCount(), time);
    }

    public CapsidRecipe(NonNullList<Ingredient> ingredients, Item resultItem, int resultCount, int time) {
        this(ingredients, List.of(), resultItem, resultCount, time);
    }

    public CapsidRecipe(NonNullList<Ingredient> ingredients, List<TagKey<Item>> tags, Item resultItem, int resultCount, int time) {
        this.ingredients = ingredients;
        this.tags = tags;
        this.resultItem = resultItem;
        this.resultCount = resultCount;
        this.time = time;
    }

    private static void readIngredients(JsonArray ingredientArray, NonNullList<Ingredient> ingredients, List<TagKey<Item>> tags) {
        for (int i = 0; i < ingredientArray.size(); ++i) {
            addIngredient(ingredients, tags, ingredientArray.get(i));
        }
    }

    /**
     * Ingredient codecs need registry ops, which this reload listener does not have.
     * Accept an item id, a {@code #tag}, a list of those, or the older {@code {"item"}} / {@code {"tag"}} objects.
     */
    private static void addIngredient(NonNullList<Ingredient> ingredients, List<TagKey<Item>> tags, JsonElement element) {
        if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                addIngredient(ingredients, tags, child);
            }
            return;
        }
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            addParsedId(ingredients, tags, element.getAsString());
            return;
        }
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("item")) {
                addParsedId(ingredients, tags, object.get("item").getAsString());
                return;
            }
            if (object.has("tag")) {
                addParsedId(ingredients, tags, "#" + object.get("tag").getAsString());
                return;
            }
        }
        AlexsMobs.LOGGER.error("Couldn't parse capsid ingredient {}", element);
    }

    private static void addParsedId(NonNullList<Ingredient> ingredients, List<TagKey<Item>> tags, String raw) {
        if (raw.startsWith("#")) {
            tags.add(TagKey.create(Registries.ITEM, Identifier.parse(raw.substring(1))));
            return;
        }
        Identifier itemId = Identifier.parse(raw);
        Item item = BuiltInRegistries.ITEM.get(itemId).map(Holder::value).orElse(Items.AIR);
        if (item == Items.AIR) {
            AlexsMobs.LOGGER.error("Unknown capsid ingredient item {}", raw);
            return;
        }
        ingredients.add(Ingredient.of(item));
    }

    public ItemStack getResult() {
        return resultItem == Items.AIR ? ItemStack.EMPTY : new ItemStack(resultItem, resultCount);
    }

    public NonNullList<Ingredient> getIngredients() {
        if (this.tags.isEmpty()) {
            return ingredients;
        }
        NonNullList<Ingredient> resolved = NonNullList.create();
        resolved.addAll(this.ingredients);
        for (TagKey<Item> tag : this.tags) {
            BuiltInRegistries.ITEM.get(tag).ifPresent(set -> resolved.add(Ingredient.of(set)));
        }
        return resolved;
    }

    public int getTime() {
        return time;
    }

    public boolean matches(ItemStack... stacks) {
        int required = this.ingredients.size() + this.tags.size();
        if (required == 0) {
            return false;
        }
        IntList taken = new IntArrayList();
        ItemStack[] copy = new ItemStack[stacks.length];
        for (int j = 0; j < copy.length; j++) {
            copy[j] = stacks[j].copy();
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredients.get(i).test(copy[j])) {
                    taken.add(j);
                    copy[j].shrink(1);
                }
            }
            for (TagKey<Item> tag : this.tags) {
                if (copy[j].is(tag)) {
                    taken.add(j);
                    copy[j].shrink(1);
                }
            }
        }
        return taken.size() >= required;
    }

    public static class Deserializer implements JsonDeserializer<CapsidRecipe> {

        @Override
        public CapsidRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = json.getAsJsonObject();
            int time = JsonUtils.getInt(jsonobject, "time");
            Item resultItem = Items.AIR;
            int count = 1;
            if (jsonobject.has("result")) {
                JsonObject resultObj = JsonUtils.getJsonObject(jsonobject, "result");
                net.minecraft.resources.Identifier itemId = net.minecraft.resources.Identifier.parse(resultObj.get("item").getAsString());
                resultItem = BuiltInRegistries.ITEM.get(itemId).map(Holder::value).orElse(Items.AIR);
                count = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;
            }
            NonNullList<Ingredient> nonnulllist = NonNullList.create();
            List<TagKey<Item>> tags = new ArrayList<>();
            readIngredients(JsonUtils.getJsonArray(jsonobject, "ingredients"), nonnulllist, tags);
            return new CapsidRecipe(nonnulllist, tags, resultItem, count, time);
        }

    }
}
