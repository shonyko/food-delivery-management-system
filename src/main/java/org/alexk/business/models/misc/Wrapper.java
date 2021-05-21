package org.alexk.business.models.misc;

import lombok.Data;
import org.alexk.business.models.MenuItem;

/**
 * Clasa care faciliteaza selectarea produselor cu cantitatea acestora in cadrul stream-urilor
 */
@Data
public class Wrapper {
    /**
     * Obiect care reprezinta produsul
     */
    public MenuItem menuItem;
    /**
     * Numar care reprezinta cantitatea produsului
     */
    public int frequency;

    /**
     * Creeaza un obiect
     * @param menuItem produsul de incapsulat
     * @param frequency cantitatea produsului
     */
    public Wrapper(MenuItem menuItem, int frequency) {
        this.menuItem = menuItem;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return String.format(
                "Item No. %d   Title: %s   Rating: %.2f   Calories: %.2f    Protein: %.2f Fat: %.2f Sodium: %.2f  Price: %.2f   Quantity: %d\n",
                menuItem.getId(),
                menuItem.getTitle(),
                menuItem.getRating(),
                menuItem.getCalories(),
                menuItem.getProtein(),
                menuItem.getFat(),
                menuItem.getSodium(),
                menuItem.computePrice(),
                getFrequency()
        );
    }
}
