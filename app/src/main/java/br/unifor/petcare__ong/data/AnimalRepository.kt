package br.unifor.petcare__ong.data.repository

import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.model.MedicalRecord
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AnimalRepository {

    private val database =
        FirebaseDatabase.getInstance().reference.child("animais")

    fun salvarAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val id = database.push().key ?: return

        val novoAnimal = animal.copy(id = id)

        database.child(id)
            .setValue(novoAnimal)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun deletarAnimal(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        database.child(id)
            .removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun buscarAnimalPorId(id: String, onResult: (Animal?) -> Unit) {
        database.child(id).get().addOnSuccessListener { snapshot ->
            val animal = snapshot.getValue(Animal::class.java)
            onResult(animal)
        }.addOnFailureListener {
            onResult(null)
        }
    }

    fun adicionarRegistroMedico(
        animalId: String,
        registro: MedicalRecord,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val registroId = database.child(animalId).child("prontuario").push().key ?: return
        val novoRegistro = registro.copy(id = registroId, animalId = animalId)

        database.child(animalId).child("prontuario").child(registroId)
            .setValue(novoRegistro)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun atualizarRegistroMedico(
        animalId: String,
        registro: MedicalRecord,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child(animalId).child("prontuario").child(registro.id)
            .setValue(registro)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deletarRegistroMedico(
        animalId: String,
        registroId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child(animalId).child("prontuario").child(registroId)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun buscarRegistrosMedicos(
        animalId: String,
        onResult: (List<MedicalRecord>) -> Unit
    ) {
        database.child(animalId).child("prontuario")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val registros = mutableListOf<MedicalRecord>()
                    for (postSnapshot in snapshot.children) {
                        val registro = postSnapshot.getValue(MedicalRecord::class.java)
                        if (registro != null) {
                            registros.add(registro)
                        }
                    }
                    onResult(registros)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(emptyList())
                }
            })
    }

    fun buscarAnimais(onResult: (List<Animal>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animais = mutableListOf<Animal>()
                for (postSnapshot in snapshot.children) {
                    val animal = postSnapshot.getValue(Animal::class.java)
                    if (animal != null) {
                        animais.add(animal)
                    }
                }
                onResult(animais)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList())
            }
        })
    }
}