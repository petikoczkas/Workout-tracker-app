package hu.bme.aut.workout_tracker_backend.business_logic_layer.api;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthRequest;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthTokenService;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.service.AuthenticationService;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApiController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    // AUTH
    @PostMapping("/register")
    public String addNewUser(@RequestBody User user) {
        return authenticationService.addUser(user);
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return authTokenService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException(ApiConstants.invalidCredentialsMessage);
        }
    }
}
