package com.example0603.easycalendar.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example0603.easycalendar.R;

import java.util.List;

import com.example0603.easycalendar.activity.AdicionarCliente;
import com.example0603.easycalendar.activity.ListaCliente;
import com.example0603.easycalendar.helper.DaoCliente;
import com.example0603.easycalendar.helper.DaoTarefa;
import com.example0603.easycalendar.model.Cliente;

public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.MyViewHolder>{

    private List<Cliente> listaCliente;
    private ListaCliente listClient = new ListaCliente();
    private Context context;
    private RecyclerView recycler;

    public AdapterCliente(List<Cliente> lista, Context cont,RecyclerView recyclerView) {
        this.listaCliente = lista;
        this.context = cont;
        this.recycler = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_adapter,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Cliente cliente = listaCliente.get( position );
        holder.nome.setText( cliente.getNome() );

        holder.tel.setText( cliente.getTelefone() );

        holder.tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telefone = "tel:" + cliente.getTelefone();

                if( cliente.getTelefone().equals("...") ){
                    Toast.makeText(context.getApplicationContext(),"Cliente não possui número salvo",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse( telefone ));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        holder.cel.setText( cliente.getCelular() );

        holder.cel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cliente.getCelular().equals("...")){

                    Toast.makeText(context.getApplicationContext(),"Cliente não possui número salvo",
                            Toast.LENGTH_SHORT).show();

                }else {
                    Intent sendIntent = new Intent ();
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setAction (Intent.ACTION_SEND);
                    sendIntent.putExtra (Intent.EXTRA_TEXT, "Olá " + cliente.getNome() + "!");
                    sendIntent.putExtra ("jid", cliente.getCelular() + "@s.whatsapp.net");
                    sendIntent.setPackage ("com.whatsapp");
                    sendIntent.setType ("text / plain");
                    context.startActivity (sendIntent);
                }

            }
        });

        holder.email.setText( cliente.getEmail() );

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cliente.getEmail().equals("...")){

                    Toast.makeText(context.getApplicationContext(),"Cliente não possui email salvo",
                            Toast.LENGTH_SHORT).show();

                }else {
                    Intent email = new Intent(Intent.ACTION_SENDTO);
                    email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    email.setData(Uri.parse("mailto:"));
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{
                            cliente.getEmail() });
                    email.putExtra(Intent.EXTRA_SUBJECT, "");
                    email.putExtra(Intent.EXTRA_TEXT, "Olá " + cliente.getNome());
                    context.startActivity(email);
                }

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( context.getApplicationContext(), AdicionarCliente.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("clienteSelecionado", cliente);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                alertDialog.setTitle( "CONFIRMAR EXCLUSÃO\n\n" );
                alertDialog.setMessage( "Deseja excluir este cliente: " + cliente.getNome() + "?");

                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DaoCliente daoCliente = new DaoCliente(context.getApplicationContext());
                        DaoTarefa daoTarefa = new DaoTarefa(context.getApplicationContext());

                        boolean r = daoTarefa.verificarTarefa(cliente.getIdCliente());
                        if( r ){

                            Toast.makeText(context.getApplicationContext(), "Ainda existem tarefas com este cliente",
                                    Toast.LENGTH_SHORT).show();
                        }else{

                            daoTarefa.deletarTarefa_Cliente(cliente.getIdCliente());

                            if(daoCliente.deletar(cliente)){
                                DaoCliente dao = new DaoCliente(context.getApplicationContext());
                                listaCliente = dao.listar();

                                AdapterCliente adapterCliente = new AdapterCliente(listaCliente,context.getApplicationContext(),recycler);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
                                recycler.setLayoutManager(layoutManager);
                                recycler.setHasFixedSize(true);
                                recycler.addItemDecoration(new DividerItemDecoration(context.getApplicationContext(), LinearLayoutManager.VERTICAL));
                                recycler.setAdapter(adapterCliente);

                            }else {
                                Toast.makeText(context.getApplicationContext(),"Erro ao excluir cliente",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                alertDialog.setNegativeButton("Não", null);
                alertDialog.create().show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return this.listaCliente.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nome,tel,cel,email;
        private ImageView edit,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNome);
            tel = itemView.findViewById(R.id.textTelefone);
            cel = itemView.findViewById(R.id.textCelular);
            email = itemView.findViewById(R.id.textEmail);

            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

        }
    }

}
