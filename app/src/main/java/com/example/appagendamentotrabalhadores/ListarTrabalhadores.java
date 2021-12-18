package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import modelo.Usuario;

public class ListarTrabalhadores extends AppCompatActivity {

    private Spinner trabalhoSpnr;
    private ListView listaTrabalhadoresList;
    private Button cancelaBtn;

    private ArrayList<Usuario> usuarios;
    private ItemListaUsuario adaptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_trabalhadores);

        getSupportActionBar().hide();

        trabalhoSpnr = (Spinner) findViewById(R.id.trabalhoSpnr);
        listaTrabalhadoresList = (ListView) findViewById(R.id.listaTrabalhadoresList);
        cancelaBtn = (Button) findViewById(R.id.cancelaBtn);

        //responsavel por chamar todos os eventos dos botoes
        listarTrabalhadoresEventos();

        carregaEventosLista();
    }

    private void listarTrabalhadoresEventos() {

        cancelaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void carregaEventosLista(){
        usuarios = new ArrayList<>();


        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuario";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        JSONArray usuariosJSON = resposta.getJSONArray("informacao");

                        for (int i = 0; i < usuariosJSON.length(); i++) {
                            JSONObject obj = usuariosJSON.getJSONObject(i);
                            Usuario temp = new Usuario(obj.getInt("id"), obj.getString("nome"),
                                    obj.getString("endereco"), new Date(obj.getLong("dataNasc")), obj.getString("email"),
                                    obj.getString("telefone"), obj.getString("cpf"),
                                    obj.getString("descricao"), obj.getString("senha"), (float) obj.getDouble("mediaAvaliacao"));
                            usuarios.add(temp);
                        }

                        adaptar = new ItemListaUsuario(getApplication(), usuarios);
                        listaTrabalhadoresList.setAdapter(adaptar);

                        listaTrabalhadoresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent trocaAct = new Intent(ListarTrabalhadores.this, AgendamentoTrabalho.class);

                                trocaAct.putExtra("id", usuarios.get(position).getId());

                                startActivity(trocaAct);
                            }
                        });

                    } else {
                        Toast.makeText(ListarTrabalhadores.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(ListarTrabalhadores.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarTrabalhadores.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "consultaexceto");
                parametros.put("id", GlobalVar.idUsuario + "");

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

}