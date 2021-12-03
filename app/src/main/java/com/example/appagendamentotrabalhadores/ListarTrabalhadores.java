package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ListarTrabalhadores extends AppCompatActivity {

    private Spinner trabalhoSpnr;
    private ListView listaTrabalhadoresList;
    private Button cancelaBtn;
    private Button confirmaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_trabalhadores);

        trabalhoSpnr = (Spinner) findViewById(R.id.trabalhoSpnr);
        listaTrabalhadoresList = (ListView) findViewById(R.id.listaTrabalhadoresList);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);
        confirmaBtn = (Button) findViewById(R.id.confirmaBtn);

        //responsavel por chamar todos os eventos dos botoes
        listarTrabalhadoresEventos();

    }

    private void listarTrabalhadoresEventos() {

        confirmaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent trocaAct = new Intent(ListarTrabalhadores.this, AgendamentoTrabalho.class);

                startActivity(trocaAct);
            }
        });

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(ListarTrabalhadores.this, MenuControle.class);

                startActivity(trocaAct);
            }
        });

    }

}