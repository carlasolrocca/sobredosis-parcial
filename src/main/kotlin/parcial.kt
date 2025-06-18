package ar.edu.unsam.algo2

import java.time.LocalDate

//Exceptions
class BusinessException(mensaje: String) : Throwable()
/*
* Tengo un Programa con sus atributos. Ese Programa tiene que cumplir Restricciones.
* Hay una clase Restriccion con metodo cumpleRestriccion.
* Si el Programa no cumple las restricciones, se ejecutan Acciones.
* Hay una clase Accion y hay una lista de acciones asociadas a c/Restriccion.
* La Accion tiene un efecto sobre los Programas y por ende en la Grilla.
* Voy a tener una Grilla de programacion que posee Programas.
*/

// *** PUNTO 1: Restricciones ***
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
    fun mitadConductores() : Int = conductoresPrincipales.size / 2

    //take toma los primeros "n" elementos y drop descarta los primeros "n" elementos
    fun primeraMitadConductores() : MutableList<String> = conductoresPrincipales.take(mitadConductores()).toMutableList()
    fun segundaMitadConductores() : MutableList<String> = conductoresPrincipales.drop(mitadConductores()).toMutableList()

    fun mitadPresupuesto() : Double = presupuestoBase / 2
    fun mitadDuracion() : Int = duracionMinutos / 2
    fun dividirTitulo() : List<String> = titulo.split(" ")
}


abstract class Restriccion {
    val accionesAsociadas : MutableList<Accion> = mutableListOf()       //Punto 2 y 3

    abstract fun cumpleRestriccion(programa: Programa): Boolean

    fun ejecutarAcciones(programa : Programa, grilla : Grilla) {
        accionesAsociadas.forEach { it.execute(programa, grilla) }
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

interface Accion {
    abstract fun execute(programa : Programa, grilla: Grilla)
}

class DividirPrograma() : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {
        val primeraMitad = programa.primeraMitadConductores()
        val segundaMitad = programa.segundaMitadConductores()

        fun calculoSegundoTitulo(programa : Programa) : String {
            val tituloPotencial = programa.dividirTitulo().getOrNull(1) //Hago calculo de 2da palabra
            if(tituloPotencial != null){
                return tituloPotencial
            } else {
                return "Programa sin nombre"
            }
        }

        val programa1 = Programa(
            titulo = "${programa.dividirTitulo().first()} en el aire!",   //delimita por espacio y trae la 1era palabra
            conductoresPrincipales = primeraMitad,
            presupuestoBase = programa.mitadPresupuesto(),
            sponsors = programa.sponsors,
            diaEmision = programa.diaEmision,
            duracionMinutos = programa.mitadDuracion())

        val programa2 = Programa(
            titulo = "${calculoSegundoTitulo(programa)}", //busca la 2da palabra o devuelve null
            conductoresPrincipales = segundaMitad,
            presupuestoBase = programa.mitadPresupuesto(),
            sponsors = programa.sponsors,
            diaEmision = programa.diaEmision,
            duracionMinutos = programa.mitadDuracion())
    }
}

class ReemplazarPorLosSimpsons() : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {
        TODO("Implementar la lógica para reemplazar el programa por Los Simpsons")
    }
}

class FusionarPrograma() : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {
        TODO("Implementar la lógica para fusionar el programa con el siguiente en la grilla")
    }
}

class CambiarDiaEmision() : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {
        TODO("Implementar la lógica para cambiar el día de emisión del programa")
    }
}

// *** FIN PUNTO 2 ***

/*
Hay una clase Grilla que:
1. agrega restricciones y sus acciones
2. tiene una lista de Programas
3. */

// *** PUNTO 3: El Proceso ***
class Grilla(){

}
// *** FIN PUNTO 3 ***