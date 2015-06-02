package com.ayalamartinez.conex_ws;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
public class ActPrincipal extends ActionBarActivity {
	TextView output; 
	ProgressBar pb; 
	List<MyTask> tasks; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_principal);
		output = (TextView)findViewById(R.id.txtMessage); 
		output.setMovementMethod(new ScrollingMovementMethod()); 
		//se coloca el ScrollingMovementMethod para permitir el scroll del tv
		pb = (ProgressBar) findViewById(R.id.progressBar1); 
		pb.setVisibility(View.INVISIBLE); 
		
		tasks = new ArrayList<>();
		
	}
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.act_principal, menu);
		return super .onCreateOptionsMenu(menu); 
		//ActionBar actionBar = getActionBar(); 
		//actionBar.setDisplayHomeAsUpEnabled(true);
	} 
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if (item.getItemId() == R.id.action_refrescar ){
			if (isOnline()) {
				requestData("http://services.hanselandpetal.com/feeds/flowers.xml");
				return true; 
			} else {
				Toast.makeText(this, "La red no se encuentra disponible ", Toast.LENGTH_LONG).show(); 
			}
			
			 
		}
		return false; 
	}
	private void requestData(String uri) {
		MyTask task = new MyTask(); 
		task.execute(uri);
		//task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
	}
	public void updateDisplay(String message){
		output.append(message + "\n");
	}
	
	protected boolean isOnline(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo netInfo = cm.getActiveNetworkInfo(); 
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			//lo que quiere decir la condicion del IF, es que si existe un valor del obj. netifo, y/o se encuentra conectado o conectando, 
			return true; 
		} else {
			return false; 
		}
	}
	
	private class MyTask extends AsyncTask<String, String, String>{
		//se crea un método al cual se le hace Override para poder acceder al hilo principal mientras se da a lugar el método background
		@Override
		protected void onPreExecute() {
			// el método onPreExecute es ejecutado previo a que se de a lugar el doinbackground, y a diferencia de doinbackground
			//tiene acceso al hilo principal 
			//de forma que se pueden realizar cambios en el hilo principal 
			//se insertan los 
			updateDisplay("Empezando tarea"); 
			if (tasks.size() == 0) {
				pb.setVisibility(View.VISIBLE); 
			}
			tasks.add(this); 
			//esto hace referencia al objeto tipo task actual
			
			
		}
		@Override
		//doInbackground recive una lista de Strings, y devuelve un String (donde el segundo viene del 3er parametro establecido en el asynctask)
		protected String doInBackground(String... params) {

			String content = ManejadorHTTP.getData(params[0]);
			return content;  

			//			for (int i = 0; i < params.length; i++) {
			//				publishProgre ss("Working with" + params[i]);
			//				try {
			//					Thread.sleep(1000);
			//				} catch (InterruptedException e) {	 
			//					e.printStackTrace();
			//				} 
			//			}
			//return "Tarea completa";
			// dentro de la sintaxis de doinbackground, los tres puntos suspensivos quieren decir
			//que se le puede pasar a la clase cualquer numero de parametros, siempre y cuando los mismos sean de tipo String
			//todo el código que corra dentro de este método, correra en un hilo secundario 

		}
		//un método que se da a lugar una vez haya corrido el método doinbackground
		//se llama onpostexecute , el cual recibe un result, el tipo de dato que recibe result viene dado 
		//por el tercer parametro que se le pasa a AsyncTask, como se ve aqui, tipo String. 
		//de igual forma, el valor que devuelve doinbackground es pasado directamente a onPostExecute
		//y cerrando el circulo, onpostexecute tiene acceso al hilo principal 
		//se trata del mismo tipo de dato que recibe 
		@Override
		protected void onPostExecute(String result) {
			updateDisplay(result); 
			pb.setVisibility(View.INVISIBLE); 
		
			tasks.remove(this); 
			//esto hace referencia al objeto tipo task actual

			if (tasks.size() == 0) {
				pb.setVisibility(View.INVISIBLE); 
			}
						
		}
		@Override
		protected void onProgressUpdate(String... values) {
			updateDisplay(values[0]);
		}

	}
}

