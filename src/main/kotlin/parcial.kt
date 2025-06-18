package ar.edu.unsam.algo2

import java.time.LocalDate

//Exceptions
class BusinessException(mensaje: String) : Throwable()
/*
* Tengo un Programa con sus atributos. Ese Programa tiene que cumplir Restricciones.
* Hay una clase Restriccion con metodo cumpleRestriccion.
* Si el Programa no cumple las restricciones, se ejecutan Acciones.
* Hay una clase Accion y hay una lista de acciones asociadas a la Restriccion.
* Voy a tener una Grilla de programacion que posee Programas
* Habla de una persona encargada haga ciertas cosas dada una Grilla. No se si es una entidad
 necesaria o no.
*/
// *** PUNTO 1: Restricciones ***
// Clase Programa
class Programa(val titulo : String,
               val conductoresPrincipales: MutableList<String>,    //No se si hay class Conductor
               val presupuestoBase : Double,
               val sponsors: MutableList<String>,       //No se si hay class Sponsor
               val diaEmision : LocalDate,
               val duracionMinutos : Int)
{
    val restriccionesAsociadas : MutableList<Restriccion> = mutableListOf()
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
    fun loConduce(conductor : String) : Boolean = conductoresPrincipales.contains(conductor)
}

// Clase Restriccion
abstract class Restriccion {
    val accionesAsociadas : MutableList<Accion> = mutableListOf()       //Punto 2 y 3

    abstract fun cumpleRestriccion(programa: Programa): Boolean

    fun ejecutarAcciones(){
        accionesAsociadas.forEach { it.execute() }
    }
}

class RatingSuperiorA(val valorRating : Double) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.ratingPromedio() > valorRating
}

class CantidadMaxConductores(val cantidad : Int) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.cantidadConductores() <= cantidad
}

class ConductorEspecifico(val conductorDeseado : String) : Restriccion() {
    override fun cumpleRestriccion(programa: Programa): Boolean = programa.loConduce(conductorDeseado)
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

// *** PUNTO 2: Acciones ***
//Una accion es partir un programa en dos:
//mitad conductores en un algo y la segunda mitad en otro.
//presupuesto dividido a la mitad. los sponsors se comparten
//se parte la duracion en minutos a la mitad para c/u
//titulo:"{primera palabra del título} en el aire!" y "{segunda palabra del título}"
// o "Programa sin nombre" si no hubiera 2da palabra
//tienen los mismos dias de emision

//el programa desaparece y lo reemplaza los simpsons

//se fusionan el programa con el siguiente de la GRILLA (hay class Grilla?)

//el programa se mueve de dia

//hay una PERSONA QUE PROGRAMA EL CANAL (class Canal? class PersonaEncargada?)
interface Accion {
    abstract fun execute()
}

class DividirPrograma() : Accion {
    override fun execute() {
        TODO("Implementar la lógica para dividir el programa")
    }
}

class ReemplazarPorLosSimpsons() : Accion {
    override fun execute() {
        TODO("Implementar la lógica para reemplazar el programa por Los Simpsons")
    }
}

class FusionarPrograma() : Accion {
    override fun execute() {
        TODO("Implementar la lógica para fusionar el programa con el siguiente en la grilla")
    }
}

class CambiarDiaEmision() : Accion {
    override fun execute() {
        TODO("Implementar la lógica para cambiar el día de emisión del programa")
    }
}

// *** FIN PUNTO 2 ***