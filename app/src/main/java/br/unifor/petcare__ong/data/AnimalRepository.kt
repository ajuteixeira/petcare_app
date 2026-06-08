package br.unifor.petcare__ong.data.repository

import br.unifor.petcare__ong.model.Animal
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
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun listarAnimais(onDataChange: (List<Animal>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animais = mutableListOf<Animal>()
                for (postSnapshot in snapshot.children) {
                    val animal = postSnapshot.getValue(Animal::class.java)
                    if (animal != null) {
                        animais.add(animal)
                    }
                }
                onDataChange(animais)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun buscarAnimalPorId(id: String, onResult: (Animal?) -> Unit) {
        database.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val animal = snapshot.getValue(Animal::class.java)
                onResult(animal)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }

    fun atualizarAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (animal.id.isEmpty()) return
        database.child(animal.id)
            .setValue(animal)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun atualizarDescricao(
        id: String,
        descricao: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child(id).child("descricao")
            .setValue(descricao)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deletarAnimal(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child(id)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
