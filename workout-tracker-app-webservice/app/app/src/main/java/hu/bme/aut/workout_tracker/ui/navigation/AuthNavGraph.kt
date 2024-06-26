package hu.bme.aut.workout_tracker.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationScreen
import hu.bme.aut.workout_tracker.ui.screen.registration.RegistrationSuccessScreen
import hu.bme.aut.workout_tracker.ui.screen.signin.SignInScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.SignIn.route
    ) {
        composable(route = AuthScreen.SignIn.route) {
            SignInScreen(
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(Graph.MAIN)
                },
                navigateToRegistration = {
                    navController.navigate(AuthScreen.Registration.route)
                }
            )
        }
        composable(route = AuthScreen.Registration.route) {
            RegistrationScreen(
                navigateToRegistrationSuccess = {
                    navController.navigate(AuthScreen.RegistrationSuccess.route)
                }
            )
        }
        composable(route = AuthScreen.RegistrationSuccess.route) {
            RegistrationSuccessScreen(
                navigateToHome = {
                    navController.navigate(Graph.MAIN)
                }
            )
        }
    }
}

sealed class AuthScreen(val route: String) {
    data object SignIn : AuthScreen(route = "SIGN_IN")
    data object Registration : AuthScreen(route = "REGISTRATION")
    data object RegistrationSuccess : AuthScreen(route = "REGISTRATION_SUCCESS")
}