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
import android.widget.TextView;
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

public class ListarTrabalhosConcluidos extends AppCompatActivity implements CustomRatePickerDialog.CustomDialogListener {

    private Button voltarBtn;
    private ListView trabalhosList;
    private LinearLayout exibicaoLL;
    private Spinner trabalhadorSpnr;
    private TextView selecionaAvaliacaoTxt;

    //Lista usada no ListView
    private ArrayList<String[]> resultadoList;
    private ItemListaConcluido adapter;

    //Lista usada no Spinner
    private List<String> listaItemSpinner;

    //variavel usada na selecao de nota
    private String idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_trabalhos_concluidos);

        getSupportActionBar().setTitle("Trabalhos Concluídos");

        trabalhosList = (ListView) findViewById(R.id.trabalhosList);
        voltarBtn = (Button) findViewById(R.id.voltarBtn);
        exibicaoLL = (LinearLayout) findViewById(R.id.opcoesExibicaoLL);
        trabalhadorSpnr = (Spinner) findViewById(R.id.trabalhadorSpnr);
        selecionaAvaliacaoTxt = (TextView) findViewById(R.id.selecionaAvaliacaoTxt);
        View vazia = findViewById(R.id.listaVaziaTxt);
        trabalhosList.setEmptyView(vazia);

        //responsavel por chamar todos os eventos dos botoes
        menuEvento();

        if (GlobalVar.usuarioIsTrabalhador == 1) {
            exibicaoLL.setVisibility(View.VISIBLE);
            configuraSpinner();
        } else {
            exibicaoLL.setVisibility(View.GONE);
            buscarElementosLista(0);
        }
    }

    private void menuEvento() {

        voltarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configuraSpinner() {
        listaItemSpinner = new ArrayList<>();

        listaItemSpinner.add("Serviços Enviados");
        listaItemSpinner.add("Serviços Recebidos");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(ListarTrabalhosConcluidos.this,
                android.R.layout.simple_spinner_item, listaItemSpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        trabalhadorSpnr.setAdapter(adapterSpinner);

        trabalhadorSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buscarElementosLista(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void buscarElementosLista(int tipoBusca) {
        if (tipoBusca == 1) {
            selecionaAvaliacaoTxt.setVisibility(View.GONE);
        } else {
            selecionaAvaliacaoTxt.setVisibility(View.VISIBLE);
        }
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
                        trabalhosList.getEmptyView().setActivated(false);

                        for (int i = 0; i < servicosJSON.length(); i++) {
                            JSONObject obj = servicosJSON.getJSONObject(i);
                            Date dataHoraInicio = new Date(obj.getLong("data_hora_inicio"));
                            Date dataHoraFim = new Date(obj.getLong("data_hora_fim"));
                            SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat formatadorHora = new SimpleDateFormat("HH:mm");

                            String temp[] = new String[8];
                            temp[0] = obj.getString("nome_usuario");
                            temp[1] = obj.getString("nome_servico");
                            temp[2] = formatadorData.format(dataHoraInicio);
                            temp[3] = formatadorHora.format(dataHoraInicio);
                            temp[4] = formatadorData.format(dataHoraFim);
                            temp[5] = formatadorHora.format(dataHoraFim);
                            temp[6] = obj.getInt("id_contratacao") + "";
                            temp[7] = obj.getDouble("nota_trabalhador") + "";

                            resultadoList.add(temp);
                        }

                        adapter = new ItemListaConcluido(getApplication(), resultadoList);
                        trabalhosList.setAdapter(adapter);

                        trabalhosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (tipoBusca == 0) {
                                    String selecionado[] = resultadoList.get(position);

                                    try {
                                        float nota = Float.parseFloat(selecionado[7]);
                                        if (nota == -1) {
                                            idSelecionado = selecionado[6];
                                            abrirDialog();
                                        } else {
                                            Toast.makeText(ListarTrabalhosConcluidos.this,
                                                    "Nota já selecionada",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (NumberFormatException ex) {
                                        Toast.makeText(ListarTrabalhosConcluidos.this,
                                                "Erro no formato numérico",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            }
                        });

                    } else if (resposta.getInt("cod") == 204) {
                        adapter = new ItemListaConcluido(getApplication(), resultadoList);
                        trabalhosList.setAdapter(adapter);
                        trabalhosList.getEmptyView().setActivated(true);
                    } else {
                        adapter = new ItemListaConcluido(getApplication(), resultadoList);
                        trabalhosList.setAdapter(adapter);
                        trabalhosList.getEmptyView().setActivated(true);
                        Toast.makeText(ListarTrabalhosConcluidos.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(ListarTrabalhosConcluidos.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarTrabalhosConcluidos.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultaconcluida");
                if (tipoBusca == 0) {
                    parametros.put("idUsuario", GlobalVar.idUsuario + "");
                } else {
                    parametros.put("idTrabalhador", GlobalVar.idUsuario + "");
                }

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void avaliaTrabalhador(float nota) {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuariocontrataservico";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        Toast.makeText(ListarTrabalhosConcluidos.this,
                                "Avaliação enviada com sucesso",
                                Toast.LENGTH_LONG).show();

                        buscarElementosLista(0);

                    } else {
                        Toast.makeText(ListarTrabalhosConcluidos.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ListarTrabalhosConcluidos.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarTrabalhosConcluidos.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "atualiza");
                parametros.put("id", idSelecionado);
                parametros.put("notaTrabalhador", nota + "");

                return parametros;
            }
        };
        pilha.add(requisicao);

    }

    private void abrirDialog() {
        CustomRatePickerDialog customDialog = new CustomRatePickerDialog();
        customDialog.show(getSupportFragmentManager(), "custom dialog");
    }

    @Override
    public void applyFloat(float nota) {
        avaliaTrabalhador(nota);
    }
}