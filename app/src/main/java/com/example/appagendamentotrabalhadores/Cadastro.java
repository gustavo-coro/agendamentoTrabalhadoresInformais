package com.example.appagendamentotrabalhadores;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import ferramentas.UsuarioDb;
import modelo.Usuario;

public class Cadastro extends AppCompatActivity {

    private DatePickerDialog calendarioUsuario;
    private Calendar dataUsuario;
    private int idade;
    private EditText nomeTxt;
    private TextView dataNascTxt;
    private EditText cpfTxt;
    private EditText enderecoTxt;
    private EditText emailTxt;
    private EditText celularTxt;
    private EditText senhaTxt;
    private EditText senhaConfirmTxt;
    private CheckBox usuarioCheck;
    private Button cdtBtn;
    private Button voltaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Link entre os atributos java e os componentes XML
        nomeTxt = (EditText) findViewById(R.id.nomeCadastroTxt);
        dataNascTxt = (TextView) findViewById(R.id.dataNascTxt);
        cpfTxt = (EditText) findViewById(R.id.cpfCadastroTxt);
        enderecoTxt = (EditText) findViewById(R.id.enderecoCadastroTxt);
        emailTxt = (EditText) findViewById(R.id.emailCadastroTxt);
        celularTxt = (EditText) findViewById(R.id.celularCadastroTxt);
        senhaTxt = (EditText) findViewById(R.id.senhaCadastroTxt);
        senhaConfirmTxt = (EditText) findViewById(R.id.senhaConfirmCadastroTxt);
        usuarioCheck = (CheckBox) findViewById(R.id.usuarioCheck);
        cdtBtn = (Button) findViewById(R.id.concluirCadastroBtn);
        voltaBtn = (Button) findViewById(R.id.voltarLoginBtn);

        //pega a data atual do dispositivo
        dataUsuario = Calendar.getInstance();
        mostraData();

        //aplicando as mascaras nos edittext
        mascaraTelefone(celularTxt);
        mascaraCpf(cpfTxt);

        //Início dos eventos
        event();
    }

    //criando mascara para o telefone
    private void mascaraTelefone(EditText tel) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(tel, smf);
        tel.addTextChangedListener(mtw);
    }

    //criando mascara para o cpf
    private void mascaraCpf(EditText cpf) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(cpf, smf);
        cpf.addTextChangedListener(mtw);
    }

    //metodo usado para mostrar a data atual quando o usuario abre a tela
    private void mostraData() {

        String pattern = "dd/MM/yyyy";
        SimpleDateFormat formatador = new SimpleDateFormat(pattern);
        dataNascTxt.setText(formatador.format(dataUsuario.getTime()));

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
                    Toast toast = Toast.makeText(Cadastro.this, "Você precisa ser maior de 18 para se cadastrar.", Toast.LENGTH_LONG);
                    toast.show();
                    mostraData();

                } else {
                    dataUsuario.set(ano, mes, dia);
                    String pattern = "dd/MM/yyyy";
                    SimpleDateFormat formatador = new SimpleDateFormat(pattern);
                    dataNascTxt.setText(formatador.format(dataUsuario.getTime()));
                }

            }
        }, dataUsuario.get(Calendar.YEAR), dataUsuario.get(Calendar.MONTH), dataUsuario.get(Calendar.DAY_OF_MONTH));

        cdtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean confirma = confirmaPreenchimento();
                if (confirma == true) {
                    cadastrarUsuario();
                } else {
                    Toast toast = Toast.makeText(Cadastro.this, "Um ou mais campos não foram preenchidos.", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

        voltaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextAct = new Intent(Cadastro.this, MainActivity.class);
                startActivity(nextAct);
            }
        });

        dataNascTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarioUsuario.show();
            }
        });

    }

    private boolean confirmaPreenchimento() {
        boolean conf = true;

        if (nomeTxt.getText().toString().isEmpty()) {
            conf = false;
        } else if (celularTxt.getText().toString().isEmpty()) {
            conf = false;
        } else if (senhaTxt.getText().toString().isEmpty()) {
            conf = false;
        } else if (cpfTxt.getText().toString().isEmpty()) {
            conf = false;
        } else if (emailTxt.getText().toString().isEmpty()) {
            conf = false;
        } else if (enderecoTxt.getText().toString().isEmpty()) {
            conf = false;
        }

        return conf;
    }

    private void cadastrarUsuario() {

        if (idade < 18) {

            dataUsuario = Calendar.getInstance();
            Toast toast = Toast.makeText(Cadastro.this, "Selecione uma data de nascimento válida.", Toast.LENGTH_LONG);
            toast.show();
            mostraData();

        } else {

            String senha = senhaTxt.getText().toString();
            String confirmaSenha = senhaConfirmTxt.getText().toString();
            if (senha.equals(confirmaSenha)) {

                String nome = nomeTxt.getText().toString();
                String endereco = enderecoTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String telefone = celularTxt.getText().toString();
                String cpf = cpfTxt.getText().toString();
                //futuramente o usuario devera escrever uma descricao sobre si
                String descricao = null;

                //trabalhando com a data de nascimento do usuario
                String nascimentoStr = dataNascTxt.getText().toString();
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat formatador = new SimpleDateFormat(pattern);

                try {
                    Date diaNasc = formatador.parse(nascimentoStr);

                    Usuario novoUsuario = new Usuario(nome, endereco, diaNasc, email, telefone, cpf, descricao, senha);


                    //confirma os dados do usuario
                    UsuarioDb db = new UsuarioDb(Cadastro.this);
                    int confirma = db.buscaUsuarioCadastro(novoUsuario);

                    if (confirma == 0) {
                        //cadastra usuario no bd
                        db.insereUsuario(novoUsuario);
                        Toast.makeText(Cadastro.this, "Cadastro feito com sucesso.", Toast.LENGTH_LONG).show();

                        finish();

                    } else if (confirma == 1) {
                        Toast.makeText(Cadastro.this, "Erro! Nome de usuário já registrado.", Toast.LENGTH_LONG).show();
                    } else if (confirma == 2) {
                        Toast.makeText(Cadastro.this, "Erro! CPF já registrado.", Toast.LENGTH_LONG).show();
                    } else if (confirma == 3) {
                        Toast.makeText(Cadastro.this, "Erro! Email já registrado.", Toast.LENGTH_LONG).show();
                    } else if (confirma == 4) {
                        Toast.makeText(Cadastro.this, "Erro! Telefone já registrado.", Toast.LENGTH_LONG).show();
                    }

                } catch (ParseException ex) {
                    System.err.println("erro no formato da data...");
                }

            } else {
                Toast toast = Toast.makeText(Cadastro.this, "As senhas não combinam.", Toast.LENGTH_LONG);
                toast.show();
            }

        }

    }

}