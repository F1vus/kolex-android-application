package edu.at.kolex.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile implements Serializable {
    @SerializedName("profileId")
    private Long id;
    private String firstName;
    private String lastName;
}
