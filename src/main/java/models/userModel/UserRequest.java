package models.userModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.*;

import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class UserRequest {

    @JsonProperty("firstName")
    private String first_name;

    private String lastName;
    private String email;
    //private String gender;
    private String phone;
    @JsonProperty("location")
    private UserLocation userLocation;

}
