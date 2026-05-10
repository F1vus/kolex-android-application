package edu.at.kolex.model;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Station {
    private Long id;
    private String name;
    private String city;

    public String getDisplayName() {
        return city + " - " + name;
    }

    @NonNull
    @Override
    public String toString() {
        return getDisplayName();
    }
}
