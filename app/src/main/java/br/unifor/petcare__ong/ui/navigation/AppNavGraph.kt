package br.unifor.petcare__ong.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import br.unifor.petcare__ong.ui.screens.*

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

        composable(Routes.AnimalProfile.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            AnimalProfileScreen(navController, animalId)
        }

        composable(Routes.AddAnimal.route) {
            AddAnimalScreen(navController)
        }

        composable(Routes.EditAnimal.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            AddAnimalScreen(navController, animalId)
        }

        composable(Routes.MedicalRecord.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            MedicalRecordScreen(navController, animalId)
        }

        composable(Routes.NewRecord.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            val recordId = backStackEntry.arguments?.getString("recordId")
            NewRecordScreen(navController, animalId, recordId)
        }

        composable(Routes.MovementHistory.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            MovementHistoryScreen(navController, animalId)
        }

        composable(Routes.NewMovement.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            NewMovementScreen(navController, animalId)
        }

        composable(Routes.EditMovement.route) { backStackEntry ->
            val animalId = backStackEntry.arguments?.getString("animalId") ?: ""
            val movementId = backStackEntry.arguments?.getString("movementId") ?: ""
            NewMovementScreen(navController, animalId, movementId)
        }

        composable(Routes.ExportReports.route) {
            ExportReportsScreen(navController)
        }
    }
}