package co.idearun.feature

import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.idearun.common.BaseMethod
import co.idearun.feature.databinding.ActivityMainBinding
import co.idearun.feature.drawer.SortedLessonListAdapter
import co.idearun.feature.viewmodel.FormViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainActivity : BaseActivity(), KoinComponent, MainListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    val baseMethod: BaseMethod by inject()
    private val viewModel: FormViewModel by viewModel()
    private lateinit var sortedFormListAdapter: SortedLessonListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.appBarMain.toolbar)


        binding.viewmodel = viewModel
        binding.listener = this
        binding.appBarMain.listener = this
        binding.lifecycleOwner = this
        initView()


    }


    private fun initView() {
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        sortedFormListAdapter = SortedLessonListAdapter()
        binding.categoryRv.apply {
            adapter = sortedFormListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {


            }

            override fun onDrawerOpened(drawerView: View) {
                fetchFormList()
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })

        binding.about.setOnClickListener {
            binding.drawerLayout.close()
            navController.navigate(R.id.about)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun fetchFormList() {
        viewModel.getLessonsList(false)
        viewModel.retrieveDBLessonList()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)){
            binding.drawerLayout.close()

        }else{
            super.onBackPressed()

        }
    }

}