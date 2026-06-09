package br.unifor.petcare__ong.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.MedicalRecordRepository
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.model.MedicalRecord
import java.text.SimpleDateFormat
import java.util.Locale

class MedicalRecordViewModel : ViewModel() {
    private val repository = MedicalRecordRepository()
    private val animalRepository = AnimalRepository()

    private val _records = mutableStateOf<List<MedicalRecord>>(emptyList())
    val records: State<List<MedicalRecord>> = _records

    private val _animal = mutableStateOf<Animal?>(null)
    val animal: State<Animal?> = _animal

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun loadData(animalId: String) {
        _isLoading.value = true
        animalRepository.buscarAnimalPorId(animalId) {
            _animal.value = it
        }
        repository.listRecords(animalId) { list ->
            val sortedList = list.sortedByDescending { record ->
                try {
                    dateFormat.parse(record.date)
                } catch (e: Exception) {
                    null
                }
            }
            _records.value = sortedList
            _isLoading.value = false
        }
    }

    fun saveRecord(
        animalId: String,
        record: MedicalRecord,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.saveRecord(animalId, record, onSuccess, onFailure)
    }

    fun deleteRecord(animalId: String, recordId: String, onSuccess: () -> Unit) {
        repository.deleteRecord(animalId, recordId, onSuccess, {})
    }

    fun fetchRecordById(animalId: String, recordId: String, onResult: (MedicalRecord?) -> Unit) {
        repository.getRecordById(animalId, recordId, onResult)
    }
}
