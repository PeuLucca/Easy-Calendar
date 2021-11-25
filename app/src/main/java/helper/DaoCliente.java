package helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class DaoCliente{

    private SQLiteDatabase escreve,le;
    private Cursor cursor;

    public DaoCliente(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    public boolean salvar(Cliente cliente) {

        ContentValues cv = new ContentValues();
        cv.put( "nome" , cliente.getNome() );
        cv.put( "telefone" , cliente.getTelefone() );
        if(cliente.getCelular().equals("...")){
            cv.put( "celular" , cliente.getCelular() );
        }else {
            cv.put( "celular" , "55"+cliente.getCelular() );
        }
        cv.put( "email" , cliente.getEmail() );

        try{
            escreve.insert( DbHelper.TABELA_CLIENTE , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean atualizar(Cliente cliente) {

        ContentValues cv = new ContentValues();
        cv.put( "nome" , cliente.getNome() );
        cv.put( "telefone" , cliente.getTelefone() );
        if( cliente.getCelular().startsWith("55") ){
            cv.put( "celular" , cliente.getCelular() );
        }else{
            if(cliente.getCelular().equals("...")){
                cv.put( "celular" , cliente.getCelular() );
            }else{
                cv.put( "celular" , "55"+cliente.getCelular() );
            }
        }
        cv.put( "email" , cliente.getEmail() );

        try{

            String[] args = { cliente.getIdCliente().toString() };
            escreve.update( DbHelper.TABELA_CLIENTE, cv, "idCliente=?", args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deletar(Cliente cliente) {

        String[] args = { cliente.getIdCliente().toString() };

        try {
            escreve.delete(DbHelper.TABELA_CLIENTE,"idCliente=?",args);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public List<Cliente> listar() {

        List<Cliente> clienteList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_CLIENTE + " ORDER BY nome;";
         cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

                Cliente cliente = new Cliente();

                @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("idCliente") );
                @SuppressLint("Range") String nome = cursor.getString( cursor.getColumnIndex( "nome" ) );
                @SuppressLint("Range") String telefone = cursor.getString( cursor.getColumnIndex( "telefone" ) );
                @SuppressLint("Range") String celular = cursor.getString( cursor.getColumnIndex( "celular" ) );
                @SuppressLint("Range") String email = cursor.getString( cursor.getColumnIndex( "email" ) );

                cliente.setIdCliente( id );
                cliente.setNome( nome );
                cliente.setTelefone( telefone );
                cliente.setCelular( celular );
                cliente.setEmail( email );

                clienteList.add( cliente );
        }

        return clienteList;
    }
}
