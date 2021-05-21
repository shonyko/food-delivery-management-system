package org.alexk.business.models.misc;

import lombok.Data;

@Data
public class FilterConfig {
    private String title = "";
    private double minRating = 0;
    private double maxRating = Double.MAX_VALUE;
    private double minCalories = 0;
    private double maxCalories = Double.MAX_VALUE;
    private double minProtein = 0;
    private double maxProtein = Double.MAX_VALUE;
    private double minFat = 0;
    private double maxFat = Double.MAX_VALUE;
    private double minSodium = 0;
    private double maxSodium = Double.MAX_VALUE;
    private double minPrice = 0;
    private double maxPrice = Double.MAX_VALUE;
}
