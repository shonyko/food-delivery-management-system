package org.alexk.utils;

import org.alexk.business.models.MenuItem;
import org.alexk.business.models.misc.FilterConfig;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemFilter {
    private MenuItemFilter() {
    }

    public static List<MenuItem> filterMenuItems(List<MenuItem> menuItems, FilterConfig config) {
        return menuItems.stream()
        .filter(x -> x.getTitle().toLowerCase().contains(config.getTitle().toLowerCase()))
        .filter(x -> x.getRating() >= config.getMinRating() && x.getRating() <= config.getMaxRating())
        .filter(x -> x.getCalories() >= config.getMinCalories() && x.getCalories() <= config.getMaxCalories())
        .filter(x -> x.getProtein() >= config.getMinProtein() && x.getProtein() <= config.getMaxProtein())
        .filter(x -> x.getFat() >= config.getMinFat() && x.getFat() <= config.getMaxFat())
        .filter(x -> x.getSodium() >= config.getMinSodium() && x.getSodium() <= config.getMaxSodium())
        .filter(x -> x.computePrice() >= config.getMinPrice() && x.computePrice() <= config.getMaxPrice())
        .collect(Collectors.toList());
    }
}
