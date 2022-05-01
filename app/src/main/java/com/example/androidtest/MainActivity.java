/* This example connects to MySQL using JDBC running on Android.
 * It uses a background thread to run JDBC so that the main thread stays responsive.
 * A handler is used to write MySQL data to the UI.
 * The application uses an INTERNET permission. The j-connector jar file
 * is on the Java build path and in the libs folder.
 */

package com.example.androidtest;

import android.app.Activity;
import android.view.Menu;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.TextView;
import java.util.ArrayList;
import java.sql.*;

public class MainActivity extends Activity {

	private TextView texted = null;
	private Thread t = null;

	private Handler handle = new Handler() {
		public void handleMessage(Message msg){
			String str = (String)msg.obj;
			texted.append(str + "\n");
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		texted = (TextView)findViewById(R.id.text);
		t = new Thread(background);
		t.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Runnable background = new Runnable() {
		public void run(){
			String URL = "jdbc:mysql://frodo.bentley.edu:3306/test";
	        String username = "harry";
	        String password = "harry";

	        try //create connection and statement objects
				( //resources part
	           Connection con = DriverManager.getConnection (
	                URL, username, password))
			{ //begin try
	            Statement stmt = con.createStatement();

	            // execute SQL commands to create table, insert data, select contents
	           
	                stmt.executeUpdate("drop table if exists first;");
	                stmt.executeUpdate("create table first(id integer primary key, city varchar(25));");
	                stmt.executeUpdate("insert into first values(1, 'Waltham');");
	                stmt.executeUpdate("insert into first values(2, 'Cambridge');");
	                stmt.executeUpdate("insert into first values(3, 'Boston');");
	              
	                ResultSet result = stmt.executeQuery("select * from first;");
	                
	                while (result.next()) {
	                
	                String str = String.format("%d    %s", result.getInt("id"), result.getString("city"));
	                Log.e("JDBC",str );
	                
	                Message msg = handle.obtainMessage(1,str);
					handle.sendMessage(msg);			
	                }
	                t = null;
	                
	        } catch (SQLException e) {
	            Log.e("JDBC","problems with SQL sent to "+URL+
	                ": "+e.getMessage());
	        }
	    
		}
	};

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

	

}

