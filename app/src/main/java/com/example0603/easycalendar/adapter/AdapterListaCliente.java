package com.example0603.easycalendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example0603.easycalendar.R;

import java.util.ArrayList;
import java.util.List;

import com.example0603.easycalendar.model.Cliente;

public class AdapterListaCliente extends RecyclerView.Adapter<AdapterListaCliente.MyViewHolder>{
    private List<Cliente> listaCliente = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;

    public AdapterListaCliente(List<Cliente> listaCliente,Context context,RecyclerView recyclerView) {
        this.listaCliente = listaCliente;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_nome_clientes,parent,false);

        return new AdapterListaCliente.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Cliente cliente = listaCliente.get( position );
        holder.nome.setText( cliente.getNome() );

        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCliente.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeDoCliente);

        }
    }
}
