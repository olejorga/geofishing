package no.hiof.geofishing.models

data class Task(val uid: Int, var name: String, val completed: Boolean = false) {

    companion object {
        fun getTasks(): List<Task> {
            return listOf(
                Task(0,"Beer",false),
                Task(1,"Beer",false),
                Task(2,"Beer",false),
                Task(3,"Beer",false),
                Task(4,"Beer",false)
            )
        }
    }
}