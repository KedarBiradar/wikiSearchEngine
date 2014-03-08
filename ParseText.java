import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * User: Kedar Biradar
 * Date: 12/1/14
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParseText
{

    static HashSet <String> stopwordList=new HashSet<String>();
     static int docId;
     static createIndex ci;

    //split input string into words

    public void makeWord(String inputText,String pageId,createIndex createindex,String contentType)
    {
        int length,i;
        char c;
        boolean linkFound=false;
        int count1,count2,count3,count4,count5,count6,count7,count8,count9;



        StringBuilder word=new StringBuilder();
       // String finalWord=null,stemmedWord=null;
        docId=Integer.parseInt(pageId.trim());

        ci=createindex;
       // Stemmer st=new Stemmer();
        //createIndex createindex=new createIndex();

        length=inputText.length();

        for(i=0;i<length;i++)
        {
            c=inputText.charAt(i);
            if(c<123 && c>96)
            {
                word.append(c);
            }

            else if(c=='{')
            {
                if ( i+9 < length && inputText.substring(i+1,i+9).equals("{infobox") )
                {

                    StringBuilder infoboxString = new StringBuilder();

                     count1 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);
                        infoboxString.append(c);
                        if ( c == '{') {
                            count1++;
                        }
                        else if ( c == '}') {
                            count1--;
                        }
                        if ( count1 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            if ( c == '=' ) {infoboxString.deleteCharAt(infoboxString.length()-1);}
                            break;
                        }
                    }

                    processInfobox(infoboxString);

                }

                else if ( i+8 < length && inputText.substring(i+1,i+8).equals("{geobox") )
                {

                    StringBuilder geoboxString = new StringBuilder();

                    count2 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);
                        geoboxString.append(c);
                        if ( c == '{') {
                            count2++;
                        }
                        else if ( c == '}') {
                            count2--;
                        }
                        if ( count2 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            if ( c == '=' ) {geoboxString.deleteCharAt(geoboxString.length()-1);}
                            break;
                        }
                    }

                   // processGeobox(geoboxString);
                }

                else if ( i+6 < length && inputText.substring(i+1,i+6).equals("{cite") )
                {

                    /*
                     *  Citations are to be removed.
                     */

                     count3 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);
                        if ( c == '{') {
                            count3++;
                        }
                        else if ( c == '}') {
                            count3--;
                        }
                        if ( count3 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            break;
                        }
                    }

                }

                else if ( i+4 < length && inputText.substring(i+1,i+4).equals("{gr") )
                {

                    /*
                     *  {{GR .. to be removed
                     */

                    count4 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);
                        if ( c == '{') {
                            count4++;
                        }
                        else if ( c == '}') {
                            count4--;
                        }
                        if ( count4 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            break;
                        }
                    }

                }
                else if ( i+7 < length && inputText.substring(i+1,i+7).equals("{coord") )
                {

                    /**
                     * Coords to be removed
                     */

                     count5 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);

                        if ( c == '{') {
                            count5++;
                        }
                        else if ( c == '}') {
                            count5--;
                        }
                        if ( count5 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            break;
                        }
                    }

                }

                //System.out.println("process infobox");
            }
            else if(c=='[')
            {
               // System.out.println("process square brace");

                if ( i+11 < length && inputText.substring(i+1,i+11).equalsIgnoreCase("[category:"))
                {

                    StringBuilder categoryString = new StringBuilder();

                     count6 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);
                        categoryString.append(c);
                        if ( c == '[') {
                            count6++;
                        }
                        else if ( c == ']') {
                            count6--;
                        }
                        if ( count6 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            if ( c == '=' ) {categoryString.deleteCharAt(categoryString.length()-1);}
                            break;
                        }
                    }

                   // System.out.println("category string="+categoryString.toString());
                    processCategories(categoryString);

                }
                else if ( i+8 < length && inputText.substring(i+1,i+8).equalsIgnoreCase("[image:") ) {

                    /**
                     * Images to be removed
                     */

                     count7 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);
                        if ( c == '[') {
                            count7++;
                        }
                        else if ( c == ']') {
                            count7--;
                        }
                        if ( count7 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            break;
                        }
                    }

                }
                else if ( i+7 < length && inputText.substring(i+1,i+7).equalsIgnoreCase("[file:") ) {

                    /**
                     * File to be removed
                     */

                    count8 = 0;
                    for ( ; i < length ; i++ ) {

                        c = inputText.charAt(i);

                        if ( c == '[') {
                            count8++;
                        }
                        else if ( c == ']') {
                            count8--;
                        }
                        if ( count8 == 0 || (c == '=' && i+1 < length && inputText.charAt(i+1) == '=')) {
                            break;
                        }
                    }

                }

            }
            else if(c=='<')
            {
                //System.out.println("Process < >");

                if ( i+4 < length && inputText.substring(i+1,i+4).equalsIgnoreCase("!--") ) {

                    /**
                     * Comments to be removed
                     */

                    int locationClose = inputText.indexOf("-->" , i+1);
                    if ( locationClose == -1 || locationClose+2 > length ) {
                        i = length-1;
                    }
                    else {
                        i = locationClose+2;
                    }

                }
                else if ( i+5 < length && inputText.substring(i+1,i+5).equalsIgnoreCase("ref>") ) {

                    /**
                     * References to be removed
                     */
                    int locationClose = inputText.indexOf("</ref>" , i+1);
                    if ( locationClose == -1 || locationClose+5 > length ) {
                        i = length-1;
                    }
                    else {
                        i = locationClose+5;
                    }

                }
                else if ( i+8 < length && inputText.substring(i+1,i+8).equalsIgnoreCase("gallery") ) {

                    /**
                     * Gallery to be removed
                     */
                    int locationClose = inputText.indexOf("</gallery>" , i+1);
                    if ( locationClose == -1 || locationClose+9 > length) {
                        i = length-1;
                    }
                    else {
                        i = locationClose+9;
                    }
                }

            }
            else if ( c == '=' && i+1 < length && inputText.charAt(i+1) == '=')
            {

                linkFound = false;
                i+=2;
                while ( i < length && ((c = inputText.charAt(i)) == ' ' || (c = inputText.charAt(i)) == '\t') )
                {
                    i++;
                }

                if ( i+14 < length && inputText.substring(i , i+14 ).equals("external links") )
                {
                    //System.out.println("External link found");
                    linkFound = true;
                    i+= 14;
                }

            }
            else if ( c == '*' && linkFound == true )
            {

                //System.out.println("Link found");
                 count9 = 0;
                boolean spaceParsed = false;
                StringBuilder link = new StringBuilder();
                while ( count9 != 2 && i < length )
                {
                    c = inputText.charAt(i);
                    if ( c == '[' || c == ']' )
                    {
                        count9++;
                    }
                    if ( count9 == 1 && spaceParsed == true)
                    {
                        link.append(c);
                    }
                    else if ( count9 != 0 && spaceParsed == false && c == ' ')
                    {
                        spaceParsed = true;
                    }
                    i++;
                }

                StringBuilder linkWord = new StringBuilder();
                for ( int j = 0 ; j < link.length() ; j++ )
                {
                    char currentCharTemp = link.charAt(j);
                    if ( (int)currentCharTemp >= 'a' && (int)currentCharTemp <= 'z' )
                    {
                        linkWord.append(currentCharTemp);
                    }
                    else
                    {

                      //  System.out.println("link : " + linkWord.toString());
                        processLink(linkWord);
                        linkWord.setLength(0);
                    }
                }
                if ( linkWord.length() > 1 )
                {
                    //System.out.println("link : " + linkWord.toString());
                    processLink(linkWord);
                    linkWord.setLength(0);
                }

            }
            else
            {
                if(word.length()>1)
                filterAndAddWord(word,contentType);

                word.setLength(0);
            }
        }


        if(word.length()>1)
        filterAndAddWord(word,contentType);

        word.setLength(0);
        /*
        if(word.length()>0)
        {
            finalWord=new String(word);
            if(!(checkStopword(finalWord)))
            {
                st.add(finalWord.toCharArray(),finalWord.length());
                stemmedWord=st.stem();

                createindex.addToTreeSet(stemmedWord,docId);

            }

            word.delete(0, word.length());
        } */

    }


    public void processInfobox(StringBuilder infoboxString)
    {

        int infoboxLength=infoboxString.length();
        int i;
        char ch;
        StringBuilder tempWord=new StringBuilder();
        for(i=0;i<infoboxLength;i++)
        {
            ch=infoboxString.charAt(i);
            if(Character.isLetter(ch))
            {
                tempWord.append(ch);
            }
            else
            {
                filterAndAddWord(tempWord,"infobox");
                tempWord.setLength(0);
            }
        }

        if(tempWord.length()>1)
            filterAndAddWord(tempWord,"infobox");


    }

    public void processLink(StringBuilder linkString)
    {

        int linkLength=linkString.length();
        int i;
        char ch;
        StringBuilder tempWord=new StringBuilder();
        for(i=0;i<linkLength;i++)
        {
            ch=linkString.charAt(i);
            if(Character.isLetter(ch))
            {
                tempWord.append(ch);
            }
            else
            {
                filterAndAddWord(tempWord,"link");
                tempWord.setLength(0);
            }
        }
        if(tempWord.length()>1)
            filterAndAddWord(tempWord,"link");
    }

    public void processCategories(StringBuilder categoryString)
    {

        int categoryLength=categoryString.length();
        int i;
        char ch;
        StringBuilder tempWord=new StringBuilder();
        for(i=0;i<categoryLength;i++)
        {
            ch=categoryString.charAt(i);
            if(Character.isLetter(ch))
            {
                tempWord.append(ch);
            }
            else
            {
                filterAndAddWord(tempWord,"category");
                tempWord.setLength(0);
            }
        }

        if(tempWord.length()>1)
            filterAndAddWord(tempWord,"category");

    }

    public void filterAndAddWord(StringBuilder word,String type)
    {
        Stemmer st=new Stemmer();
        String finalWord,stemmedWord;
        if(word.length()>1 && word.length() < 100)
        {
            finalWord=new String(word);

            if(!(checkStopword(finalWord)))
            {
                st.add(finalWord.toCharArray(),finalWord.length());
                stemmedWord=st.stem();

                ci.addToTreeSet(stemmedWord,docId,type);
            }
            //word.delete(0,word.length());
        }

    }

    public void buildStopwordDict(String stopwordfile)
    {
        try
        {
            FileInputStream fstream = new FileInputStream(stopwordfile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                stopwordList.add(strLine.trim());
            }
            System.out.println("created stopwordlist successfully");
        }
        catch (Exception e)
        {
            System.out.println("Error Creating Stopword file ::"+e);
        }

    }


    public boolean checkStopword(String s)
    {
        if(stopwordList.contains(s))
            return true;
        else
            return false;

    }

}