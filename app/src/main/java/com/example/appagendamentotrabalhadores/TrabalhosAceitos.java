package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrabalhosAceitos extends AppCompatActivity {

    private TextView usuarioTxt;
    private TextView trabalhadorTxt;
    private TextView servicotxt;
    private TextView precoTxt;
    private TextView enderecoTxt;
    private TextView dataTxt;
    private TextView horaTxt;
    private Button editarBtn;
    private Button voltarBtn;
    private Button finalizarBtn;

    private int tipoBusca = -1;
    private int idContratacao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabalhos_aceitos);

        getSupportActionBar().hide();

        usuarioTxt = (TextView) findViewById(R.id.nomeUsuarioTxt);
        trabalhadorTxt = (TextView) findViewById(R.id.nomeTrabalhadorTxt);
        servicotxt = (TextView) findViewById(R.id.tipoServicoTxt);
        precoTxt = (TextView) findViewById(R.id.precoServicoTxt);
        enderecoTxt = (TextView) findViewById(R.id.enderecoServicoTxt);
        dataTxt = (TextView) findViewById(R.id.dataTxt);
        horaTxt = (TextView) findViewById(R.id.horaInicioTxt);
        editarBtn = (Button) findViewById(R.id.editarBtn);
        voltarBtn = (Button) findViewById(R.id.voltarBtn);
        finalizarBtn = (Button) findViewById(R.id.finalizarBtn);

        Intent intencao = getIntent();

        try {
            tipoBusca = Integer.parseInt(intencao.getStringExtra("tipoBusca"));
            idContratacao = Integer.parseInt(intencao.getStringExtra("id"));
            if (tipoBusca == 0) {
                finalizarBtn.setVisibility(View.GONE);
            }

            buscaSolicitacao();

        } catch (NumberFormatException ex) {
            Toast.makeText(TrabalhosAceitos.this, "Erro no formato das ações",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        //eventos dos botões
        trabalhosAceitosEventos();
    }

    private void trabalhosAceitosEventos() {
        voltarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trocaAct = new Intent(TrabalhosAceitos.this,
                        AgendamentoTrabalho.class);
                trocaAct.putExtra("acao", "1");
                trocaAct.putExtra("idContratacao", idContratacao + "");
                startActivity(trocaAct);
                finish();
            }
        });

        finalizarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarServico();
            }
        });
    }

    private void buscaSolicitacao() {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        JSONObject obj = resposta.getJSONObject("informacao");

                        Date dataServico = new Date(obj.getLong("data_hora_inicio"));
                        SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat formatadorHora = new SimpleDateFormat("HH:mm");

                        usuarioTxt.setText(obj.getString("nome_usuario"));
                        trabalhadorTxt.setText(obj.getString("nome_trabalhador"));
                        servicotxt.setText(obj.getString("nome_servico"));
                        precoTxt.setText(String.format("%.2f",
                                obj.getDouble("preco")).replace(".", ","));
                        enderecoTxt.setText(obj.getString("endereco_servico"));
                        dataTxt.setText(formatadorData.format(dataServico));
                        horaTxt.setText(formatadorHora.format(dataServico));

                    } else {
                        Toast.makeText(TrabalhosAceitos.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(TrabalhosAceitos.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrabalhosAceitos.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultasolicitacaoid");
                parametros.put("id", idContratacao + "");

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void finalizarServico() {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        Toast.makeText(TrabalhosAceitos.this,
                                "Serviço finalizado com sucesso",
                                Toast.LENGTH_LONG).show();
                        Intent trocaAct = new Intent(TrabalhosAceitos.this,
                                MenuControle.class);
                        startActivity(trocaAct);
                        finish();

                    } else {
                        Toast.makeText(TrabalhosAceitos.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(TrabalhosAceitos.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TrabalhosAceitos.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                Calendar hoje = Calendar.getInstance();
                Date dataHora = new Date(hoje.getTime().getTime());
                SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                parametros.put("servico", "atualiza");
                parametros.put("id", idContratacao + "");
                parametros.put("horaFim", formatador.format(dataHora));

                return parametros;
            }
        };
        pilha.add(requisicao);
    }
}