package hu.bme.aut.workout_tracker_backend.business_logic_layer.api;

import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.AuthTokenService;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.authentication.SecurityConfig;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.ChartDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.SavedExerciseDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.UserDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.dto.WorkoutDTO;
import hu.bme.aut.workout_tracker_backend.business_logic_layer.service.*;
import hu.bme.aut.workout_tracker_backend.data_layer.chart.ChartType;
import hu.bme.aut.workout_tracker_backend.data_layer.exercise.Exercise;
import hu.bme.aut.workout_tracker_backend.data_layer.user.User;
import hu.bme.aut.workout_tracker_backend.data_layer.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
@Import(SecurityConfig.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthTokenService authTokenService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WorkoutService workoutService;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private SavedExerciseService savedExerciseService;

    @MockBean
    private ChartService chartService;

    private UserDTO userDTO;
    private WorkoutDTO workoutDTO;
    private Exercise exercise;
    private SavedExerciseDTO savedExerciseDTO;
    private ChartDTO chartDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setFirstName("First");
        userDTO.setLastName("Last");

        workoutDTO = new WorkoutDTO();
        workoutDTO.setId(1L);
        workoutDTO.setName("Workout 1");

        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Exercise 1");

        savedExerciseDTO = new SavedExerciseDTO();
        savedExerciseDTO.setId(1L);
        savedExerciseDTO.setUserId("user@example.com");
        savedExerciseDTO.setData(Collections.singletonList("data"));

        chartDTO = new ChartDTO();
        chartDTO.setId(1L);
        chartDTO.setUserId("user@example.com");
        chartDTO.setType(ChartType.Volume);
        chartDTO.setData(Collections.singletonList(10.0));
    }

    @Test
    void testAddNewUser() throws Exception {
        when(authenticationService.addUser(any(User.class))).thenReturn("User added");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"password\":\"password\", \"roles\":\"ROLE_USER\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User added"));
    }

    @Test
    void testAuthenticateAndGetToken() throws Exception {
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken("user@example.com", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        when(authTokenService.generateToken("user@example.com"))
                .thenReturn("mock-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", is("mock-token")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetCurrentUser() throws Exception {
        when(userService.getUser(eq("user@example.com"))).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getCurrentUser")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user@example.com")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testUpdateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\", \"firstName\":\"First\", \"lastName\":\"Last\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.singletonList(userDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("user@example.com")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetExercises() throws Exception {
        when(exerciseService.getExercises()).thenReturn(Collections.singletonList(exercise));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getExercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Exercise 1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetStandingsExercises() throws Exception {
        when(exerciseService.getStandingsExercises()).thenReturn(Collections.singletonList(exercise));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getStandingsExercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Exercise 1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testCreateExercise() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/createExercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Exercise 1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetUserWorkouts() throws Exception {
        when(workoutService.getUserWorkouts(eq("user@example.com"))).thenReturn(Collections.singletonList(workoutDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserWorkouts")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Workout 1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetUserFavoriteWorkouts() throws Exception {
        when(workoutService.getUserFavoriteWorkouts(eq("user@example.com"))).thenReturn(Collections.singletonList(workoutDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserFavoriteWorkouts")
                        .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Workout 1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetWorkout() throws Exception {
        when(workoutService.getWorkout(eq(1L))).thenReturn(workoutDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getWorkout")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Workout 1")));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testUpdateWorkout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/updateWorkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Workout 1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testDeleteWorkout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/deleteWorkout")
                        .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetUserSavedExercises() throws Exception {
        when(savedExerciseService.getUserSavedExercises("user@example.com"))
                .thenReturn(Collections.singletonList(savedExerciseDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserSavedExercises")
                        .param("email", "user@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value("user@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].data[0]").value("data"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testUpdateSavedExercise() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/updateSavedExercise")
                        .contentType("application/json")
                        .content("{\"id\":1,\"userId\":\"user@example.com\",\"data\":[\"exerciseData\"]}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetUserCharts() throws Exception {
        when(chartService.getUserCharts("user@example.com"))
                .thenReturn(Collections.singletonList(chartDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserCharts")
                        .param("email", "user@example.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value("user@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("Volume"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].data[0]").value(10.0));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testGetCharts() throws Exception {
        when(chartService.getCharts())
                .thenReturn(Collections.singletonList(chartDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getCharts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value("user@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].type").value("Volume"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].data[0]").value(10.0));
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void testUpdateChart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/updateChart")
                        .contentType("application/json")
                        .content("{\"id\":1,\"userId\":\"user@example.com\",\"type\":\"Volume\",\"data\":[10.0]}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}