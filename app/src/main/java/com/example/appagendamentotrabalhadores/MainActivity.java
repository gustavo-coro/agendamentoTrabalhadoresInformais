package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText telefoneTxt;
    private EditText senhaTxt;
    private Button entrarBtn;
    private Button cadastrarBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        telefoneTxt = (EditText) findViewById(R.id.telefoneTxt);
        senhaTxt = (EditText) findViewById(R.id.senhaTxt);
        entrarBtn = (Button) findViewById(R.id.entrarBtn);
        cadastrarBtn = (Button) findViewById(R.id.cadastrarBtn);

        //responsavel por chamar todos os eventos dos botoes
        loginEvento();


    }

    private void loginEvento(){

        entrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MainActivity.this, MenuControle.class);

                startActivity(trocaAct);
            }
        });

        cadastrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MainActivity.this, Cadastro.class);

                startActivity(trocaAct);

            }
        });

    }


}