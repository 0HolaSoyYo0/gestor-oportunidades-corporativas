package cl.duoc.gestoroportunidadescorporativas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface OportunidadDAO {

    @Query("SELECT * FROM oportunidades ORDER BY nombre ASC")
    fun obtenerOportunidades(): Flow<List<OportunidadEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :id")
    suspend fun obtenerPorId(id: Long): OportunidadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(oportunidad: OportunidadEntity)

}