package org.alexk.business.models;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.Serializable;

@Setter
@Getter
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String date;
    private int clientId;

    public Order(int id, String date, int clientId) {
        this.id = id;
        this.date = date;
        this.clientId = clientId;
    }

    public boolean isWellFormed() {
        if(date.isEmpty() || date.isBlank()) {
            return false;
        }
        if(id < 0 || clientId < 0) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Order)) {
            return false;
        }

        val other = (Order) obj;
        return hashCode() == other.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "Order No. %d   Client Id: %d   Date: %s\n",
                getId(),
                getClientId(),
                getDate()
        );
    }
}
