package com.example.work.currency_converter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
//import android.widget.ListAdapter;

/**
 * Created by Work on 11/10/2016.
 */

public class CurrenciesAdapter extends ArrayAdapter<Currency>
{
    public CurrenciesAdapter(Context context) {
        super(context, R.layout.ligne);
    }

    @Override
    public void add(Currency c) {
        super.add(c);
    }

    public void loadFromCSV(String CSVName)
    {
        System.out.println("Chargement du fichier de devises");
        try {
            InputStream instream = super.getContext().getAssets().open(CSVName);

            if (instream != null)
            {
                InputStreamReader inputReader = new InputStreamReader(instream);
                BufferedReader bufferReader = new BufferedReader(inputReader);
                String line = "";
                try
                {
                    while ((line = bufferReader.readLine()) != null) {
                        Currency currency= new Currency(line);
                        add(currency);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } catch (java.io.IOException e)
        {
            System.err.println("Impossibe de lire le fichier csv");
            e.printStackTrace();
        }
    }


    // utilise le fichier xml pour chaque élément de l'ArrayAdapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Currency item= getItem(position);

//		applique le texte à la vue
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(R.layout.ligne, parent, false);

        TextView acronym= (TextView)convertView.findViewById(R.id.currencyName);
        acronym.setText(item.acronym);

        TextView value= (TextView)convertView.findViewById(R.id.currencyValue);
        value.setText(item.value);

        return convertView;
    }

    public void refresh(double moneyValue)
    {
        int nb= getCount();
        for (int i= 0; i < nb; ++i) {
            Currency c= getItem(i);
            c.value= String.valueOf(c.ratio * moneyValue);
        }

        notifyDataSetChanged();
    }
}
