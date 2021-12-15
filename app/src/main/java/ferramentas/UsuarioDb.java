package ferramentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

import modelo.Usuario;

public class UsuarioDb extends SQLiteOpenHelper {

    private Context context;

    public UsuarioDb(Context context) {

        super(context, "usuario", null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String criaTabela = "CREATE TABLE IF NOT EXISTS usuario (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT, endereco TEXT, datanasc DATE, email TEXT, telefone TEXT, cpf TEXT," +
                "descricao TEXT, senha TEXT)";

        db.execSQL(criaTabela);

    }

    public void insereUsuario(Usuario novoUsuario) {

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            ContentValues valores = new ContentValues();

            valores.put("nome", novoUsuario.getNome());
            valores.put("endereco", novoUsuario.getEndereco());
            valores.put("datanasc", novoUsuario.getDataNasc().getTime());
            valores.put("email", novoUsuario.getEmail());
            valores.put("telefone", novoUsuario.getTelefone());
            valores.put("cpf", novoUsuario.getCpf());
            valores.put("descricao", novoUsuario.getDescricao());
            valores.put("senha", novoUsuario.getSenha());

            db.insert("usuario", null, valores);

        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

    }

    public void atualizaUsuario() {

    }

    public ArrayList<Usuario> buscaUsuarioLogin(String telefone, String senha) {

        ArrayList<Usuario> resultado = new ArrayList<>();

        String sql = "SELECT * FROM usuario";

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            Cursor tuplas = db.rawQuery(sql, null);

            if (tuplas.moveToFirst()) {

                do {
                    String tel = tuplas.getString(5);
                    String password = tuplas.getString(8);

                    if (tel.equals(telefone) && password.equals(senha)) {
                        int id = tuplas.getInt(0);
                        String nome = tuplas.getString(1);
                        String endereco = tuplas.getString(2);
                        Date data = new Date(tuplas.getLong(3));
                        String email = tuplas.getString(4);
                        String cpf = tuplas.getString(6);
                        String descricao = tuplas.getString(7);
                        float media = 0;

                        Usuario user = new Usuario(id, nome, endereco, data, email, tel, cpf, descricao, password, media);

                        resultado.add(user);
                    }

                } while (tuplas.moveToNext());

            }

        } catch (SQLiteException ex) {
            System.err.println("erro na consulta com o banco de dados.");
            ex.printStackTrace();
        }

        return resultado;

    }

    public int buscaUsuarioCadastro(Usuario confirmaUsuario) {

        int confirma = 0;

        String sql = "SELECT * FROM usuario";

        try (SQLiteDatabase db = this.getWritableDatabase()) {

            Cursor tuplas = db.rawQuery(sql, null);

            if (tuplas.moveToFirst()) {

                do {
                    String tel = tuplas.getString(5);
                    String nome = tuplas.getString(1);
                    String email = tuplas.getString(4);
                    String cpf = tuplas.getString(6);

                    if (confirmaUsuario.getNome().equals(nome)) {
                        confirma = 1;
                    } else if (confirmaUsuario.getCpf().equals(cpf)) {
                        confirma = 2;
                    } else if (confirmaUsuario.getEmail().equals(email)) {
                        confirma = 3;
                    } else if (confirmaUsuario.getTelefone().equals(tel)) {
                        confirma = 4;
                    }

                } while (tuplas.moveToNext());

            }

        } catch (SQLiteException ex) {
            System.err.println("erro na consulta com o banco de dados.");
            ex.printStackTrace();
        }

        return confirma;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
