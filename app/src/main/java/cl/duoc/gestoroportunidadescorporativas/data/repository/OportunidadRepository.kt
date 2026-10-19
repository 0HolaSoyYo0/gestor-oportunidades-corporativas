package cl.duoc.gestoroportunidadescorporativas.data.repository

import cl.duoc.gestoroportunidadescorporativas.data.local.OportunidadDAO
import cl.duoc.gestoroportunidadescorporativas.data.local.OportunidadEntity
import kotlinx.coroutines.flow.Flow

interface OportunidadRepository {

    fun obtenerOportunidades(): Flow<List<OportunidadEntity>>

    suspend fun obtenerPorId(id: Long): OportunidadEntity?

    suspend fun guardar(oportunidad: OportunidadEntity)

}

class RoomOportunidadRepository(private val dao: OportunidadDAO): OportunidadRepository {

    override fun obtenerOportunidades(): Flow<List<OportunidadEntity>> {
        return dao.obtenerOportunidades()
    }

    override suspend fun obtenerPorId(id: Long): OportunidadEntity? {
        return dao.obtenerPorId(id)
    }

    override suspend fun guardar(oportunidad: OportunidadEntity) {
        return dao.guardar(oportunidad)
    }
}