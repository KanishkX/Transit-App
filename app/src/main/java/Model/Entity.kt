package Model

class Entity(private var id:Int, private var vehicle: Vehicle) {


    fun Entity(id: Int, vehicle: Vehicle){
        this.id = id
        this.vehicle = vehicle
    }
    fun getEntity(): Int { return this.id}
}