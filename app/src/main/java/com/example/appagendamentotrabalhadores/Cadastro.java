package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ferramentas.UsuarioDb;
import modelo.Usuario;

public class Cadastro extends AppCompatActivity {

    private DatePickerDialog calendarioUsuario;
    private Calendar dataUsuario;
    private int idade;

    private TextInputLayout nomeTxt;
    private TextInputLayout dataNascTxt;
    private TextView opcaoTrabalhadorTxt;
    private TextInputLayout cpfTxt;
    private TextInputLayout enderecoTxt;
    private TextInputLayout emailTxt;
    private TextInputLayout celularTxt;
    private TextInputLayout descricaoTxt;
    private TextInputLayout senhaTxt;
    private TextInputLayout senhaConfirmTxt;
    private CheckBox usuarioCheck;
    private Button cdtBtn;
    private Button voltaBtn;

    //0 - cadastro, 1 - edicao
    private int operacao = -1;
    private Usuario usuarioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Link entre os atributos java e os componentes XML
        nomeTxt = (TextInputLayout) findViewById(R.id.nomeCadastroTxt);
        dataNascTxt = (TextInputLayout) findViewById(R.id.dataNascTxt);
        opcaoTrabalhadorTxt = (TextView) findViewById(R.id.opcaoTrabalhadorTxt);
        cpfTxt = (TextInputLayout) findViewById(R.id.cpfCadastroTxt);
        enderecoTxt = (TextInputLayout) findViewById(R.id.enderecoCadastroTxt);
        emailTxt = (TextInputLayout) findViewById(R.id.emailCadastroTxt);
        celularTxt = (TextInputLayout) findViewById(R.id.celularCadastroTxt);
        descricaoTxt = (TextInputLayout) findViewById(R.id.descricaoCadastroTxt);
        senhaTxt = (TextInputLayout) findViewById(R.id.senhaCadastroTxt);
        senhaConfirmTxt = (TextInputLayout) findViewById(R.id.senhaConfirmCadastroTxt);
        usuarioCheck = (CheckBox) findViewById(R.id.usuarioCheck);
        cdtBtn = (Button) findViewById(R.id.concluirCadastroBtn);
        voltaBtn = (Button) findViewById(R.id.voltarLoginBtn);

        //pega a data atual do dispositivo
        dataUsuario = Calendar.getInstance();

        //0 - cadastro, 1 - edição
        Intent intencao = getIntent();
        operacao = intencao.getIntExtra("acao", -1);
        ajustaOperacao();

        //aplicando as mascaras nos edittext
        mascaraTelefone(celularTxt);
        mascaraCpf(cpfTxt);

        //Início dos eventos dos botões
        event();
    }

    //Confere se o usuário quer se cadastrar ou editar suas informações
    private void ajustaOperacao() {
        if (operacao == 0) {
            getSupportActionBar().setTitle("Cadastrar");
            mostraData();
            nomeTxt.setEndIconVisible(false);
            cpfTxt.setEndIconVisible(false);
            enderecoTxt.setEndIconVisible(false);
            emailTxt.setEndIconVisible(false);
            celularTxt.setEndIconVisible(false);
            descricaoTxt.setEndIconVisible(false);
        } else if (operacao == 1) {
            //Mudando a visibilidade do icone, mostrando ao usuário que ele pode editar as informações
            dataNascTxt.setEndIconDrawable(R.drawable.ic_baseline_edit_calendar_24);
            getSupportActionBar().setTitle("Editar");
            cdtBtn.setText("Atualizar");
            voltaBtn.setText("Excluir");
            if(GlobalVar.usuarioIsTrabalhador == 1) {
                opcaoTrabalhadorTxt.setText("Quer começar a oferecer um novo trabalho?");
            } else {
                opcaoTrabalhadorTxt.setText("Quer começar a oferecer algum trabalho?");
            }

            if (GlobalVar.idUsuario != -1) {
                RequestQueue pilha = Volley.newRequestQueue(this);
                String url = GlobalVar.urlServidor + "usuario";
                StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resposta = new JSONObject(response);

                            if (resposta.getInt("cod") == 200) {
                                JSONObject obj = resposta.getJSONObject("informacao");
                                usuarioSelecionado = new Usuario(obj.getInt("id"), obj.getString("nome"),
                                        obj.getString("endereco"), new Date(obj.getLong("dataNasc")), obj.getString("email"),
                                        obj.getString("telefone"), obj.getString("cpf"),
                                        obj.getString("descricao"), obj.getString("senha"), 0);

                                nomeTxt.getEditText().setText(usuarioSelecionado.getNome());
                                cpfTxt.getEditText().setText(usuarioSelecionado.getCpf());
                                enderecoTxt.getEditText().setText(usuarioSelecionado.getEndereco());
                                emailTxt.getEditText().setText(usuarioSelecionado.getEmail());
                                celularTxt.getEditText().setText(usuarioSelecionado.getTelefone());
                                descricaoTxt.getEditText().setText(usuarioSelecionado.getDescricao());
                                senhaTxt.getEditText().setText(usuarioSelecionado.getSenha());
                                senhaConfirmTxt.getEditText().setText(usuarioSelecionado.getSenha());

                                dataUsuario.setTime(usuarioSelecionado.getDataNasc());
                                mostraData();
                                idade = calculaIdade(dataUsuario.get(Calendar.YEAR),
                                        dataUsuario.get(Calendar.MONTH), dataUsuario.get(Calendar.DAY_OF_MONTH));

                            } else {
                                Toast.makeText(Cadastro.this,
                                        resposta.getString("informacao"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException ex) {
                            Toast.makeText(Cadastro.this,
                                    "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Cadastro.this,
                                "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> parametros = new HashMap<>();

                        parametros.put("servico", "consulta");
                        parametros.put("id", GlobalVar.idUsuario + "");

                        return parametros;
                    }
                };
                pilha.add(jsonRequest);

            } else {
                Toast toast = Toast.makeText(Cadastro.this,
                        "Erro na identificação do usuário", Toast.LENGTH_LONG);
                toast.show();
            }

        } else {
            Toast toast = Toast.makeText(Cadastro.this,
                    "Erro na identificação da ação", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }

    //criando mascara para o telefone
    private void mascaraTelefone(TextInputLayout tel) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(tel.getEditText(), smf);
        tel.getEditText().addTextChangedListener(mtw);
    }

    //criando mascara para o cpf
    private void mascaraCpf(TextInputLayout cpf) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(cpf.getEditText(), smf);
        cpf.getEditText().addTextChangedListener(mtw);
    }

    //metodo usado para mostrar a data atual quando o usuario abre a tela
    private void mostraData() {

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat formatador = new SimpleDateFormat(pattern);
        dataNascTxt.getEditText().setText(formatador.format(dataUsuario.getTime()));

    }

    //metodo usado para calcular a idade do usuario
    //se ele for menor de 18, nao podera se cadastrar
    private static int calculaIdade(int ano, int mes, int dia) {
        Calendar hoje = Calendar.getInstance();
        Calendar dataN = Calendar.getInstance();
        dataN.set(mes, dia);
        int idade = hoje.get(Calendar.YEAR) - ano;
        if ((hoje.get(Calendar.MONTH) < mes) || (hoje.get(Calendar.MONTH) == mes && hoje.get(Calendar.DAY_OF_MONTH) < dia)) {
            idade--;
        }
        return idade;
    }

    private void event() {
        //metodo usado na data de nascimento
        calendarioUsuario = new DatePickerDialog(Cadastro.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                idade = calculaIdade(ano, mes, dia);
                if (idade < 18) {

                    dataUsuario = Calendar.getInstance();
                    Toast toast = Toast.makeText(Cadastro.this, "Você precisa ser maior de 18 para usar o app.", Toast.LENGTH_LONG);
                    toast.show();
                    mostraData();

                } else {
                    dataUsuario.set(ano, mes, dia);
                    String pattern = "dd/MM/yyyy";
                    SimpleDateFormat formatador = new SimpleDateFormat(pattern);
                    dataNascTxt.getEditText().setText(formatador.format(dataUsuario.getTime()));
                }

            }
        }, dataUsuario.get(Calendar.YEAR), dataUsuario.get(Calendar.MONTH), dataUsuario.get(Calendar.DAY_OF_MONTH));

        cdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmaPreenchimento() == true) {
                    cadastraEditaUsuario();
                }
            }
        });

        voltaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operacao == 0) {
                    //eventos do cadastro
                    Intent nextAct = new Intent(Cadastro.this, MainActivity.class);
                    startActivity(nextAct);
                    finish();
                } else if (operacao == 1) {
                    //eventos de edição

                    AlertDialog.Builder confirmarExclusao = new AlertDialog.Builder(Cadastro.this);
                    confirmarExclusao.setTitle("Atencão!");
                    confirmarExclusao.setMessage("Tem certeza que deseja excluir o registro?");
                    confirmarExclusao.setCancelable(false);
                    confirmarExclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            exclusaoUsuario();
                        }
                    });
                    confirmarExclusao.setNegativeButton("Não", null);
                    confirmarExclusao.setIcon(android.R.drawable.ic_dialog_alert);
                    confirmarExclusao.show();
                }
            }
        });

        dataNascTxt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarioUsuario.show();
            }
        });

    }

    //Checa se o usuário preencheu todos os campos
    private boolean confirmaPreenchimento() {
        boolean conf = true;

        if (nomeTxt.getEditText().getText().toString().isEmpty()) {
            nomeTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else {
            nomeTxt.setError(null);
        }
        if (celularTxt.getEditText().getText().toString().isEmpty()) {
            celularTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else if (celularTxt.getEditText().getText().toString().length() < 15) {
            celularTxt.setError("Preencha o campo corretamente");
            conf = false;
        } else {
            celularTxt.setError(null);
        }
        if (senhaTxt.getEditText().getText().toString().isEmpty()) {
            senhaTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else {
            senhaTxt.setError(null);
        }
        if (senhaConfirmTxt.getEditText().getText().toString().isEmpty()) {
            senhaConfirmTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else {
            senhaConfirmTxt.setError(null);
        }
        if (descricaoTxt.getEditText().getText().toString().isEmpty()) {
            descricaoTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else {
            descricaoTxt.setError(null);
        }
        if (cpfTxt.getEditText().getText().toString().isEmpty()) {
            cpfTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else if (cpfTxt.getEditText().getText().toString().length() < 14) {
            cpfTxt.setError("Preencha o campo corretamente");
            conf = false;
        } else {
            cpfTxt.setError(null);
        }
        if (emailTxt.getEditText().getText().toString().isEmpty()) {
            emailTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else {
            emailTxt.setError(null);
        }
        if (enderecoTxt.getEditText().getText().toString().isEmpty()) {
            enderecoTxt.setError("Campo não pode estar vazio");
            conf = false;
        } else {
            enderecoTxt.setError(null);
        }

        return conf;
    }

    private void cadastraEditaUsuario() {

        if (idade < 18) {

            dataUsuario = Calendar.getInstance();
            Toast toast = Toast.makeText(Cadastro.this, "Selecione uma data de nascimento válida.", Toast.LENGTH_LONG);
            toast.show();
            mostraData();

        } else {

            String senha = senhaTxt.getEditText().getText().toString();
            String confirmaSenha = senhaConfirmTxt.getEditText().getText().toString();
            if (senha.equals(confirmaSenha)) {
                senhaConfirmTxt.setError(null);

                String nome = nomeTxt.getEditText().getText().toString();
                String endereco = enderecoTxt.getEditText().getText().toString();
                String email = emailTxt.getEditText().getText().toString();
                String telefone = celularTxt.getEditText().getText().toString();
                String cpf = cpfTxt.getEditText().getText().toString();
                String descricao = descricaoTxt.getEditText().getText().toString();
                float media;
                if (operacao == 1) {
                    media = GlobalVar.usuarioLogin.getMediaAvaliacao();
                } else {
                    media = 0;
                }

                //trabalhando com a data de nascimento do usuario
                String nascimentoStr = dataNascTxt.getEditText().getText().toString();
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);

                try {
                    Date diaNasc = formatador.parse(nascimentoStr);

                    Usuario novoUsuario = new Usuario(nome, endereco, diaNasc, email, telefone, cpf, descricao, senha, media);

                    comparaExistenciaDados(novoUsuario);

                } catch (ParseException ex) {
                    System.err.println("Erro no formato da data...");
                }

            } else {
                senhaConfirmTxt.setError("As senhas não combinam");
            }

        }

    }

    private void comparaExistenciaDados(Usuario novo) {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuario";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {

                        if (operacao == 0) {
                            //Cadastro de um novo usuário
                            requestCadastroUsuario(novo);
                        } else if (operacao == 1) {
                            //Edição de um usuário já existente
                            requestUpdateUsuario(novo);
                        } else {
                            Toast.makeText(Cadastro.this, "Erro na identificação da ação",
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(Cadastro.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(Cadastro.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.", Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cadastro.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "compara");
                parametros.put("id", GlobalVar.idUsuario + "");
                parametros.put("nome", novo.getNome());
                parametros.put("email", novo.getEmail());
                parametros.put("telefone", novo.getTelefone());
                parametros.put("cpf", novo.getCpf());

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void requestCadastroUsuario(Usuario novo) {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuario";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(Cadastro.this,
                                "Cadastro feito com sucesso!",
                                Toast.LENGTH_LONG).show();

                        Intent trocaAct;

                        if (usuarioCheck.isChecked()) {
                            int idCadastro = resposta.getInt("informacao");
                            trocaAct = new Intent(Cadastro.this, SelecionarTrabalho.class);
                            trocaAct.putExtra("idUsuario", idCadastro);
                            startActivity(trocaAct);
                            finish();
                        } else {
                            trocaAct = new Intent(Cadastro.this, MainActivity.class);
                            startActivity(trocaAct);
                            finish();
                        }

                    } else {
                        Toast.makeText(Cadastro.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(Cadastro.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cadastro.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "cadastro");
                parametros.put("nome", novo.getNome());
                parametros.put("endereco", novo.getEndereco());
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);
                parametros.put("dataNasc", formatador.format(novo.getDataNasc()));
                parametros.put("mediaAvaliacao", novo.getMediaAvaliacao() + "");
                parametros.put("email", novo.getEmail());
                parametros.put("telefone", novo.getTelefone());
                parametros.put("cpf", novo.getCpf());
                parametros.put("descricao", novo.getDescricao());
                parametros.put("senha", novo.getSenha());

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void requestUpdateUsuario(Usuario novo) {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuario";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(Cadastro.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();

                        GlobalVar.usuarioLogin = novo;
                        GlobalVar.usuarioLogin.setId(GlobalVar.idUsuario);

                        Intent trocaAct;

                        if (usuarioCheck.isChecked()) {
                            trocaAct = new Intent(Cadastro.this, SelecionarTrabalho.class);
                            startActivity(trocaAct);
                            finish();
                        } else {
                            trocaAct = new Intent(Cadastro.this, MenuControle.class);
                            startActivity(trocaAct);
                            finish();
                        }

                    } else {
                        Toast.makeText(Cadastro.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(Cadastro.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cadastro.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "atualiza");
                parametros.put("id", GlobalVar.idUsuario + "");
                parametros.put("nome", novo.getNome());
                parametros.put("endereco", novo.getEndereco());
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);
                parametros.put("dataNasc", formatador.format(novo.getDataNasc()));
                parametros.put("email", novo.getEmail());
                parametros.put("telefone", novo.getTelefone());
                parametros.put("cpf", novo.getCpf());
                parametros.put("descricao", novo.getDescricao());
                parametros.put("senha", novo.getSenha());

                return parametros;
            }
        };
        pilha.add(requisicao);
    }

    private void exclusaoUsuario() {

        RequestQueue pilha = Volley.newRequestQueue(this);
        String url = GlobalVar.urlServidor + "usuario";

        StringRequest requisicao = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);

                    if (resposta.getInt("cod") == 200) {
                        Toast.makeText(Cadastro.this,
                                "Deleção foi um sucesso",
                                Toast.LENGTH_LONG).show();

                        GlobalVar.idUsuario = -1;
                        Intent trocaAct = new Intent(Cadastro.this, MainActivity.class);
                        startActivity(trocaAct);
                        finish();

                    } else {
                        Toast.makeText(Cadastro.this, resposta.getString("informacao"),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException ex) {
                    Toast.makeText(Cadastro.this,
                            "Erro no formato de rotorno do servidor. Contate a equipe de desenvolvimento.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cadastro.this,
                        "Erro! Verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("servico", "desativa");
                parametros.put("id", GlobalVar.idUsuario + "");

                return parametros;
            }
        };
        pilha.add(requisicao);
    }
}