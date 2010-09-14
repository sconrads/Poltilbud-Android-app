package no.poltilbud.feed;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProductList extends ListActivity {
	
	private List<Product> m_productsFromXML = null;
	private ArrayList<Product> m_products = null;
	private ProgressDialog m_ProgressDialog;
	private ProductAdapter m_adapter;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        if (isOnline())
        	loadFeed();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, 0, 
				0, R.string.android_sax);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);
		ArrayAdapter<String> adapter =
			(ArrayAdapter<String>) this.getListAdapter();
		if (adapter.getCount() > 0){
			adapter.clear();
		}
		this.loadFeed();
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent viewMessage = new Intent(Intent.ACTION_VIEW, 
				Uri.parse(m_productsFromXML.get(position).getLink().toExternalForm()));
		this.startActivity(viewMessage);
	}

	private void loadFeed(){
    	try{
	    	final FeedParser parser = FeedParserFactory.getParser();
	    	long start = System.currentTimeMillis();
	    	
	    	m_products = new ArrayList<Product>();
	        this.m_adapter = new ProductAdapter(this, R.layout.row, m_products);
	                setListAdapter(this.m_adapter);
	    	
	    	Runnable viewProducts = new Runnable(){
	            @Override
	            public void run() {
	            	m_productsFromXML = parser.parse();	 
	            	m_products = new ArrayList<Product>(m_productsFromXML.size());
	    	    	for (Product msg : m_productsFromXML){
	    	    		m_products.add(msg);
	    	    	}
	            	runOnUiThread(returnRes);
	            }
	        };
		    Thread thread =  new Thread(null, viewProducts, "MagentoBackground");
		        thread.start();
	        m_ProgressDialog = ProgressDialog.show(ProductList.this,    
	              "Vennligst vent...", "Henter tilbud ...", true);
	          		   
	    	long duration = System.currentTimeMillis() - start;
	    	Log.i("Poltilbud", "Parser duration=" + duration);
	    	String xml = writeXml();
	    	Log.i("Poltilbud", xml);
	    	
	    	this.setListAdapter(m_adapter);
	    	
    	} catch (Throwable t){
    		Log.e("Poltilbud",t.getMessage(),t);
    	}
    }
	
	private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_products != null && m_products.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_products.size();i++)
                	m_adapter.add(m_products.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
	private String writeXml(){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "messages");
			serializer.attribute("", "number", String.valueOf(m_productsFromXML.size()));
			for (Product msg: m_productsFromXML){
				serializer.startTag("", "message");
				serializer.attribute("", "date", msg.getDate());
				serializer.startTag("", "title");
				serializer.text(msg.getTitle());
				serializer.endTag("", "title");
				serializer.startTag("", "url");
				serializer.text(msg.getLink().toExternalForm());
				serializer.endTag("", "url");
				serializer.startTag("", "body");
				serializer.text(msg.getDescription());
				serializer.endTag("", "body");
				serializer.endTag("", "message");
			}
			serializer.endTag("", "messages");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public boolean isOnline() {
   	 ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
   	 if (cm.getActiveNetworkInfo() == null)
   		 return false;
   	 return cm.getActiveNetworkInfo().isConnectedOrConnecting();

   	}
}