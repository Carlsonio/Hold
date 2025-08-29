package com.example.hold.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hold.ui.noteedit.NoteEditScreen
import com.example.hold.ui.noteslist.NotesScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "notes_list") {
        composable("notes_list") {
            NotesScreen(
                onNoteClick = { noteId ->
                    navController.navigate("note_edit/$noteId")
                },
                onAddNoteClick = {
                    navController.navigate("note_edit/-1")
                }
            )
        }
        composable(
            route = "note_edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) {
            NoteEditScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}