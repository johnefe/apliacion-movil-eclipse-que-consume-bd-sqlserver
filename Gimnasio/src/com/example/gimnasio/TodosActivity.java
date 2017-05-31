package com.example.gimnasio;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
//import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

//import android.R.integer;
import android.app.Activity;
//import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class TodosActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	GridView grvDatos;
	Button btnRegresamenu;
	Button btnTactualizar;
	Button btnTeliminar;
	TextView txtTcliente;
	int ciudad, sede;
	String id, nombre, telefono,direccion;
	private ProgressDialog dialogo;
	
	private static String SOAP_ACTION="http://tempuri.org/ListClientes";
	private static String NAMESPACE="http://tempuri.org/";
	private static String METHOD_NAME="ListClientes";
	//private static String URL="http://192.168.0.20:8086/Service.asmx?ListClientes";
	private static String URL="http://192.168.1.2:8092/Service.asmx?ListClientes";
	
	private String[] cargardatos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todosclientes);
		
		
		btnRegresamenu =(Button) findViewById(R.id.btnRegresamenu);
		btnRegresamenu.setOnClickListener(this);
		grvDatos =(GridView)findViewById(R.id.gridViewClientes);
		txtTcliente=(TextView) findViewById(R.id.lblTclientes);
		grvDatos.setOnItemClickListener(this);
		btnTactualizar=(Button) findViewById(R.id.btnTactualizar);
		btnTactualizar.setOnClickListener(this);
		
		btnTeliminar=(Button)findViewById(R.id.btnTeliminar);
		btnTeliminar.setOnClickListener(this);
		new asyntodos().execute();
		
	}
	
	public Boolean InvocarWS(){
		boolean bandera=true;
		try {
			SoapObject respuesta= new SoapObject(NAMESPACE,METHOD_NAME);
			SoapSerializationEnvelope sobre= new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sobre.dotNet=true;
			sobre.setOutputSoapObject(respuesta);
			HttpTransportSE transporte =new HttpTransportSE(URL);
			transporte.call(SOAP_ACTION, sobre);
			
			SoapObject response=(SoapObject) sobre.getResponse();
			SoapObject diffgram =(SoapObject) response.getProperty("diffgram");
			SoapObject newdataset=(SoapObject) diffgram.getProperty("NewDataSet");
			
						
			cargardatos=new String[ newdataset.getPropertyCount()*4];
			int fila=0;
			for(int i=0; i<newdataset.getPropertyCount();i++){
				
				SoapObject datosxml =(SoapObject) newdataset.getProperty(i);
		
				
				cargardatos[fila]=datosxml.getProperty(0).toString();
				cargardatos[fila+1]=datosxml.getProperty(3).toString();
				cargardatos[fila+2]=datosxml.getProperty(4).toString();
				cargardatos[fila+3]=datosxml.getProperty(5).toString();

				
				fila+=4;
			}
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			bandera=false;
		}catch (XmlPullParserException e) {
			// TODO: handle exception
			e.printStackTrace();
			bandera=false;
		}
		
		return bandera;
	}

	//-- cargar datos en el gridwiev
	
	public void cargar_elementos(){
		ArrayAdapter<String> adaptador= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, cargardatos);
		grvDatos.setAdapter(adaptador);
	}
	
	//clase para sincronizar las tareas y no se tome como aplicacion sin responder
	
	class asyntodos extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			dialogo=new ProgressDialog(TodosActivity.this);
			dialogo.setMessage("cargando datos...");
			dialogo.setIndeterminate(false);
			dialogo.setCancelable(false);
			dialogo.show();
			
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			if(InvocarWS()){
				return "ok";
				
			}else{
				
				return "error";
			}
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			
			dialogo.dismiss();
			if(result.equals("ok")){
				//aqui cargaremos los datos al grid
				cargar_elementos();
				
			}
		}
		
	}

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		
		if(boton.getId()==findViewById(R.id.btnRegresamenu).getId()){
			Intent btnoprincipal= new Intent(this, PrincipalActivity.class);
			startActivity(btnoprincipal);
			finish();
		}
		if(boton.getId()==findViewById(R.id.btnTactualizar).getId()){
			Intent btnoprincipal= new Intent(this, ActualizarActivity.class);
			btnoprincipal.putExtra("tid", id);
			btnoprincipal.putExtra("tciudad", ciudad);
			btnoprincipal.putExtra("tsede", sede);
			btnoprincipal.putExtra("tnombre", nombre);
			btnoprincipal.putExtra("ttelefono", telefono);
			btnoprincipal.putExtra("tdireccion", direccion);
			startActivity(btnoprincipal);
			finish();
		}
		if(boton.getId()==findViewById(R.id.btnTeliminar).getId()){
			Intent btnoprincipal= new Intent(this, EliminarActivity.class);
			btnoprincipal.putExtra("tid", id);
			btnoprincipal.putExtra("tnombre", nombre);
			startActivity(btnoprincipal);
			finish();
		}
		
	}
	@SuppressWarnings("unused")
	private Boolean esnumero(String valor){
		try {
			
			int d=Integer.valueOf(valor);
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long arg3) {
		// TODO Auto-generated method stub
		if(esnumero(cargardatos[posicion])){
			
			txtTcliente.setText(cargardatos[posicion+1]);
			
			id=cargardatos[posicion];

			nombre=cargardatos[posicion+1];
			telefono=cargardatos[posicion+2];
			direccion=cargardatos[posicion+3];
		}
		
	}
	
	
}
