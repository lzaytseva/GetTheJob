package ru.practicum.android.diploma.core.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {

    private val binding: ActivityRootBinding by lazy { ActivityRootBinding.inflate(layoutInflater) }

    val toolbar: Toolbar get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.teamFragment,
                R.id.favoritesFragment,
                R.id.searchFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.bottomNavigationViewLineAbove.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.bottomNavigationViewLineAbove.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Toolbar
     *
     * Прикрепление navigationIcon:
     *
     *         binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back) в onResume()
     *         binding.toolbar.navigationIcon = null в onPause()
     *
     * Смена иконки:
     *
     *      menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_active)
     *
     * Смена видимости пункта меню:
     *
     *      item.isVisible =
     *      false в onPause()
     *      true в onResume()
     *
     * Смена текста в title:
     *      toolbar.setTitle()
     * OnClick:
     *
     *      item.setOnMenuItemClickListener { MenuItem ->
     *          // doSomething()
     *          true
     *      }
     */

    private fun setupToolbar() {
        menuInflater.inflate(R.menu.toolbar_menu, binding.toolbar.menu)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}



