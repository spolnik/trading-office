package com.trading;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class Party implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Party that = (Party) o;

        return Objects.equals(id, that.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
