package com.example.appagendamentotrabalhadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListaUsuario extends ArrayAdapter<String[]> {

    private Context contextoPai;
    ArrayList<String[]> usuario;

    private static class ViewHolder {
        private TextView nomeUsuario;
        private TextView servicoLista;
        private TextView precoServico;
    }

    public ItemListaUsuario(Context contexto, ArrayList<String[]> dados) {
        super(contexto, R.layout.item_lista_usuarios, dados);

        this.contextoPai = contexto;
        this.usuario = dados;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String[] usuarioAtual = usuario.get(position);
        ItemListaUsuario.ViewHolder novaView;
        final View resultado;

        //primeira vez em que a lista é criada
        if (convertView == null) {
            novaView = new ItemListaUsuario.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lista_usuarios, parent, false);

            novaView.nomeUsuario = (TextView) convertView.findViewById(R.id.nomeUsuarioListaTxt);
            novaView.servicoLista = (TextView) convertView.findViewById(R.id.servicoListaTxt);
            novaView.precoServico = (TextView) convertView.findViewById(R.id.precoServicoListaTxt);

            resultado = convertView;
            convertView.setTag(novaView);
        } else {
            //item modificado
            novaView = (ItemListaUsuario.ViewHolder) convertView.getTag();
            resultado = convertView;
        }
        //setando os valores
        novaView.nomeUsuario.setText(usuarioAtual[0]);
        novaView.servicoLista.setText(usuarioAtual[2]);
        novaView.precoServico.setText(usuarioAtual[3]);

        return resultado;
    }
}
