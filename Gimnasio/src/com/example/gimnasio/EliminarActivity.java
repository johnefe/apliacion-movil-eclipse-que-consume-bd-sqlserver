package com.example.gimnasio;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EliminarActivity extends Activity implements OnClickListener {
	
	Button btnRegresamenu;
	Button btnTeliminar;
	TextView txtnombrecli;
	String  id, nombre;
	private ProgressDialog dialogo;
	
	private static String SOAP_ACTION="http://tempuri.org/DeleteCliente";
	private static String NAMESPACE="http://tempuri.org/";
	private static String METHOD_NAME="DeleteCliente";
	//private static String URL="http://192.168.0.20:8086/Service.asmx?DeleteCliente";
	private static String URL="http://192.168.1.2:8089/Service.asmx?DeleteCliente";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eliminar);
		
		btnRegresamenu=(Button)findViewById(R.id.btnEregresar);
		btnTeliminar=(Button) findViewById(R.id.btnElimeliminar);
		
		btnRegresamenu.setOnClickListener(this);
		btnTeliminar.setOnClickListener(this);
		
		txtnombrecli=(TextView) findViewById(R.id.txtnombrecli);
		id= getIntent().getStringExtra("tid");
		nombre=getIntent().getStringExtra("tnombre");
		
		txtnombrecli.setText(getIntent().getStringExtra("tnombre"));
		
		
		
	}
	private void lanzarprincipal(){
		Intent btnoprincipal= new Intent(this, PrincipalActivity.class);
		startActivity(btnoprincipal);
		finish();
	}
	public Boolean InvocarWS(){
		boolean bandera=true;
		try {
			SoapObject respuesta= new SoapObject(NAMESPACE,METHOD_NAME);
			
			respuesta.addProperty("id_cliente", Integer.valueOf(getIntent().getStringExtra("tid")));
						
			SoapSerializationEnvelope sobre= new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sobre.dotNet=true;
			sobre.setOutputSoapObject(respuesta);
			HttpTransportSE transporte =new HttpTransportSE(URL);
			transporte.call(SOAP_ACTION, sobre);
			
			SoapObject result=(SoapObject) sobre.bodyIn;
			int estado=Integer.parseInt(result.getProperty(0).toString());
			
			if(estado==0){
				bandera=false;
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

	class asynInserta extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialogo=new ProgressDialog(EliminarActivity.this);
			dialogo.setMessage("Eliminando datos...");
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
						
			dialogo.dismiss();
			if(result.equals("ok")){
				lanzarprincipal();
				
			}
		}
		
	}

	@Override
	public void onClick(View button ) {
		// TODO Auto-generated method stub
		if(button.getId()==findViewById(R.id.btnElimeliminar).getId()){
			new asynInserta().execute();
		}
		if(button.getId()==findViewById(R.id.btnEregresar).getId()){
			Intent btnoprincipal= new Intent(this, PrincipalActivity.class);
			startActivity(btnoprincipal);
			finish();
		}
		
	}

}
