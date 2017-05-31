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

public class RegistrarActivity extends Activity implements OnClickListener {
	
	private ProgressDialog dialogo;	
	
	private static String SOAP_ACTION="http://tempuri.org/InsertCliente";
	private static String NAMESPACE="http://tempuri.org/";
	private static String METHOD_NAME="InsertCliente";
	//private static String URL="http://192.168.0.20:8086/Service.asmx?ListClientes";
	private static String URL="http://192.168.1.2:8092/Service.asmx?ListClientes";
	
	TextView txtciudad;
	TextView txtsede;
	TextView txtnombre;
	TextView txttelefono;
	TextView txtdireccion;
	
	Button btnRegistrar;
	Button btnRegresar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inserta);
		
		txtciudad =(TextView) findViewById(R.id.txtciudad);
		txtsede =(TextView) findViewById(R.id.txtsede);
		txtnombre=(TextView) findViewById(R.id.txtnombre);
		txttelefono =(TextView) findViewById(R.id.txttelefono);
		txtdireccion =(TextView) findViewById(R.id.txtdireccion);
		btnRegistrar=(Button) findViewById(R.id.btnRegistrar);
		btnRegresar=(Button)findViewById(R.id.btnRegresarInsert);
		
		btnRegistrar.setOnClickListener(this);
		btnRegresar.setOnClickListener(this);
		
	}
	
	public Boolean InvocarWS(){
		boolean bandera=true;
		try {
			SoapObject respuesta= new SoapObject(NAMESPACE,METHOD_NAME);
			
			respuesta.addProperty("id_ciudad", Integer.parseInt(txtciudad.getText().toString()));
			respuesta.addProperty("id_sede", Integer.parseInt(txtsede.getText().toString()));
			respuesta.addProperty("nombre", txtnombre.getText().toString());
			respuesta.addProperty("telefono", txttelefono.getText().toString());
			respuesta.addProperty("direccion", txtdireccion.getText().toString());
			
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
			//super.onPreExecute();
			dialogo=new ProgressDialog(RegistrarActivity.this);
			dialogo.setMessage("Registrando datos...");
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
				//cargar_elementos();
				txtciudad.setText("");
				txtsede.setText("");
				txtnombre.setText("");
				txttelefono.setText("");
				txtdireccion.setText("");
				
			}
		}
		
	}

@Override
public void onClick(View button) {
	// TODO Auto-generated method stub
	
	if(button.getId()==findViewById(R.id.btnRegistrar).getId()){
		new asynInserta().execute();
	}
	if(button.getId()==findViewById(R.id.btnRegresarInsert).getId()){
		Intent btnoprincipal= new Intent(this, PrincipalActivity.class);
		startActivity(btnoprincipal);
		finish();
	}
	
}

}
