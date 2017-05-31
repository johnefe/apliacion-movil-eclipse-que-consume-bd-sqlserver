package com.example.gimnasio;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

//import com.example.gimnasio.RegistrarActivity.asynInserta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActualizarActivity extends Activity implements OnClickListener {
	
	
	String  id,ciudad,sede,nombre,telefono,direccion;
	TextView txtciudad, txtsede, txtnombre, txttelefono, txtdireccion;
	Button regresar;
	Button actualizar;
	
	private ProgressDialog dialogo;
	
	private static String SOAP_ACTION="http://tempuri.org/UpdateCliente";
	private static String NAMESPACE="http://tempuri.org/";
	private static String METHOD_NAME="UpdateCliente";
	//private static String URL="http://192.168.0.20:8086/Service.asmx?ListClientes";
	private static String URL="http://192.168.1.2:8083/Service.asmx?UpdateCliente";
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actualizar);
		id= getIntent().getStringExtra("tid");
		nombre=getIntent().getStringExtra("tnombre");
		telefono=getIntent().getStringExtra("ttelefono");
		direccion=getIntent().getStringExtra("tdireccion");
		
		txtnombre=(TextView) findViewById(R.id.txtAcnombre);
		txttelefono=(TextView) findViewById(R.id.txtActelefono);
		txtdireccion=(TextView) findViewById(R.id.txtAcdireccion);
		
		txtnombre.setText(nombre);
		txttelefono.setText(telefono);
		txtdireccion.setText(direccion);
		
		actualizar=(Button)findViewById(R.id.btnAcactualizar);
		regresar=(Button)findViewById(R.id.btnAcregresar);
		
		actualizar.setOnClickListener(this);
		regresar.setOnClickListener(this);
		
		
	}
	
	public Boolean InvocarWS(){
		boolean bandera=true;
		try {
			SoapObject respuesta= new SoapObject(NAMESPACE,METHOD_NAME);
			respuesta.addProperty("id_cliente", Integer.valueOf(id));//comprobar si es correcto escribir asi
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
	
	private void lanzarprincipal(){
		Intent btnoprincipal= new Intent(this, PrincipalActivity.class);
		startActivity(btnoprincipal);
		finish();
	}
	class asynInserta extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			dialogo=new ProgressDialog(ActualizarActivity.this);
			dialogo.setMessage("Actualizando datos...");
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
	public void onClick(View button) {
		// TODO Auto-generated method stub
		
		if(button.getId()==findViewById(R.id.btnAcactualizar).getId()){
			new asynInserta().execute();
		}
		if(button.getId()==findViewById(R.id.btnAcregresar).getId()){
			Intent btnoprincipal= new Intent(this, TodosActivity.class);
			startActivity(btnoprincipal);
			finish();
		}
	
}

}
