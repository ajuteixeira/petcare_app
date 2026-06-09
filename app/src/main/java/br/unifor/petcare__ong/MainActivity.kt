package br.unifor.petcare__ong

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.unifor.petcare__ong.ui.navigation.AppNavGraph
import br.unifor.petcare__ong.ui.theme.PETCARE__ONGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PETCARE__ONGTheme {
                AppNavGraph()
            }
        }
    }
}
