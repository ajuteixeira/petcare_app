package br.unifor.petcare__ong.data

import br.unifor.petcare__ong.model.MedicalRecord
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MedicalRecordRepository {

    private val database = FirebaseDatabase.getInstance().reference.child("animais")

    fun saveRecord(
        animalId: String,
        record: MedicalRecord,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val recordRef = database.child(animalId).child("prontuario")
        val id = if (record.id.isEmpty()) recordRef.push().key ?: return else record.id
        val recordWithId = record.copy(id = id)

        recordRef.child(id)
            .setValue(recordWithId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun listRecords(animalId: String, onDataChange: (List<MedicalRecord>) -> Unit) {
        database.child(animalId).child("prontuario")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val records = mutableListOf<MedicalRecord>()
                    for (postSnapshot in snapshot.children) {
                        val record = postSnapshot.getValue(MedicalRecord::class.java)
                        if (record != null) {
                            records.add(record)
                        }
                    }
                    onDataChange(records)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun getRecordById(animalId: String, recordId: String, onResult: (MedicalRecord?) -> Unit) {
        database.child(animalId).child("prontuario").child(recordId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val record = snapshot.getValue(MedicalRecord::class.java)
                    onResult(record)
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    fun deleteRecord(
        animalId: String,
        recordId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        database.child(animalId).child("prontuario").child(recordId)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
