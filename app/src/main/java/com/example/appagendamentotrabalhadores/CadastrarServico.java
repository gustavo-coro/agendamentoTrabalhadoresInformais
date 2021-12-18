package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CadastrarServico extends AppCompatActivity {

    private TextView nomeTipoServicoTxt;
    private EditText nomeServicoTxt;
    private EditText precoTxt;
    private Button cadastraBtn;
    private Button cancelaBtn;

    //0 - cadastro, 1 - ediçao
    private int operacao = -1;
    private String nomeTipoOperacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_servico);

        getSupportActionBar().hide();

        nomeTipoServicoTxt = (TextView) findViewById(R.id.tipoServicoSelectTxt);
        nomeServicoTxt = (EditText) findViewById(R.id.nomeServicoTxt);
        precoTxt = (EditText) findViewById(R.id.precoServicoTxt);
        cadastraBtn = (Button) findViewById(R.id.cadastraServicoBtn);
        cancelaBtn = (Button) findViewById(R.id.cancelaCadastroServicoBtn);

        //0 - cadastro, 1 - edição
        Intent intencao = getIntent();
        operacao = intencao.getIntExtra("acao", -1);
        if (operacao == 0) {
            nomeTipoOperacao = intencao.getStringExtra("nome");
        }
        ajustaOperacao();

        cadastroEventos();

    }

    private void ajustaOperacao() {
        if (operacao == 0) {
            //eventos de cadastro
            nomeTipoServicoTxt.setText(nomeTipoOperacao);
        } else if (operacao == 1) {
            //eventos de ediçao
        } else {
            //erro
            Toast toast = Toast.makeText(CadastrarServico.this,
                    "Erro na identificação da ação", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void cadastroEventos() {

        cadastraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}