package ar.edu.unsam.algo2

/*
Quiero modelar un sistema que gestione la programacion de un canal.
Programa:
    *tiene titulo
    *varios conductores -principales- (hay secundarios?)
    *un presupuesto base
    *sponsors de publicidad
    *el dia que sale (Lunes / Domingo) (puede salir más de un dia?)
    *la duracion del programa (en minutos)

    *tambien se conoce los ratings de las ultimas 5 emisiones (del mismo programa o de los ultimos
    5 programas que pasó el canal?)
*/

// *** PUNTO 1: Restricciones ***
/*
Mantener un programa implica una serie de criterios VARIABLES que son restricciones
*/

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

class Programa {}
// *** FIN PUNTO 1 ***