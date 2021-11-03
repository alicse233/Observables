package com.example.observableslearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.observableslearning.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(MainViewModel::class.java)

//        simple life cycle aware observable
//        still used in java projects
        binding.btnLive.setOnClickListener {
            viewModel.triggerLiveData()
        }

//        hot flow -> emit even there is no collector
//        saves a value in it and does not resend the same value like if value is not changes then no emission.
//        when activity rotated then values are emitted
//        flow is lifecycle observable with support of coroutines
//        it works well with kotlin coroutines
//        whether rotate screen the state is safe which is put within it

        binding.btnStateFlow.setOnClickListener {
//            simply put some values within the flow and then observe changes
            viewModel.triggerStateFlow()
        }


//        it a one time executed observable
//        usually used for processing which is needed ony one time
//        after its execution it quits.
//        note: it does not contain state like when we rotate the device
//        activity gets recreated and we are unable to fetch our state back
        binding.btnSFlow.setOnClickListener {
//            we need a suspend function for processing this flow
            lifecycleScope.launch {
//                collect the emission coming from this normal flow
//                it will stop or quit after processing
                viewModel.triggerFlow().collectLatest {
                    binding.tvFlow.text = it
                }
            }
        }

//        also a flow. for one time event
//        both state flow and shared flow are hot flows
//        which means both send events even when there are no collectors
//        it emits one time event even the screen is rotated
//        usually used for snack bar
        binding.btnSharedFlow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }

        subscribeToUI()
    }

    private fun subscribeToUI() {
        viewModel.liveData.observe(this) {
            binding.tvLive.text = it
        }

        lifecycleScope.launchWhenCreated {
            viewModel.stateFlow.collectLatest {
                binding.tvStateFlow.text = it
            }
        }

        lifecycleScope.launch {
            viewModel.sharedFlow.collectLatest {
                Snackbar.make(
                    binding.root,
                    it, Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}