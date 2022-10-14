package no.hiof.geofishing.models

data class User(val uid: Int, var name: String, var points: Int){

    companion object {
        fun getUsers() : List<User> {
            return listOf(
                User(1, "Simen Jacobsen", 150),
                User(2, "Ole-Jørgen Andersen", 135),
                User(3, "Erik Jarem", 57),
                User(4, "Tom-Heine", 328357)
            )
        }
    }
}
