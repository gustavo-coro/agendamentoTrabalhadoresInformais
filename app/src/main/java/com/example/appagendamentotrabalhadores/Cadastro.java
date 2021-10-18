package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Cadastro extends AppCompatActivity {

    private EditText nomeTxt;
    private EditText cpfTxt;
    private EditText emailTxt;
    private EditText celularTxt;
    private EditText senhaTxt;
    private EditText senhaConfirmTxt;
    private Button cdtBtn;
    private Button voltaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Link entre os atributos java e os componentes XML
        nomeTxt = (EditText) findViewById(R.id.nomeCadastroTxt);
        cpfTxt = (EditText) findViewById(R.id.cpfCadastroTxt);
        emailTxt = (EditText) findViewById(R.id.emailCadastroTxt);
        celularTxt =(EditText)  findViewById(R.id.celularCadastroTxt);
        senhaTxt = (EditText)  findViewById(R.id.senhaCadastroTxt);
        senhaConfirmTxt = (EditText) findViewById(R.id.senhaConfirmCadastroTxt);
        cdtBtn = (Button) findViewById(R.id.concluirCadastroBtn);
        voltaBtn = (Button) findViewById(R.id.voltarLoginBtn);

        //Início dos eventos
        event();
    }

    private void event(){

        cdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextAct = new Intent(Cadastro.this, MenuControle.class);

                startActivity(nextAct);
            }
        });

        voltaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextAct = new Intent(Cadastro.this, MainActivity.class);

                startActivity(nextAct);
            }
        });

    }

}