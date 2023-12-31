package br.com.omniatechnology.verificaresultado

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var numbersWins = ""
    private var totalNumbersForWins: Int = 0
    private var value = ""
    private lateinit var txtResult: TextView
    private lateinit var btnStart: Button
    private lateinit var btnSaveToFile: Button
    private var results: List<Result> = mutableListOf()
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        btnStart = findViewById(R.id.btnIniciar)
        btnSaveToFile = findViewById(R.id.btnSaveToFile)
        txtResult = findViewById(R.id.txtResultado)
        btnStart.setOnClickListener(this)
        btnSaveToFile.setOnClickListener(this)
        val fab = findViewById<FloatingActionButton>(R.id.fabRenew)
        fab.setOnClickListener(this)
    }

    private fun refactorLayout(svValue: Float, llValue: Float) {
        val ll = findViewById<LinearLayout>(R.id.llButton)
        val sv = findViewById<ScrollView>(R.id.svText)
        val paramSV = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            svValue
        )
        sv.layoutParams = paramSV
        val paramLL = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            llValue
        )
        ll.layoutParams = paramLL
    }

    private fun showDialog(title: String, id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
            value = input.text.toString().trim { it <= ' ' }
            if (value.isEmpty()) {
                Toast.makeText(applicationContext, "Valor não pode ser vazio", Toast.LENGTH_LONG)
                    .show()
                return@OnClickListener
            }
            when (id) {
                R.id.btnIniciar -> {
                    numbersWins = value
                    showDialog("Acerto mínimo para premiar: Ex(Mega = 4, LotoFácil = 11)", 2)
                }

                2 -> {
                    totalNumbersForWins = Integer.valueOf(value)
                    val toast = Toast.makeText(
                        applicationContext,
                        "Selecione o arquivo txt com os jogos",
                        Toast.LENGTH_LONG
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "text/plain"
                    startActivityForResult(intent, READ_REQUEST_CODE)
                }
            }
            dialog.cancel()
        })
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri?): String {
        val inputStream = contentResolver.openInputStream(uri!!)
        val reader = BufferedReader(
            InputStreamReader(
                inputStream
            )
        )
        results = CheckLottery.checkLottery(reader, numbersWins, totalNumbersForWins, this)
        inputStream!!.close()
        val retorno = CheckLottery.createDataWithResults(results)
        txtResult.text = retorno
        btnStart.visibility = View.GONE
        btnSaveToFile.visibility = View.VISIBLE
        refactorLayout(9f, 1f)
        return retorno
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri: Uri?
            if (data != null) {
                uri = data.data
                try {
                    readTextFromUri(uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == WRITE_REQUEST_CODE && resultCode == RESULT_OK) {
            var uri: Uri? = null
            if (data != null) {
                uri = data.data
            }
            try {
                val pfd = this.contentResolver.openFileDescriptor(uri!!, "w")
                val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
                fileOutputStream.write(txtResult.text.toString().toByteArray())
                // Let the document provider know you're done by closing the stream.
                fileOutputStream.close()
                pfd.close()
                val toast = Toast.makeText(
                    applicationContext,
                    "Arquivo salvo com Sucesso",
                    Toast.LENGTH_LONG
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_sair) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        val id = v.id
        value = ""
        when (id) {
            R.id.btnIniciar -> showDialog("Digite os números Sorteados separados por vírgulas", id)
            R.id.fabRenew -> {
                txtResult.text = ""
                btnSaveToFile.visibility = View.GONE
                btnStart.visibility = View.VISIBLE
                refactorLayout(4f, 6f)
            }

            R.id.btnSaveToFile -> createFile("text/plain", "resultados.txt")
        }
    }

    private fun createFile(mimeType: String, fileName: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // Create a file with the requested MIME type.
        intent.type = mimeType
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        startActivityForResult(intent, WRITE_REQUEST_CODE)
    }

    companion object {
        private const val READ_REQUEST_CODE = 42
        private const val WRITE_REQUEST_CODE = 43
    }
}