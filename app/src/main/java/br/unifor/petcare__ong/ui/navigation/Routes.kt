package br.unifor.petcare__ong.ui.navigation

sealed class Routes(val route: String) {
    // só as rotas declaradas aqui podem acessar a classe

    object Login : Routes("login")

    object Register : Routes("register")

    object Dashboard : Routes("dashboard")

    object AnimalList : Routes("animal_list")

    object AnimalProfile : Routes("animal_profile/{animalId}") {
        fun createRoute(animalId: String) = "animal_profile/$animalId"
    }

    object AddAnimal : Routes("add_animal")

    object EditAnimal : Routes("edit_animal/{animalId}") {
        fun createRoute(animalId: String) = "edit_animal/$animalId"
    }

    object MedicalRecord : Routes("medical_record")

    object NewRecord : Routes("new_record")

    object MovementHistory : Routes("movement_history")

    object NewMovement : Routes("new_movement")

    object ExportReports : Routes("export_reports")
}