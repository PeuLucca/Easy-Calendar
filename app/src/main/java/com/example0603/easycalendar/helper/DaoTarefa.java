package com.example0603.easycalendar.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example0603.easycalendar.model.Tarefa;

public class DaoTarefa{

    private SQLiteDatabase escreve,le;
    private Cursor cursor;
    private Context c;

    public DaoTarefa(Context context) {
        this.c = context;
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @SuppressLint("Range")
    public Long getHorario(String nomeString){
        Long idCliente = null;

        String sqlListar = "SELECT idCliente FROM " + DbHelper.TABELA_CLIENTE + " WHERE nome = '" + nomeString + "';";
        cursor = le.rawQuery( sqlListar, null );

        while (cursor.moveToNext()){
            idCliente = cursor.getLong( cursor.getColumnIndex("idCliente") );
        }

        return idCliente;
    }

    public boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "idCliente" , tarefa.getIdCliente() );
        cv.put( "dataMes" , tarefa.getDataMes() );
        cv.put( "dataDia" , tarefa.getDataDia() );
        cv.put( "dataAno" , tarefa.getDataAno() );
        cv.put( "horario" , tarefa.getHorario() );
        cv.put("status",tarefa.getStatus());

        try{
            escreve.insert( DbHelper.TABELA_TAREFA , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public void deletarTarefa_Cliente(Long id){

        String[] idCliente = { String.valueOf(id) };

        try{
            escreve.delete(DbHelper.TABELA_TAREFA,"idCliente = ?",idCliente);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean verificarTarefa(Long idCliente){

        List<Tarefa> l = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " WHERE idCliente = " + idCliente +";";
        cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long idTarefa = cursor.getLong( cursor.getColumnIndex("idTarefa") );
            @SuppressLint("Range") Long idClient = cursor.getLong( cursor.getColumnIndex("idCliente") );
            @SuppressLint("Range") int dataMes = cursor.getInt( cursor.getColumnIndex( "dataMes" ) );
            @SuppressLint("Range") int dataDia = cursor.getInt( cursor.getColumnIndex( "dataDia" ) );
            @SuppressLint("Range") int dataAno = cursor.getInt( cursor.getColumnIndex( "dataAno" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") int status = cursor.getInt( cursor.getColumnIndex( "status" ) );

            tarefa.setIdTarefa( idTarefa );
            tarefa.setIdCliente( idClient );
            tarefa.setDataMes( dataMes );
            tarefa.setDataDia( dataDia );
            tarefa.setDataAno( dataAno );
            tarefa.setHorario( horario );
            tarefa.setStatus( status );

            l.add( tarefa );

            if( l.isEmpty() ){
                return false;
            }else {
                return true;
            }
        }

        return false;

    }

    public boolean atualizar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "idCliente" , tarefa.getIdCliente() );
        cv.put( "dataMes" , tarefa.getDataMes() );
        cv.put( "dataDia" , tarefa.getDataDia() );
        cv.put( "dataAno" , tarefa.getDataAno() );
        cv.put( "horario" , tarefa.getHorario() );
        cv.put("status",tarefa.getStatus());

        try{

            String[] args = { tarefa.getIdTarefa().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "idTarefa=?", args );

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(c.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public boolean deletar(Tarefa tarefa) {

        String[] args = { tarefa.getIdTarefa().toString() };

        try {
            escreve.delete(DbHelper.TABELA_TAREFA,"idTarefa=?",args);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<Tarefa> listarTarefasCalendar(int dia,int mes,int ano){

        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " " +
                "WHERE dataDia = "+dia + " AND dataMes = "+mes + " AND dataAno = "+ano + " ORDER BY horario ASC;";
        cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long idTarefa = cursor.getLong( cursor.getColumnIndex("idTarefa") );
            @SuppressLint("Range") Long idCliente = cursor.getLong( cursor.getColumnIndex("idCliente") );
            @SuppressLint("Range") int dataMes = cursor.getInt( cursor.getColumnIndex( "dataMes" ) );
            @SuppressLint("Range") int dataDia = cursor.getInt( cursor.getColumnIndex( "dataDia" ) );
            @SuppressLint("Range") int dataAno = cursor.getInt( cursor.getColumnIndex( "dataAno" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") int status = cursor.getInt( cursor.getColumnIndex( "status" ) );

            tarefa.setIdTarefa( idTarefa );
            tarefa.setIdCliente( idCliente );
            tarefa.setDataMes( dataMes );
            tarefa.setDataDia( dataDia );
            tarefa.setDataAno( dataAno );
            tarefa.setHorario( horario );
            tarefa.setStatus( status );

            tarefaList.add( tarefa );
        }

        return tarefaList;

    }

    public String getNome(Long id){

        String name="";
        String sqlListar = "SELECT nome FROM " + DbHelper.TABELA_CLIENTE +
                " WHERE idCliente = " + id +";";
        cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()) {

            @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex("nome"));

            name = nome;
        }

        return  name;
    }

    public String getTelefone(Long id){

        String phone="";
        String sqlListar = "SELECT telefone FROM " + DbHelper.TABELA_CLIENTE +
                " WHERE idCliente = " + id +";";
        cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()) {

            @SuppressLint("Range") String telefone = cursor.getString( cursor.getColumnIndex( "telefone" ) );

            phone = telefone;
        }

        return  phone;
    }

    public String getCelular(Long id){

        String cell="";
        String sqlListar = "SELECT celular FROM " + DbHelper.TABELA_CLIENTE +
                " WHERE idCliente = " + id +";";
        cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()) {

            @SuppressLint("Range") String celular = cursor.getString( cursor.getColumnIndex( "celular" ) );

            cell = celular;
        }

        return cell;
    }

    public void atualizarStatus(Tarefa tarefa){

        ContentValues cv = new ContentValues();
        cv.put( "idCliente" , tarefa.getIdCliente() );
        cv.put( "dataMes" , tarefa.getDataMes() );
        cv.put( "dataDia" , tarefa.getDataDia() );
        cv.put( "dataAno" , tarefa.getDataAno() );
        cv.put( "horario" , tarefa.getHorario() );
        cv.put("status",tarefa.getStatus());

        try{
            String[] args = { tarefa.getIdTarefa().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "idTarefa=?", args );

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
