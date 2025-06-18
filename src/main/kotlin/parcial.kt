package ar.edu.unsam.algo2

import java.time.LocalDate

//Exceptions
class BusinessException(mensaje: String) : Throwable()

// *** PUNTO 1: Restricciones ***
// Clase Programa
class Programa(val titulo : String,
               val conductoresPrincipales: MutableList<String>,    //No se si hay class Conductor
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

    fun cantidadMaxRatings() : Boolean = ratingUltimasEmisiones.size == 5   //Solo admite 5 ratings
    fun ratingPromedio() : Double = ratingUltimasEmisiones.average()
    fun cantidadConductores() : Int = conductoresPrincipales.size
}

// Clase Restriccion
abstract class Restriccion {
    abstract fun cumpleRestriccion(programa: Programa): Boolean
}

class RatingSuperiorA(val valorRating : Double) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.ratingPromedio() > valorRating
}

class CantidadMaxConductores(val cantidad : Int) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.cantidadConductores() <= cantidad
}

//interpreto que con que este alguno de esos conductores en la lista de principales, ya está ok
class ConductorEspecifico() : Restriccion() {
    val conductoresEspecificos : MutableList<String> = mutableListOf("Juan Carlos Pérez Loizeau", "Mario Monteverde")
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.conductoresPrincipales.any { it in conductoresEspecificos }
}

class PresupuestoMaximo(val tope : Double) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.presupuestoBase <= tope
}

//Composite de restricciones OR
class RestriccionCombinadaOR(val restricciones : MutableSet<Restriccion>) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = restricciones.any { it.cumpleRestriccion(programa) }
}

//Composite de restricciones AND
class RestriccionCombinadaAND(val restricciones : MutableSet<Restriccion>) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = restricciones.all { it.cumpleRestriccion(programa) }
}

// *** FIN PUNTO 1 ***



