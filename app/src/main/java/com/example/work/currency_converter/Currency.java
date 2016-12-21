package com.example.work.currency_converter;

/**
 * Created by Work on 11/10/2016.
 */

public class Currency {

    public String acronym;
    public String value;
    public double ratio;
    public String name= "";
    public String symbol= "";

//    public Currency(String _acronym, String _value)
//    {
//        acronym= _acronym;
//        value= _value;
//    }

    public Currency(String line)
    {
        String[] strings= line.split(";");
        acronym= strings[0];
        String textValue= strings[1].replace(',', '.');
        try {
            ratio= Double.parseDouble(textValue);
        } catch (Exception e) {
            System.err.println("Impossible de caster en double : " + strings[1]);
            e.printStackTrace();
            ratio= 2.0;
        }
        value= textValue;
    }
}
