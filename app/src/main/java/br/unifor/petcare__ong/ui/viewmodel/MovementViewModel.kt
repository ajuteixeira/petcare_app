package br.unifor.petcare__ong.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.MovementRepository
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.model.Movement
import java.text.SimpleDateFormat
import java.util.Locale

class MovementViewModel : ViewModel() {
    private val movementRepository = MovementRepository()
    private val animalRepository = AnimalRepository()

    private val _movements = mutableStateOf<List<Movement>>(emptyList())
    val movements: State<List<Movement>> = _movements

    private val _animal = mutableStateOf<Animal?>(null)
    val animal: State<Animal?> = _animal

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun loadData(animalId: String) {
        _isLoading.value = true
        animalRepository.buscarAnimalPorId(animalId) {
            _animal.value = it
        }
        movementRepository.listMovements(animalId) { list ->
            // Sort movements by date (descending - most recent first)
            val sortedList = list.sortedByDescending { movement ->
                try {
                    dateFormat.parse(movement.startDateTime)
                } catch (e: Exception) {
                    null
                }
            }
            _movements.value = sortedList
            _isLoading.value = false
        }
    }

    fun saveMovement(
        animalId: String,
        movement: Movement,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        movementRepository.saveMovement(animalId, movement, {
            if (movement.type == "Óbito") {
                animalRepository.buscarAnimalPorId(animalId) { animal ->
                    animal?.let {
                        val updatedAnimal = it.copy(status = "Falecido")
                        animalRepository.atualizarAnimal(updatedAnimal, onSuccess, onFailure)
                    } ?: onSuccess()
                }
            } else {
                onSuccess()
            }
        }, onFailure)
    }

    fun deleteMovement(animalId: String, movementId: String) {
        movementRepository.deleteMovement(animalId, movementId, {}, {})
    }

    fun fetchMovementById(animalId: String, movementId: String, onResult: (Movement?) -> Unit) {
        movementRepository.getMovementById(animalId, movementId, onResult)
    }
}
