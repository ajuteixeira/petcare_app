package br.unifor.petcare__ong.data.repository

import br.unifor.petcare__ong.model.Animal
import com.google.firebase.database.FirebaseDatabase

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
}