import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * User: Kedar Biradar
 * Date: 14/1/14
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class mergeIndex
{
    String indexFileName=new String("Index_temp.txt");



    public String mergeLine(String word,TreeMap<String,HashSet<Integer>>line)
    {
        StringBuffer tempWord=new StringBuffer();
        Set s=line.get(word);
        Iterator i=s.iterator();

        while(i.hasNext())
        {
            tempWord.append(i.next()+",");
        }

        return tempWord.toString();

    }
    public String[] separateWord(String word)
    {
        String [] temp ={"",""};
        temp=word.split(":");
        return temp;
    }

    public void mergeFile(String fileName,TreeMap<String,HashSet<Integer>>tree)
    {
        try
        {
            FileWriter wr;
            BufferedWriter br;

            File tempFile=new File(fileName);
            FileReader inputFile=new FileReader(tempFile.getAbsoluteFile());
            BufferedReader reader=new BufferedReader(inputFile);

            File outputFile=new File(indexFileName);
            String line=new String();
            String [] words;
            StringBuilder buffer=new StringBuilder();
            int i=0,length;


            if(outputFile.exists())
            {
                wr=new FileWriter(outputFile.getAbsoluteFile(),true);
            }
            else
            {

                outputFile.createNewFile();
                wr=new FileWriter(outputFile.getAbsoluteFile());
            }

            br=new BufferedWriter(wr);

            Object [] keys=tree.keySet().toArray();
            line=reader.readLine();

            length=keys.length;

            while(i<length && line!=null )
            {
                words=separateWord(line) ;

                if(words[0].compareToIgnoreCase(keys[i].toString())>0)
                {
                    buffer.append(keys[i].toString()+":");
                    buffer.append(mergeLine(keys[i].toString(),tree));
                    br.write(buffer.toString()+"\n");
                    buffer.setLength(0);
                    i++;
                }
                else if(words[0].compareToIgnoreCase(keys[i].toString())==0)
                {
                    buffer.append(line);
                    buffer.append(mergeLine(keys[i].toString(),tree));
                    br.write(buffer.toString()+"\n");
                    line=reader.readLine();
                    i++;
                    buffer.setLength(0);

                }
                else
                {
                    br.write(line+"\n");
                    line=reader.readLine();
                }

            }

            while (line!=null)
            {
                br.write(line+"\n");
                line=reader.readLine();
            }

            while(i<length)
            {
                buffer.append(keys[i].toString()+":");
                buffer.append(mergeLine(keys[i].toString(),tree));
                br.write(buffer.toString()+"\n");
                buffer.setLength(0);
                i++;
            }

            reader.close();
            inputFile.close();
            tempFile.delete();

            br.close();
            wr.close();
            outputFile.renameTo(tempFile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}