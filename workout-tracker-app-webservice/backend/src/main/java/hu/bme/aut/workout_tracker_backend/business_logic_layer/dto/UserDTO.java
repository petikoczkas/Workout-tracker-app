package hu.bme.aut.workout_tracker_backend.business_logic_layer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    String email;
    String firstName;
    String lastName;
    byte[] photo;
}
