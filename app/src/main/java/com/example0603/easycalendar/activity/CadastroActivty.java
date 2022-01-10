package com.example0603.easycalendar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example0603.easycalendar.R;

public class CadastroActivty extends AppCompatActivity {

    private EditText nome,senha,senha2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_activty);

        nome = findViewById(R.id.editNomeUser);
    }

    public void cadastroEfetuado(View view){

        if(verificarDados()){ // entao os dados inseridos pelo usuario são validos
        }
    }

    public boolean verificarDados(){
        String nomeInserido = nome.getText().toString();

        if(nomeInserido.isEmpty()){
            Toast.makeText(getApplicationContext(),"Insira todos os dados requisitados",Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
}