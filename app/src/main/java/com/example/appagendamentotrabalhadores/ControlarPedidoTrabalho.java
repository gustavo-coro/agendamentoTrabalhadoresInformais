package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ControlarPedidoTrabalho extends AppCompatActivity {


    private TextView usuarioTxt;
    private TextView tipoServicoTxt;
    private TextView dataTxt;
    private TextView horaTxt;
    private Button recusarBtn;
    private Button aceitarBtn;
    private Button cancelarBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlar_pedido_trabalho);

        usuarioTxt = (TextView) findViewById(R.id.nomeUsuarioTxt);
        tipoServicoTxt = (TextView) findViewById(R.id.tipoServicoTxt);
        dataTxt = (TextView) findViewById(R.id.dataTxt);
        horaTxt = (TextView) findViewById(R.id.horaInicioTxt);
        recusarBtn = (Button) findViewById(R.id.recusarBtn);
        aceitarBtn = (Button) findViewById(R.id.aceitarBtn);
        cancelarBtn = (Button) findViewById(R.id.cancelarBtn);

        //responsavel por chamar todos os eventos dos botoes
        controlaPedidoEvento();


    }

    private void controlaPedidoEvento() {


        recusarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(ControlarPedidoTrabalho.this, MenuControle.class);

                startActivity(trocaAct);
            }
        });

        aceitarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(ControlarPedidoTrabalho.this, MenuControle.class);

                startActivity(trocaAct);
            }
        });

        cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(ControlarPedidoTrabalho.this, MenuControle.class);

                startActivity(trocaAct);
            }
        });

    }


}