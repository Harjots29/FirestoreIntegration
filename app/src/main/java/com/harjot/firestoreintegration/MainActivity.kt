package com.harjot.firestoreintegration

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.harjot.firestoreintegration.databinding.ActivityMainBinding
import com.harjot.firestoreintegration.databinding.DialogLayoutBinding

class MainActivity : AppCompatActivity(), RecyclerInterface {
    lateinit var binding: ActivityMainBinding
    var arrayList = ArrayList<Model>()
    var recyclerAdapater = RecyclerAdapater(arrayList,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)


        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = recyclerAdapater
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabAdd.setOnClickListener {
            dialog()
        }

    }

    override fun onListClick(position: Int) {
        val intent = Intent(this,DetailsActivity::class.java)
        startActivity(intent)
    }

    override fun onEditClick(position: Int) {
        dialog(position)
    }

    override fun onDeleteClick(position: Int) {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete Item")
        alertDialog.setMessage("Do you want to delete the item?")
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("No") { _, _ ->
            alertDialog.setCancelable(true)
        }
        alertDialog.setPositiveButton("Yes") { _, _ ->
            if (arrayList.size == 0){
                Toast.makeText(this, "List Is Empty", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(
                    this,
                    "The item is  deleted",
                    Toast.LENGTH_SHORT
                ).show()
                arrayList.removeAt(position)
                recyclerAdapater.notifyDataSetChanged()
            }
        }
        alertDialog.show()
    }
    fun dialog(position: Int = -1){
        var dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
        var dialog = Dialog(this).apply {
            setContentView(dialogBinding.root)
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            if (position > -1){
                dialogBinding.btnAdd.setText("Update")
                dialogBinding.etName.setText(arrayList[position].name)
                dialogBinding.etPhoneNo.setText(arrayList[position].phoneNo.toString())
                dialogBinding.etEmail.setText(arrayList[position].email)

            }else{
                dialogBinding.btnAdd.setText("Add")
            }
            dialogBinding.btnAdd.setOnClickListener {
                Toast.makeText(this@MainActivity, "in add click", Toast.LENGTH_SHORT).show()
                if (dialogBinding.etName.text.toString().trim().isNullOrEmpty()){
                    dialogBinding.etName.error = "Enter Name"
                }else  if (dialogBinding.etEmail.text.toString().trim().isNullOrEmpty()){
                    dialogBinding.etEmail.error = "Enter Email"
                }else  if (dialogBinding.etPhoneNo.text.toString().trim().isNullOrEmpty()){
                    dialogBinding.etPhoneNo.error = "Enter PhoneNo"
                }else{
                    if (position > -1){
                        arrayList[position] = Model(
                            "",
                            name =  dialogBinding.etName.text.toString(),
                            email = dialogBinding.etEmail.text.toString(),
                            phoneNo = dialogBinding.etPhoneNo.text.toString(),
                        )
                    }else{
                        Log.e(TAG, "dialog: fabbutton", )
                        arrayList.add(Model(
                            "",
                            name = dialogBinding.etName.text.toString(),
                            email = dialogBinding.etEmail.text.toString(),
                            phoneNo = dialogBinding.etPhoneNo.text.toString())
                        )
                    }
                    dismiss()
                    recyclerAdapater.notifyDataSetChanged()
                }
            }
            show()
        }
    }
}