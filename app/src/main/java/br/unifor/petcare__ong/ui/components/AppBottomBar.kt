package br.unifor.petcare__ong.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.ui.navigation.Routes

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String,
    tipoUsuario: String
) {

    val tealPrimary = Color(0xFF009688)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        // Animais (todos)
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Animais", fontSize = 10.sp) },
            selected = currentRoute == Routes.AnimalList.route,
            onClick = {
                navController.navigate(Routes.AnimalList.route)
            }
        )

        // Dashboard (somente gestor)
        if (tipoUsuario == "GESTOR") {
            NavigationBarItem(
                icon = { Icon(Icons.Default.BarChart, null) },
                label = { Text("Dashboard", fontSize = 10.sp) },
                selected = currentRoute == Routes.Dashboard.route,
                onClick = {
                    navController.navigate(Routes.Dashboard.route)
                }
            )
        }

        // Exportar (somente gestor)
        if (tipoUsuario == "GESTOR") {
            NavigationBarItem(
                icon = { Icon(Icons.Default.FileDownload, null) },
                label = { Text("Exportar", fontSize = 10.sp) },
                selected = currentRoute == Routes.ExportReports.route,
                onClick = {
                    navController.navigate(Routes.ExportReports.route)
                }
            )
        }

        // Adicionar (todos)
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircleOutline, null) },
            label = { Text("Adicionar", fontSize = 10.sp) },
            selected = currentRoute == Routes.AddAnimal.route,
            onClick = {
                navController.navigate(Routes.AddAnimal.route)
            }
        )
    }
}