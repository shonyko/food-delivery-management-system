package org.alexk.business.models;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class MenuItem implements Serializable {
    public static int nextId;

    private int id;
    private String title;
    private double rating;

    public MenuItem() {
    }

    public MenuItem(String title, double rating) {
        this.updateId();
        this.title = title;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }
    public MenuItem setId(int id) {
        this.id = id;

        return this;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public abstract double getCalories();
    public abstract double getProtein();
    public abstract double getFat();
    public abstract double getSodium();
    public abstract double computePrice();
    public abstract boolean isWellFormed();

    public int getAndIncrementNextId() {
        return nextId++;
    }

    public MenuItem updateId() {
        setId(getAndIncrementNextId());

        return this;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int id) {
        nextId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return id == menuItem.id && Double.compare(menuItem.rating, rating) == 0 && title.equals(menuItem.getTitle());
    }
}
