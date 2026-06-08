package br.unifor.petcare__ong.data

import br.unifor.petcare__ong.model.Movement
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MovementRepository {

    private val database = FirebaseDatabase.getInstance().reference.child("animais")

    fun saveMovement(
        animalId: String,
        movement: Movement,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val movementRef = database.child(animalId).child("movimentacoes")
        val id = if (movement.id.isEmpty()) movementRef.push().key ?: return else movement.id
        val movementWithId = movement.copy(id = id)

        movementRef.child(id)
            .setValue(movementWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun listMovements(animalId: String, onDataChange: (List<Movement>) -> Unit) {
        database.child(animalId).child("movimentacoes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movements = mutableListOf<Movement>()
                    for (postSnapshot in snapshot.children) {
                        val movement = postSnapshot.getValue(Movement::class.java)
                        if (movement != null) {
                            movements.add(movement)
                        }
                    }
                    onDataChange(movements)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getMovementById(animalId: String, movementId: String, onResult: (Movement?) -> Unit) {
        database.child(animalId).child("movimentacoes").child(movementId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movement = snapshot.getValue(Movement::class.java)
                    onResult(movement)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    fun deleteMovement(
        animalId: String,
        movementId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child(animalId).child("movimentacoes").child(movementId)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
