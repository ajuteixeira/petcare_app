package br.unifor.petcare__ong.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.AnimalRepository

data class DashboardState(
    val totalAnimals: Int = 0,
    val availableCount: Int = 0,
    val inTreatmentCount: Int = 0,
    val dogsCount: Int = 0,
    val catsCount: Int = 0,
    val dogsPercentage: Float = 0f,
    val catsPercentage: Float = 0f,
    val isLoading: Boolean = true
)

class DashboardViewModel : ViewModel() {
    private val animalRepository = AnimalRepository()

    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        _state.value = _state.value.copy(isLoading = true)
        animalRepository.listarAnimais { animais ->
            val total = animais.size
            val available = animais.count { it.status.equals("Disponível", ignoreCase = true) }
            val inTreatment = animais.count { it.status.equals("Em Tratamento", ignoreCase = true) }
            val dogs = animais.count { it.especie.contains("Cachorro", ignoreCase = true) || it.especie.contains("Cão", ignoreCase = true) }
            val cats = animais.count { it.especie.contains("Gato", ignoreCase = true) }
            
            val dogsPercent = if (total > 0) dogs.toFloat() / total else 0f
            val catsPercent = if (total > 0) cats.toFloat() / total else 0f

            _state.value = DashboardState(
                totalAnimals = total,
                availableCount = available,
                inTreatmentCount = inTreatment,
                dogsCount = dogs,
                catsCount = cats,
                dogsPercentage = dogsPercent,
                catsPercentage = catsPercent,
                isLoading = false
            )
        }
    }
}
