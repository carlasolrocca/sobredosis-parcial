package ar.edu.unsam.algo2

import java.time.LocalDate

/*
Quiero modelar un sistema que gestione la programacion de un canal.
Programa:
    *tiene titulo
    *varios conductores -principales- (hay secundarios?)
    *un presupuesto base
    *sponsors de publicidad
    *el dia que sale (Lunes / Domingo) (puede salir más de un dia?)
    *la duracion del programa (en minutos)

    *tambien se conoce los ratings de las ultimas 5 emisiones
*/

// *** PUNTO 1: Restricciones ***
class Programa(val titulo : String,
               val conductores: MutableList<String>,    //No se si hay class Conductor
               val presupuestoBase : Double,
               val sponsors: MutableList<String>,       //No se si hay class Sponsor
               val diaEmision : LocalDate,
               val duracionMinutos : Int)
{
    val ratingUltimasEmisiones : MutableList<Double> = mutableListOf()

    fun agregarRating(rating : Double) {
        if(!cantidadMaxRatings()){
            ratingUltimasEmisiones.add(rating)
        }else{
            throw BusinessException("No se pueden agregar mas ratings")
        }
    }

    fun cantidadMaxRatings() : Boolean = ratingUltimasEmisiones.size == 5
}


abstract class Restriccion {
    abstract fun cumpleRestriccion(programa: Programa): Boolean
}

class RatingSuperiorA() : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean {
        TODO("Not yet implemented")
    }
}

class CantidadMaxConductores() : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean {
        TODO("Not yet implemented")
    }
}

class ConductorEspecifico() : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean {
        TODO("Not yet implemented")
    }
}

class PresupuestoMaximo() : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean {
        TODO("Not yet implemented")
    }
}

class RestriccionCombinada() : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean {
        TODO("Not yet implemented")
    }
}


// *** FIN PUNTO 1 ***



//Exceptions
class BusinessException(mensaje: String) : Throwable() {}