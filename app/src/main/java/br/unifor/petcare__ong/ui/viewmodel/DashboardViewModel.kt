package br.unifor.petcare__ong.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.model.Animal
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

data class DashboardState(
    val totalAnimals: Int = 0,
    val availableCount: Int = 0,
    val inTreatmentCount: Int = 0,
    val dogsCount: Int = 0,
    val catsCount: Int = 0,
    val dogsPercentage: Float = 0f,
    val catsPercentage: Float = 0f,
    val entriesLast30Days: Int = 0,
    val adoptionsLast30Days: Int = 0,
    val deathsLast30Days: Int = 0,
    val outsideBreakdown: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = true
)

class DashboardViewModel : ViewModel() {
    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        _state.value = _state.value.copy(isLoading = true)
        
        val database = FirebaseDatabase.getInstance().reference.child("animais")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val thirtyDaysAgo = Calendar.getInstance().apply { 
                    add(Calendar.DAY_OF_YEAR, -30) 
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
                
                var total = 0
                var available = 0
                var inTreatment = 0
                var dogs = 0
                var cats = 0
                
                var entriesCount = 0
                var adoptionsCount = 0
                var deathsCount = 0
                val breakdown = mutableMapOf<String, Int>()

                val entradaTypes = listOf("Resgate", "Abandono", "Devolução", "Transferência")
                val saidaTypes = listOf("Evento de adoção", "Cirurgia", "Consulta", "Lar temporário", "Passeio", "Óbito", "Outro")
                
                for (animalSnapshot in snapshot.children) {
                    val animal = animalSnapshot.getValue(Animal::class.java)
                    if (animal != null) {
                        total++
                        if (animal.status.equals("Disponível", ignoreCase = true)) available++
                        if (animal.status.equals("Em Tratamento", ignoreCase = true)) inTreatment++
                        
                        val especie = animal.especie.lowercase()
                        if (especie.contains("cachorro") || especie.contains("cão")) dogs++
                        if (especie.contains("gato")) cats++
                        
                        // Processar movimentações do animal
                        val movementsSnapshot = animalSnapshot.child("movimentacoes")
                        for (movementSnap in movementsSnapshot.children) {
                            val type = movementSnap.child("tipo").getValue(String::class.java) ?: ""
                            val status = movementSnap.child("status").getValue(String::class.java) ?: ""
                            val dateStr = movementSnap.child("dataHoraInicio").getValue(String::class.java) ?: ""
                            
                            // Lógica para "Fora do Abrigo" (Status: Em progresso + Tipo: Saída)
                            // Independente de data, pois é o estado atual
                            if (saidaTypes.contains(type) && status.equals("Em progresso", ignoreCase = true)) {
                                breakdown[type] = (breakdown[type] ?: 0) + 1
                            }

                            // Lógica para estatísticas dos últimos 30 dias
                            if (dateStr.isNotEmpty()) {
                                try {
                                    val datePart = dateStr.split(" ")[0]
                                    val date = sdf.parse(datePart)
                                    if (date != null && (date.after(thirtyDaysAgo) || date == thirtyDaysAgo)) {
                                        if (entradaTypes.contains(type)) entriesCount++
                                        if (type == "Evento de adoção") adoptionsCount++
                                        if (type == "Óbito") deathsCount++
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }

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
                    entriesLast30Days = entriesCount,
                    adoptionsLast30Days = adoptionsCount,
                    deathsLast30Days = deathsCount,
                    outsideBreakdown = breakdown,
                    isLoading = false
                )
            }

            override fun onCancelled(error: DatabaseError) {
                _state.value = _state.value.copy(isLoading = false)
            }
        })
    }
}
