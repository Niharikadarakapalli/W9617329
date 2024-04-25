package uk.ac.tees.mad.w9617329

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector




sealed class NavController(val route: String, val label: String, val icons: ImageVector) {

    object Home : NavController("home", "Home", Icons.Default.Home)

    object Account: NavController("account","Account", Icons.Default.AccountCircle)

}