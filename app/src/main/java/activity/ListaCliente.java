package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.noteapp.R;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterCliente;
import helper.DaoCliente;
import model.Cliente;

public class ListaCliente extends AppCompatActivity {

    private RecyclerView recycler;
    private List<Cliente> listaCliente = new ArrayList<>();
    private TextView txtMensagem,txtQnt,txtInstrucoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cliente);

        recycler = findViewById(R.id.recyclerTarefas);
        txtMensagem = findViewById(R.id.txtMensagem);
        txtQnt = findViewById(R.id.txtQnt);
        txtInstrucoes = findViewById(R.id.txtInstrucoes);
    }

    public void carregarListaClientes(){

        DaoCliente dao = new DaoCliente(getApplicationContext());
        listaCliente = dao.listar();
        int qntClientes = listaCliente.size();

        txtQnt.setText( qntClientes + " clientes" );

        if(listaCliente.isEmpty()){
            txtMensagem.setVisibility(View.VISIBLE);
            txtInstrucoes.setVisibility(View.INVISIBLE);
        }

        AdapterCliente adapterCliente = new AdapterCliente(listaCliente,getApplicationContext(),recycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);
        recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recycler.setAdapter(adapterCliente);
    }

    @Override
    protected void onStart() {
       carregarListaClientes();
        super.onStart();
    }
}