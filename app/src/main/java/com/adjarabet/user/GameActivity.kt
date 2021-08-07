package com.adjarabet.user

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adjarabet.bot.BotCommunicatorService
import com.adjarabet.user.databinding.ActivityGameBinding
import java.util.*
import kotlin.collections.ArrayList


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var inputWordsList: ArrayList<String> = arrayListOf<String>()
    private var currentWordsList: ArrayList<String> = arrayListOf<String>()
    private var answersList: ArrayList<String> = arrayListOf<String>()
    private var errorsList: ArrayList<String> = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerViewWords.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewWords.setHasFixedSize(true)

        binding.btnSend.setOnClickListener {
            readAndValidate()
        }

    }

    private fun readAndValidate(){
        var userText: String = binding.edtWords.text.toString().trim()

        if(currentWordsList.isEmpty()){
            if(userText.contains(" ", ignoreCase = true) || userText.isEmpty()){

                binding.edtWords.isEnabled = false
                binding.btnSend.isEnabled = false
                answersList.add("You lost, you should have started with a single word")
                updateRecyclerView()

//                Toast.makeText(applicationContext,"You lost, you should have started with a single word",Toast.LENGTH_SHORT).show()
            }else{
                currentWordsList.add(userText)
                appendAndCallService()
            }
        }else{
            val list: List<String> = listOf(*userText.split(" ").toTypedArray())
            inputWordsList = ArrayList(list)
            var isValid = true

            if(inputWordsList.size == currentWordsList.size || inputWordsList.size < currentWordsList.size){
                binding.edtWords.isEnabled = false
                binding.btnSend.isEnabled = false
                answersList.add("You lost, not enough words entered")
                isValid = false
                updateRecyclerView()
            }

            if(isValid){
//            Toast.makeText(applicationContext,
//                currentWordsList.size.toString() + " " + inputWordsList.size.toString(),
//                Toast.LENGTH_SHORT).show()
                if(inputWordsList.size - currentWordsList.size > 1){
                    binding.edtWords.isEnabled = false
                    binding.btnSend.isEnabled = false
                    answersList.add("You lost, you added more than one word")
                    updateRecyclerView()
                }else{
//                Toast.makeText(applicationContext,
//                        currentWordsList.size.toString() + " " + inputWordsList.size.toString(),
//                        Toast.LENGTH_SHORT).show()
                    for (x in 0 until currentWordsList.size){
//                    Toast.makeText(applicationContext,
//                            currentWordsList[x] + " " + inputWordsList[inputWordsList.size-1],
//                            Toast.LENGTH_SHORT).show()
                        if(currentWordsList[x] == inputWordsList[inputWordsList.size-1]){
                            binding.edtWords.isEnabled = false
                            binding.btnSend.isEnabled = false
                            answersList.add("You lost, you added a word that was already used '" +
                                    inputWordsList[inputWordsList.size-1] + "'")
                            updateRecyclerView()
                            isValid = false
                            break
                        }else{
                            isValid = true
                        }
                    }

                    if(isValid){
                        errorsList.clear()
                        for (x in 0 until currentWordsList.size){
                            if(currentWordsList[x] != inputWordsList[x]){
                                binding.edtWords.isEnabled = false
                                binding.btnSend.isEnabled = false
                                errorsList.add("'" + inputWordsList[x] +
                                        "' is incorrect, should have been " + "'" + currentWordsList[x] + "'."  )
                                isValid = false
                            }
                        }

                        if(isValid){
//                        currentWordsList.clear()
//                        currentWordsList = inputWordsList
//                        Toast.makeText(applicationContext,
//                                currentWordsList.size.toString() + " " + inputWordsList.size.toString(),
//                                Toast.LENGTH_SHORT).show()
                            currentWordsList.add(inputWordsList[inputWordsList.size-1])
                            appendAndCallService()
                        }else{
                            answersList.add("You lost, " + errorsList.joinToString(separator = " "))
                            updateRecyclerView()
                        }

                    }

                }
            }



//            Toast.makeText(applicationContext,inputWordsList[2],Toast.LENGTH_SHORT).show()

        }
    }

    private fun appendAndCallService(){
        answersList.add(currentWordsList.joinToString(separator = " "))
        inputWordsList.clear()
        binding.edtWords.setText("")
        updateRecyclerView()

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

                val localBinder = p1 as BotCommunicatorService.LocalBinder
                val service = localBinder.getInstance()
                val response:String = service.getBotAnswer()

                if(response == "TOO_MUCH_FOR_ME"){
                    currentWordsList.clear()
                    binding.edtWords.isEnabled = false
                    binding.btnSend.isEnabled = false
                }
                currentWordsList.add(response)
//            Toast.makeText(applicationContext,response,Toast.LENGTH_SHORT).show()
                answersList.add(currentWordsList.joinToString(separator = " "))
                updateRecyclerView()

            }

            override fun onServiceDisconnected(p0: ComponentName?) {

            }

        }

        val intent = Intent(this, BotCommunicatorService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

    }

    private fun updateRecyclerView(){
        if(answersList.size == 100){
            answersList[99] =  ("TOO_MUCH_FOR_ME")
            binding.edtWords.isEnabled = false
            binding.btnSend.isEnabled = false
        }
        val listAdapter = WordsListAdapter(answersList)
//        Toast.makeText(applicationContext,isUserAnswer.toString(),Toast.LENGTH_SHORT).show()
        binding.recyclerViewWords.adapter = listAdapter
        binding.scrlWords.post { binding.scrlWords.fullScroll(View.FOCUS_DOWN) }
        listAdapter.notifyDataSetChanged()
    }


}