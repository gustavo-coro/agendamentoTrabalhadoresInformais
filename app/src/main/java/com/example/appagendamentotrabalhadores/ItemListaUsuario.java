package com.example.appagendamentotrabalhadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelo.Usuario;

public class ItemListaUsuario extends ArrayAdapter<Usuario> {

    private Context contextoPai;
    ArrayList<Usuario> usuario;

    private static class ViewHolder {
        private TextView nomeUsuario;
        private TextView telefoneUsuario;
        private TextView emailUsuario;
    }

    public ItemListaUsuario(Context contexto, ArrayList<Usuario> dados) {
        super(contexto, R.layout.item_lista_usuarios, dados);

        this.contextoPai = contexto;
        this.usuario = dados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario usuarioAtual = usuario.get(position);
        ItemListaUsuario.ViewHolder novaView;
        final View resultado;

        //primeira vez em que a lista é criada
        if (convertView == null) {
            novaView = new ItemListaUsuario.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_usuarios, parent, false);

            novaView.nomeUsuario = (TextView) convertView.findViewById(R.id.nomeUsuarioListaTxt);
            novaView.telefoneUsuario = (TextView) convertView.findViewById(R.id.telefoneUsuarioListaTxt);
            novaView.emailUsuario = (TextView) convertView.findViewById(R.id.emailUsuarioListaTxt);

            resultado = convertView;
            convertView.setTag(novaView);
        } else {
            //item modificado
            novaView = (ItemListaUsuario.ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando os valores
        novaView.nomeUsuario.setText(usuarioAtual.getNome());
        novaView.telefoneUsuario.setText(usuarioAtual.getTelefone());
        novaView.emailUsuario.setText(usuarioAtual.getEmail());

        return resultado;
    }
}
