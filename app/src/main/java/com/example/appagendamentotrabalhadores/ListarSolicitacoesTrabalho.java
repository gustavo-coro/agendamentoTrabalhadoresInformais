package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
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

public class ListarSolicitacoesTrabalho extends AppCompatActivity {

    private LinearLayout opcoesExibicaoLL;
    private Spinner trabalhadorSpnr;
    private ListView solicitacoesList;
    private Button cancelarBtn;

    //Lista usada para no ListView
    private ArrayList<String[]> resultadoList;
    private ItemListaSolicitacao adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_solicitacoes_trabalho);

        getSupportActionBar().hide();

        opcoesExibicaoLL = (LinearLayout) findViewById(R.id.opcoesExibicaoLL);
        trabalhadorSpnr = (Spinner) findViewById(R.id.trabalhadorSpnr);
        solicitacoesList = (ListView) findViewById(R.id.listaSolicitacoesList);
        cancelarBtn = (Button) findViewById(R.id.cancelaBtn);
        View vazia = findViewById(R.id.listaVaziaTxt);
        solicitacoesList.setEmptyView(vazia);

        //eventos dos botoes
        listarSolicitacoesTrabalhoEventos();

        configuraSpinner();

    }

    private void listarSolicitacoesTrabalhoEventos() {
        cancelarBtn.setOnClickListener(new View.OnClickListener() {
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

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(ListarSolicitacoesTrabalho.this,
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
                        solicitacoesList.getEmptyView().setActivated(false);

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
                        solicitacoesList.setAdapter(adapter);

                    } else if (resposta.getInt("cod") == 204) {
                        adapter = new ItemListaSolicitacao(getApplication(), resultadoList);
                        solicitacoesList.setAdapter(adapter);
                        solicitacoesList.getEmptyView().setActivated(true);
                    } else {
                        adapter = new ItemListaSolicitacao(getApplication(), resultadoList);
                        solicitacoesList.setAdapter(adapter);
                        solicitacoesList.getEmptyView().setActivated(true);
                        Toast.makeText(ListarSolicitacoesTrabalho.this,
                                resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(ListarSolicitacoesTrabalho.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarSolicitacoesTrabalho.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultasolicitacao");
                if (tipoBusca == 0) {
                    parametros.put("idUsuario", GlobalVar.idUsuario+"");
                } else {
                    parametros.put("idTrabalhador", GlobalVar.idUsuario+"");
                }
                Date dataAtual = Calendar.getInstance().getTime();
                SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                parametros.put("dataAtual", formatador.format(dataAtual));

                return parametros;
            }
        };
        pilha.add(requisicao);
    }
}