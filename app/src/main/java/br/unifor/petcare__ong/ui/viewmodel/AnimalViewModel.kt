package br.unifor.petcare__ong.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.model.Animal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnimalViewModel(private val repository: AnimalRepository = AnimalRepository()) : ViewModel() {

    private val _animais = MutableStateFlow<List<Animal>>(emptyList())
    val animais: StateFlow<List<Animal>> = _animais.asStateFlow()

    private val _animalSelecionado = MutableStateFlow<Animal?>(null)
    val animalSelecionado: StateFlow<Animal?> = _animalSelecionado.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun listarAnimais() {
        _isLoading.value = true
        repository.listarAnimais { list ->
            _animais.value = list
            _isLoading.value = false
        }
    }

    fun buscarAnimalPorId(id: String) {
        _isLoading.value = true
        repository.buscarAnimalPorId(id) { animal ->
            _animalSelecionado.value = animal
            _isLoading.value = false
        }
    }

    fun salvarAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.salvarAnimal(animal, onSuccess, onFailure)
    }

    fun atualizarAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.atualizarAnimal(animal, onSuccess, onFailure)
    }

    fun deletarAnimal(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.deletarAnimal(id, onSuccess, onFailure)
    }

    fun atualizarDescricao(
        id: String,
        descricao: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.atualizarDescricao(id, descricao, onSuccess, onFailure)
    }
}
