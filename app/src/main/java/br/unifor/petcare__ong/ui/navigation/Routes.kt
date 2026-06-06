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

    object MedicalRecord : Routes("medical_record/{animalId}") {
        fun createRoute(animalId: String) = "medical_record/$animalId"
    }

    object NewRecord : Routes("new_record/{animalId}?recordId={recordId}") {
        fun createRoute(animalId: String, recordId: String? = null) = 
            if (recordId != null) "new_record/$animalId?recordId=$recordId" 
            else "new_record/$animalId"
    }

    object MovementHistory : Routes("movement_history")

    object NewMovement : Routes("new_movement")

    object ExportReports : Routes("export_reports")
}