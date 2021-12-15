package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class SelecionarTrabalho extends AppCompatActivity {

    private NavigationView listaTrabalhosNview;
    private Button confirmaBtn;
    private Button cancelaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_trabalho);

        listaTrabalhosNview = (NavigationView) findViewById(R.id.trabalhosNView);
        confirmaBtn = (Button) findViewById(R.id.confirmaBtn);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);

        //responsavel por chamar todos os eventos dos botoes
        eventos();
    }

    private void eventos() {

        confirmaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct;
                //verifica se o usuário fez login
                if(GlobalVar.idUsuario >= 0){
                    trocaAct = new Intent(SelecionarTrabalho.this, MenuControle.class);
                    startActivity(trocaAct);
                }else{
                    trocaAct = new Intent(SelecionarTrabalho.this, MainActivity.class);
                    startActivity(trocaAct);
                    finish();
                }
            }
        });

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct;
                //verifica se o usuário fez login
                if(GlobalVar.idUsuario >= 0){
                    trocaAct = new Intent(SelecionarTrabalho.this, MenuControle.class);
                    startActivity(trocaAct);
                }else{
                    trocaAct = new Intent(SelecionarTrabalho.this, MainActivity.class);
                    startActivity(trocaAct);
                    finish();
                }
            }
        });

    }


}