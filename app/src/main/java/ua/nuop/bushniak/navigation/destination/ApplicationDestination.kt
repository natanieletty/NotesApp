package ua.nuop.bushniak.navigation.destination

sealed class ApplicationDestination(val route: String) {
    data object MainScreens : ApplicationDestination("main_screens") {
        data object Notes : ApplicationDestination("notes")
        data object CreateNote : ApplicationDestination("create_note")
        data object Settings : ApplicationDestination("settings")
        data object PasswordInput : ApplicationDestination("password")
        data object CreatePassword : ApplicationDestination("create_password")
        data object PrivateNotes : ApplicationDestination("private_notes")
        data object Trash : ApplicationDestination("trash")
    }
}