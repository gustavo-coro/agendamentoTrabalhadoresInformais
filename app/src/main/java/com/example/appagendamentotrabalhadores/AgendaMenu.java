package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AgendaMenu extends AppCompatActivity {

    private Button agendarBtn;
    private Button editAgendaBtn;
    private Button trabalhosConcluidBtn;
    private Button solicitBtn;
    private Button cancelarTrabBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_menu);

        getSupportActionBar().hide();

        //Link entre os atributos java e os componentes XML
        agendarBtn = (Button) findViewById(R.id.agendarBtn);
        editAgendaBtn = (Button) findViewById(R.id.editarAgendaBtn);
        trabalhosConcluidBtn = (Button) findViewById(R.id.trabalhosConcluidosBtn);
        solicitBtn = (Button) findViewById(R.id.solicitacoesBtn);
        cancelarTrabBtn = (Button) findViewById(R.id.cancelarTrabBtn);

        comecaEv();
    }

    private void comecaEv() {

        agendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextAct = new Intent(AgendaMenu.this, AgendamentoTrabalho.class);

                startActivity(nextAct);

            }
        });

        solicitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextAct = new Intent(AgendaMenu.this, ControlarPedidoTrabalho.class);

                startActivity(nextAct);

            }
        });

        editAgendaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextAct = new Intent(AgendaMenu.this, EditAgenda.class);

                startActivity(nextAct);

            }
        });

        trabalhosConcluidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextAct = new Intent(AgendaMenu.this, TrabalhosConcluidos.class);

                startActivity(nextAct);

            }
        });


    }
}