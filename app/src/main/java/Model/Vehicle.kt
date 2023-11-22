package Model

class Vehicle(var position: Position, var trip: Trip){

    fun Vehicle(position: Position, trip: Trip){
        this.position = position
        this.trip = trip
    }
}