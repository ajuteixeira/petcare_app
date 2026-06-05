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

        composable(Routes.AnimalProfile.route) {
            AnimalProfileScreen(navController)
        }

        composable(Routes.AddAnimal.route) {
            AddAnimalScreen(navController)
        }

        composable(Routes.MedicalRecord.route) {
            MedicalRecordScreen(navController)
        }

        composable(Routes.NewRecord.route) {
            NewRecordScreen(navController)
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