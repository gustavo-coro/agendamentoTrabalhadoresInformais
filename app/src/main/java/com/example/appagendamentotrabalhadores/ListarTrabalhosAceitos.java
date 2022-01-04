package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarTrabalhosAceitos extends AppCompatActivity {

    private ListView aceitosList;
    private Button cancelaBtn;
    private Spinner trabalhadorSpnr;
    private LinearLayout exibicaoLL;

    //Lista usada para no ListView
    private ArrayList<String[]> resultadoList;
    private ItemListaSolicitacao adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_trabalhos_aceitos);

        getSupportActionBar().hide();

        aceitosList = (ListView) findViewById(R.id.listaAceitosList);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);
        trabalhadorSpnr = (Spinner) findViewById(R.id.trabalhadorSpnr);
        exibicaoLL = (LinearLayout) findViewById(R.id.opcoesExibicaoLL);
        View vazia = findViewById(R.id.listaVaziaTxt);
        aceitosList.setEmptyView(vazia);

        //eventos dos botoes
        trabalhosAceitosEventos();

        if (GlobalVar.usuarioIsTrabalhador == 1) {
            exibicaoLL.setVisibility(View.VISIBLE);
            configuraSpinner();
        } else {
            exibicaoLL.setVisibility(View.GONE);
            carregaEventosLista(0);
        }
    }

    private void trabalhosAceitosEventos() {
        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configuraSpinner() {
        List<String> listaItemSpinner = new ArrayList<>();

        listaItemSpinner.add("Solicitações Enviadas");
        listaItemSpinner.add("Solicitações Recebidas");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(ListarTrabalhosAceitos.this,
                android.R.layout.simple_spinner_item, listaItemSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        trabalhadorSpnr.setAdapter(adapterSpinner);

        trabalhadorSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carregaEventosLista(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void carregaEventosLista(int tipoBusca) {
        resultadoList = new ArrayList<>();

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        JSONArray servicosJSON = resposta.getJSONArray("informacao");
                        aceitosList.getEmptyView().setActivated(false);

                        for (int i = 0; i < servicosJSON.length(); i++) {
                            JSONObject obj = servicosJSON.getJSONObject(i);
                            Date dataHoraInicio = new Date(obj.getLong("data_hora_inicio"));
                            SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatadorHora = new SimpleDateFormat("HH:mm");

                            String temp[] = new String[6];
                            temp[0] = obj.getInt("id_contratacao") + "";
                            temp[1] = obj.getInt("aceito") + "";
                            temp[2] = formatadorData.format(dataHoraInicio);
                            temp[3] = formatadorHora.format(dataHoraInicio);
                            temp[4] = obj.getString("nome_usuario");
                            temp[5] = obj.getString("nome_servico");

                            resultadoList.add(temp);
                        }

                        adapter = new ItemListaSolicitacao(getApplication(), resultadoList);
                        aceitosList.setAdapter(adapter);

                        aceitosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent trocaAct = new Intent(ListarTrabalhosAceitos.this,
                                        TrabalhosAceitos.class);
                                String extra[] = resultadoList.get(position);
                                trocaAct.putExtra("id", extra[0]);
                                trocaAct.putExtra("tipoBusca", tipoBusca+"");
                                startActivity(trocaAct);
                                finish();
                            }
                        });

                    } else if (resposta.getInt("cod") == 204) {
                        adapter = new ItemListaSolicitacao(getApplication(), resultadoList);
                        aceitosList.setAdapter(adapter);
                        aceitosList.getEmptyView().setActivated(true);
                    } else {
                        adapter = new ItemListaSolicitacao(getApplication(), resultadoList);
                        aceitosList.setAdapter(adapter);
                        aceitosList.getEmptyView().setActivated(true);
                        Toast.makeText(ListarTrabalhosAceitos.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(ListarTrabalhosAceitos.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarTrabalhosAceitos.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultaaceita");
                if (tipoBusca == 0) {
                    parametros.put("idUsuario", GlobalVar.idUsuario+"");
                } else {
                    parametros.put("idTrabalhador", GlobalVar.idUsuario+"");
                }

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

}