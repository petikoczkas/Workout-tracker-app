package hu.bme.aut.workout_tracker_backend.business_logic_layer.service;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.api.ApiConstants;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthUserDetails;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    /// Username is actually email, but can not be renamed because of the override.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = repository.findByEmail(username);
        return userDetail.map(AuthUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(ApiConstants.userNotFoundWithEmailMessage + username));
    }

    public String addUser(User user) {
        Optional<User> userByEmail = repository.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new IllegalStateException(ApiConstants.userAlreadyExistsMessage);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setPhoto("");
        repository.save(user);
        return ApiConstants.userAddedMessage;
    }
}
