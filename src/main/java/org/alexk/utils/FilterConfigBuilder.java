package org.alexk.utils;

import lombok.Data;
import lombok.val;
import org.alexk.business.models.misc.FilterConfig;

@Data
public class FilterConfigBuilder {
    private String title;
    private String rating;
    private String calories;
    private String protein;
    private String fat;
    private String sodium;
    private String price;

    public FilterConfigBuilder(String title, String rating, String calories, String protein, String fat, String sodium, String price) {
        this.title = title;
        this.rating = rating;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
        this.price = price;
    }

    public FilterConfig getConfig() {
        val config = new FilterConfig();

        parseTitle(config, title);
        parseRating(config, rating);
        parseCalories(config, calories);
        parseProtein(config, protein);
        parseFat(config, fat);
        parseSodium(config, sodium);
        parsePrice(config, price);

        return config;
    }

    private void parseTitle(FilterConfig config, String title) {
        if(title != null) {
            config.setTitle(title);
        }
    }

    private void parseRating(FilterConfig config, String rating) {
        if(rating != null) {
            val arr = rating.split(";");
            config.setMinRating(parseDouble(arr[0]));
            config.setMaxRating(parseDouble(arr[1]));
        }
    }

    private void parseCalories(FilterConfig config, String calories) {
        if(calories != null) {
            val arr = calories.split(";");
            config.setMinCalories(parseDouble(arr[0]));
            config.setMaxCalories(parseDouble(arr[1]));
        }
    }

    private void parseProtein(FilterConfig config, String protein) {
        if(protein != null) {
            val arr = protein.split(";");
            config.setMinProtein(parseDouble(arr[0]));
            config.setMaxProtein(parseDouble(arr[1]));
        }
    }

    private void parseFat(FilterConfig config, String fat) {
        if(fat != null) {
            val arr = fat.split(";");
            config.setMinFat(parseDouble(arr[0]));
            config.setMaxFat(parseDouble(arr[1]));
        }
    }

    private void parseSodium(FilterConfig config, String sodium) {
        if(sodium != null) {
            val arr = sodium.split(";");
            config.setMinSodium(parseDouble(arr[0]));
            config.setMaxSodium(parseDouble(arr[1]));
        }
    }

    private void parsePrice(FilterConfig config, String price) {
        if(price != null) {
            val arr = price.split(";");
            config.setMinPrice(parseDouble(arr[0]));
            config.setMaxPrice(parseDouble(arr[1]));
        }
    }

    private double parseDouble(String dbl) {
        return Math.ceil(Double.parseDouble(dbl));
    }
}
