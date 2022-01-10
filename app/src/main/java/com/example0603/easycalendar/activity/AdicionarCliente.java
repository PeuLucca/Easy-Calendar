package com.example0603.easycalendar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example0603.easycalendar.R;

import com.example0603.easycalendar.helper.DaoCliente;
import com.example0603.easycalendar.model.Cliente;

public class AdicionarCliente extends AppCompatActivity {

    private EditText nome,telefone,celular,email;
    private Cliente clienteAtualizar;
    private static final String textoSalvar= "Adicione novos clientes";
    private static final String textoAtualizar= "Atualize seus clientes";
    private Button btnSalvar;
    private TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cliente);

        nome = findViewById(R.id.editNome);
        telefone = findViewById(R.id.editTelefone);
        celular = findViewById(R.id.editCelular);
        email = findViewById(R.id.editEmail);
        btnSalvar = findViewById(R.id.btnSalvar);
        txtTitulo = findViewById(R.id.txtTitulo);

        txtTitulo.setText( textoSalvar );
        btnSalvar.setText( "Salvar" );

        clienteAtualizar = (Cliente) getIntent().getSerializableExtra("clienteSelecionado");

        if( clienteAtualizar!=null ){ // para atualizar

            txtTitulo.setText( textoAtualizar );
            btnSalvar.setText("Atualizar");

            nome.setText( clienteAtualizar.getNome() );
            telefone.setText( clienteAtualizar.getTelefone() );
            celular.setText( clienteAtualizar.getCelular() );
            email.setText( clienteAtualizar.getEmail() );
        }

    }

    public void salvar(View view){

        if(verificarDados()){ // entao os dados estao corretos

            DaoCliente daoCliente = new DaoCliente( getApplicationContext() );
            Cliente cliente = new Cliente();

            if( clienteAtualizar == null ){ // entao é para salvar

                cliente.setNome( nome.getText().toString() );
                if(telefone.getText().toString().equals("")){
                    cliente.setTelefone( "..." );
                }else {
                    cliente.setTelefone( telefone.getText().toString() );
                }
                if( celular.getText().toString().equals("") ){
                    cliente.setCelular( "..." );
                }else {
                    cliente.setCelular( celular.getText().toString() );
                }
                if( email.getText().toString().equals("") ){
                    cliente.setEmail( "..." );
                }else {
                    cliente.setEmail( email.getText().toString() );
                }

                if( daoCliente.salvar( cliente ) ){
                    Toast.makeText(getApplicationContext(),"Cliente salvo com sucesso",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Erro ao salvar cliente",Toast.LENGTH_SHORT).show();
                }


            }else{ //entao é para atualizar

                cliente.setNome( nome.getText().toString() );
                if(telefone.getText().toString().equals("")){
                    cliente.setTelefone( "..." );
                }else {
                    cliente.setTelefone( telefone.getText().toString() );
                }
                if( celular.getText().toString().equals("") ){
                    cliente.setCelular( "..." );
                }else {
                    cliente.setCelular( celular.getText().toString() );
                }
                if( email.getText().toString().equals("") ){
                    cliente.setEmail( "..." );
                }else {
                    cliente.setEmail( email.getText().toString() );
                }
                cliente.setIdCliente( clienteAtualizar.getIdCliente() );

                if( daoCliente.atualizar( cliente ) ){
                    Toast.makeText(getApplicationContext(),"Cliente atualizado com sucesso",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Erro ao atualizar cliente",Toast.LENGTH_SHORT).show();
                }
            }

        }else{
            Toast.makeText(getApplicationContext(),"Insira os dados obrigatórios",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean verificarDados(){

        if(nome.getText().toString().equals("")){
            return false;
        }else {
            return true;
        }
    }
}