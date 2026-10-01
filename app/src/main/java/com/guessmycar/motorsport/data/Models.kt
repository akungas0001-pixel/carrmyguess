package com.guessmycar.motorsport.data

data class Car(
    val id: Int,
    val regionId: String,
    val region: String,
    val country: String,
    val manufacturer: String,
    val model: String,
    val generation: String,
    val productionYear: Int,
    val imageResource: String,
    val difficulty: String,
    val description: String
) {
    val displayName: String
        get() = listOf(manufacturer, model, generation).filter { it.isNotBlank() }.joinToString(" ")
}

data class Stage(
    val id: Int,
    val levelNumber: Int,
    val name: String,
    val regionId: String,
    val carId: Int,
    val starsEarned: Int = 0,
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false,
    val objectiveName: String
)

data class Region(
    val id: String,
    val name: String,
    val code: String,
    val flag: String,
    val title: String,
    val subtitle: String,
    val totalCars: Int,
    val description: String
)
