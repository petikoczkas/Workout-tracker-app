package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.UserDTO;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO getUser(String email) {
        val userWrapped = userRepository.findByEmail(email);
        if (userWrapped.isEmpty()) throw new IllegalStateException("No such user");
        val user = userWrapped.get();
        var userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoto(user.getPhoto());
        return userDTO;
    }

    public void updateUser(UserDTO userDTO) {
        val userWrapped = userRepository.findByEmail(userDTO.getEmail());
        if (userWrapped.isEmpty()) throw new IllegalStateException("No such user");
        var user = userWrapped.get();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoto(userDTO.getPhoto());
        userRepository.save(user);
    }

    public List<UserDTO> getUsers() {
        val users = userRepository.findAll();
        val list = new ArrayList<UserDTO>();
        for (User u : users) {
            list.add(getUser(u.getEmail()));
        }
        return list;
    }
}
