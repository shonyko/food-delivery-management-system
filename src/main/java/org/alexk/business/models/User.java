package org.alexk.business.models;

import lombok.Data;
import org.alexk.business.models.enums.UserType;

import java.io.Serializable;
import java.util.Objects;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private UserType type;

    public User(String username, String password, UserType type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public boolean isWellFormed() {
        if(username.isEmpty() || username.isBlank()) {
            return false;
        }
        if(password.isEmpty() || password.isBlank()) {
            return false;
        }
        if(type == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        if (!Objects.equals(username, user.username)) return false;
        if (!Objects.equals(password, user.password)) return false;
        return type == user.type;
    }
}
