package hu.bme.aut.workout_tracker_backend.data_layer.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`user`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String roles;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;
}
