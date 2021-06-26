package co.idearun.learningapp

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.idearun.learningapp.common.BaseMethod
import co.idearun.learningapp.databinding.ActivityMainBinding
import co.idearun.learningapp.feature.BaseActivity
import co.idearun.learningapp.feature.drawer.SortedFormListAdapter
import co.idearun.learningapp.feature.viewmodel.FormViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent

class MainActivity : BaseActivity(), KoinComponent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    val baseMethod: BaseMethod by inject()
    private val viewModel: FormViewModel by viewModel()
    private lateinit var sortedFormListAdapter: SortedFormListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.appBarMain.toolbar)
        baseMethod.showBackBtn(supportActionBar)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        initView()
        initData()


    }

    private fun initData() {
        fetchFormList()

    }

    private fun initView() {
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        sortedFormListAdapter = SortedFormListAdapter()
        binding.categoryRv.apply {
            adapter = sortedFormListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        binding.about.setOnClickListener {
            navController.navigate(R.id.about)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun fetchFormList() {
        viewModel.retrieveDBFormList()
    }


}