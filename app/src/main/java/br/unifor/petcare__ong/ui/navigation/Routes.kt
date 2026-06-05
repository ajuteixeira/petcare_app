package br.unifor.petcare__ong.ui.navigation

sealed class Routes(val route: String) {
    // só as rotas declaradas aqui podem acessar a classe

    object Login : Routes("login")

    object Register : Routes("register")

    object Dashboard : Routes("dashboard")

    object AnimalList : Routes("animal_list")

    object AnimalProfile : Routes("animal_profile")

    object AddAnimal : Routes("add_animal")

    object MedicalRecord : Routes("medical_record")

    object NewRecord : Routes("new_record")

    object MovementHistory : Routes("movement_history")

    object NewMovement : Routes("new_movement")

    object ExportReports : Routes("export_reports")
}