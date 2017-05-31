package com.example.gimnasio;

import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

//import android.os.Build;

public class PrincipalActivity extends ActionBarActivity implements OnClickListener {
	
	Button btnEntrarClientes;
	Button btnRegClientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnEntrarClientes = (Button) findViewById(R.id.btnEntrarClientes);
        btnRegClientes = (Button) findViewById(R.id.btnRegClientes);
        btnEntrarClientes.setOnClickListener(this);
        btnRegClientes.setOnClickListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_principal, container, false);
            return rootView;
        }
    }

	@Override
	public void onClick(View boton) {
		// TODO Auto-generated method stub
		if(boton.getId()== findViewById(R.id.btnEntrarClientes).getId()){
			Intent obtntodos =new Intent(this, TodosActivity.class);
			startActivity(obtntodos);
			finish();
		}
		if(boton.getId()== findViewById(R.id.btnRegClientes).getId()){
			Intent obtntodos =new Intent(this, RegistrarActivity.class);
			startActivity(obtntodos);
			finish();
		}
		
	}

}
