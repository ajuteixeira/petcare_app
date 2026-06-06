package br.unifor.petcare__ong.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import br.unifor.petcare__ong.ui.screens.*

import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {

        composable(Routes.Login.route) {
            LoginScreen(navController)
        }

        composable(Routes.Register.route) {
            RegisterScreen(navController)
        }

        composable(Routes.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(Routes.AnimalList.route) {
            AnimalListScreen(navController)
        }

        composable(
            route = Routes.AnimalProfile.route,
            arguments = listOf(navArgument("animalId") { type = NavType.StringType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            AnimalProfileScreen(navController, animalId)
        }

        composable(Routes.AddAnimal.route) {
            AddAnimalScreen(navController)
        }

        composable(
            route = Routes.MedicalRecord.route,
            arguments = listOf(navArgument("animalId") { type = NavType.StringType })
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            MedicalRecordScreen(navController, animalId)
        }

        composable(
            route = Routes.NewRecord.route,
            arguments = listOf(
                navArgument("animalId") { type = NavType.StringType },
                navArgument("recordId") { 
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            val recordId = backStackEntry.arguments?.getString("recordId")
            NewRecordScreen(navController, animalId, recordId)
        }

        composable(Routes.MovementHistory.route) {
            MovementHistoryScreen(navController)
        }

        composable(Routes.NewMovement.route) {
            NewMovementScreen(navController)
        }

        composable(Routes.ExportReports.route) {
            ExportReportsScreen(navController)
        }
    }
}