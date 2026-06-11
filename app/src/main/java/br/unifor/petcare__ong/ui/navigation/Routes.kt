package br.unifor.petcare__ong.ui.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")

    object Register : Routes("register")

    object Dashboard : Routes("dashboard")

    object AnimalList : Routes("animal_list")

    object AnimalProfile : Routes("animal_profile/{animalId}") {
        fun createRoute(animalId: String) = "animal_profile/$animalId"
    }
    // A TELA SABE PELO ID QUAL ANIMAL BUS CAR NO BANCO DE DADOS
    object AddAnimal : Routes("add_animal")

    object EditAnimal : Routes("edit_animal/{animalId}") {
        fun createRoute(animalId: String) = "edit_animal/$animalId"
    }

    object MedicalRecord : Routes("medical_record/{animalId}") {
        fun createRoute(animalId: String) = "medical_record/$animalId"
    }

    object NewRecord : Routes("new_record/{animalId}") {
        fun createRoute(animalId: String) = "new_record/$animalId"
    }

    object EditRecord : Routes("edit_record/{animalId}/{recordId}") {
        fun createRoute(animalId: String, recordId: String) = "edit_record/$animalId/$recordId"
    }

    object MovementHistory : Routes("movement_history/{animalId}") {
        fun createRoute(animalId: String) = "movement_history/$animalId"
    }

    object NewMovement : Routes("new_movement/{animalId}") {
        fun createRoute(animalId: String) = "new_movement/$animalId"
    }

    object EditMovement : Routes("edit_movement/{animalId}/{movementId}") {
        fun createRoute(animalId: String, movementId: String) = "edit_movement/$animalId/$movementId"
    }

    object ExportReports : Routes("export_reports")
}
