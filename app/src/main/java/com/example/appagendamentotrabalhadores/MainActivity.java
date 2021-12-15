package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ferramentas.UsuarioDb;
import modelo.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText telefoneTxt;
    private EditText senhaTxt;
    private Button entrarBtn;
    private Button cadastrarBtn;
    private Usuario usuarioLogin = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        telefoneTxt = (EditText) findViewById(R.id.telefoneTxt);
        senhaTxt = (EditText) findViewById(R.id.senhaTxt);
        entrarBtn = (Button) findViewById(R.id.entrarBtn);
        cadastrarBtn = (Button) findViewById(R.id.cadastrarBtn);

        mascaraTelefone(telefoneTxt);

        configuraPermissoes();
        //responsavel por chamar todos os eventos dos botoes
        loginEvento();

    }

    private void configuraPermissoes() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET}, 0);
        }
    }

    //criando mascara para o telefone
    private void mascaraTelefone(EditText tel) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(tel, smf);
        tel.addTextChangedListener(mtw);
    }

    private void loginEvento() {

        entrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmaLogin(telefoneTxt.getText().toString(), senhaTxt.getText().toString());
            }
        });

        cadastrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trocaAct = new Intent(MainActivity.this, Cadastro.class);
                trocaAct.putExtra("acao", 0);

                startActivity(trocaAct);

            }
        });

    }

    private void confirmaLogin(String tel, String senha) {
        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuario";

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        JSONObject obj = resposta.getJSONObject("informacao");

                        GlobalVar.idUsuario = obj.getInt("id");
                        GlobalVar.descricaoUsuarioLogin = obj.getString("descricao");
                        GlobalVar.nomeUsuarioLogin = obj.getString("nome");

                        if (GlobalVar.idUsuario < 0) {

                            Toast.makeText(MainActivity.this, "Telefone ou senha incorretos.", Toast.LENGTH_LONG).show();

                        } else {
                            Intent trocaAct = new Intent(MainActivity.this, MenuControle.class);

                            startActivity(trocaAct);
                            finish();
                        }

                    } else {
                        Toast.makeText(MainActivity.this,
                                resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(MainActivity.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "login");
                parametros.put("telefone", tel);
                parametros.put("senha", senha);

                return parametros;
            }
        };
        pilha.add(jsonRequest);
    }

    /*private void confirmaLogin() {

        usuarioLogin = new ArrayList<>();

        String telefone = telefoneTxt.getText().toString();
        String senha = senhaTxt.getText().toString();

        UsuarioDb db = new UsuarioDb(MainActivity.this);
        usuarioLogin = db.buscaUsuarioLogin(telefone, senha);


    }*/

}