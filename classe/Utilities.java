/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XML11Serializer;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Nivekiba
 */
public class  Utilities {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static String idToStr(int num){
        /*String str1 = null,str2 = null;
        str1 = str.substring(0,3);
        str2 = str.substring(3,6);
        strf = str1+"-"+str2;*/
        String strf="", str = ""+num;
        String sti[] = new String[10];
        for(int i =0; i<str.length()/3;i++)
        {
                sti[i] = str.substring(3*i,3*(i+1));
                strf = strf+sti[i]+"-";
        }
        strf = strf.substring(0, strf.length()-1);
        return strf;
    }

    public static int strToId(String str){
        int num =0;
        String[] st = str.split("-");
        String string;
        string = st[0]+st[1];
        num = Integer.parseInt(string);
        return num;
    }
    
    public static int genId(int a, int b){
        return (int) (Math.random()*(b-a+1)+a);
    }
    /* Les codes suivants seront utilisés pour les couleurs : rouge, vert, bleu, jaune, blanc, noir, violet*/
    public static String surligne (String chaine, String bgColor) {
        //TO - DO
        String chaine_surligned;
        switch (bgColor){
            case "rouge":
                chaine_surligned =  ANSI_RED_BACKGROUND + chaine + ANSI_RESET;
                break;
            case "vert":
                chaine_surligned =  ANSI_GREEN_BACKGROUND + chaine + ANSI_RESET;
                break;
            case "bleu":
                chaine_surligned =  ANSI_BLUE_BACKGROUND + chaine + ANSI_RESET;
                break;
            case "jaune":
                chaine_surligned =  ANSI_YELLOW_BACKGROUND + chaine + ANSI_RESET;
                break;
            case "blanc":
                chaine_surligned = ANSI_WHITE_BACKGROUND + ANSI_BLACK + chaine + ANSI_RESET;
                break;
            default:
                chaine_surligned = ANSI_WHITE_BACKGROUND + ANSI_BLACK + chaine + ANSI_RESET;
        }
        return chaine_surligned;
    }
    public static String colorie(String aChercher, String chaine, String color){
        String chaine_colored = "";
        String[] temp = chaine.split(aChercher);
        //TO - DO
        switch(color) {
            case ("rouge"):
                chaine_colored = ANSI_RED + aChercher + ANSI_RESET;
                break;
            case ("bleu"):
                chaine_colored = ANSI_BLUE + aChercher + ANSI_RESET;
                break;
            case ("vert"):
                chaine_colored = ANSI_GREEN + aChercher + ANSI_RESET;
                break;
            case ("violet"):
                chaine_colored = ANSI_PURPLE + aChercher + ANSI_RESET;
                break;
            case ("blanc"):
                chaine_colored = ANSI_BLACK_BACKGROUND + ANSI_WHITE + aChercher + ANSI_RESET;
                break;
            case ("jaune"):
                chaine_colored = ANSI_YELLOW + aChercher + ANSI_RESET;
                break;
            case ("noir"):
                chaine_colored = ANSI_BLACK + aChercher + ANSI_RESET;
                break;
            default:
                chaine_colored = ANSI_BLACK_BACKGROUND + ANSI_WHITE + aChercher + ANSI_RESET;
                break;
        }
        if(temp.length != 0){
            String tmp = temp[0];
            for(int i=1; i<temp.length;i++){
                tmp += chaine_colored + temp[i];
            }
            return tmp;
        }else
            return chaine_colored;
    }
    public static String formatPrix(double prix){
        return String.format("%.2f", prix);
    }
    
    public static void loadConfig(String name) throws ParserConfigurationException, SAXException, IOException{
        File inputFile = new File(name);
        
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        
        Node bd = doc.getElementsByTagName("bd").item(0);
        
        USER_BD = ((Element)bd).getElementsByTagName("user").item(0).getTextContent();
        PASS_BD = ((Element)bd).getElementsByTagName("pass").item(0).getTextContent();
        PORT_BD = ((Element)bd).getElementsByTagName("port").item(0).getTextContent();
        DBNAME_BD = ((Element)bd).getElementsByTagName("db").item(0).getTextContent();
        URL_BD = ((Element)bd).getElementsByTagName("url").item(0).getTextContent();
        
        Node admin = doc.getElementsByTagName("admin").item(0);
        
        ADMIN_USER = ((Element)admin).getElementsByTagName("user").item(0).getTextContent();
        ADMIN_PASS = ((Element)admin).getElementsByTagName("pass").item(0).getTextContent();
        
        Node server = doc.getElementsByTagName("server").item(0);
        
        
        SERVER_URL = ((Element)server).getElementsByTagName("url").item(0).getTextContent();
    }
    public static String USER_BD;
    public static String PASS_BD;
    public static String PORT_BD;
    public static String DBNAME_BD;
    public static String URL_BD;
    
    public static String ADMIN_USER;
    public static String ADMIN_PASS;
    
    public static String SERVER_URL;
}