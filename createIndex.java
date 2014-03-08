import java.util.*;
import java.io.*;

/**
 * User: Kedar Biradar
 * Date: 13/1/14
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */

class dataType
{
    public int count;
    public boolean type[];

    public dataType()
    {
        this.count=0;
        type=new boolean[5];

        for(int i=0;i<5;i++)
            type[i]=false;
    }

    boolean[] getType()
    {
        return type;
    }

    void setType(boolean[] type)
    {
        this.type = type;
    }

    int getCount()
    {
        return count;
    }

    void setCount(int count)
    {
        this.count = count;
    }
}



public class createIndex
{
//    static int  count=0;

        /*
    public enum wordType
    {}*/
    public static TreeMap<String, HashMap<Integer,dataType>> wordTree;

    public createIndex()
    {
        wordTree=new TreeMap<String, HashMap<Integer,dataType>>();
    }


    int defineTypeIndex(String wordType)
    {
        if(wordType.equalsIgnoreCase("title"))
            return 0;
        else if(wordType.equalsIgnoreCase("body"))
            return 1;
        else if(wordType.equalsIgnoreCase("category"))
            return 2;
        else if(wordType.equalsIgnoreCase("infobox"))
            return 3;
        else if(wordType.equalsIgnoreCase("link"))
            return 4;
        else
            return 1;

    }


    public void addToTreeSet(String word,int docId,String wordType)
    {

        int index=0;

        index=defineTypeIndex(wordType);



        HashMap<Integer,dataType> docList =  wordTree.get(word);

        if(docList==null)
        {
            docList = new HashMap<Integer,dataType>();
        }

        dataType currentWordType=docList.get(docId);

        if(currentWordType==null)
        {
            currentWordType=new dataType();
        }

        currentWordType.count++;
        currentWordType.type[index]=true;

        docList.put(docId,currentWordType);

        //docList.add(docId);
        wordTree.put(word, docList);
    }


    public void dumpToFile(int FileNo)
    {

        String filename = new String("/media/kedar/STUDY/Evaluate/Temp_File"+FileNo);

        Set docIds;
        StringBuilder docType=new StringBuilder();
        StringBuilder line=new StringBuilder();
        BufferedWriter writer = null;

        HashMap<Integer,dataType>docList;
        dataType currentWordType;
        int id;
        try {
            //create a temporary file

            File indexFile = new File(filename);

            // This will output the full path where the file will be written to...

            writer = new BufferedWriter(new FileWriter(indexFile));

            for(String key:wordTree.keySet())
            {
                line.append(key+":");
                docList=wordTree.get(key);
                docIds=docList.keySet();

                Iterator i=docIds.iterator();
                while(i.hasNext())
                {

                    id=Integer.parseInt(i.next().toString());
                    currentWordType=docList.get(id);


                    if(currentWordType.type[0]==true)
                            docType.append("t");

                    if(currentWordType.type[1]==true)
                        docType.append("b");

                    if(currentWordType.type[2]==true)
                        docType.append("c");

                    if(currentWordType.type[3]==true)
                        docType.append("i");

                    if(currentWordType.type[4]==true)
                        docType.append("l");


                    line.append(id+docType.toString()+currentWordType.count+",");
                    docType.setLength(0);
                }

                writer.write(line.toString()+"\n");
                line.setLength(0);
                docType.setLength(0);

            }
            //writer.write("Hello world!");
        } catch (Exception e)
        {
            System.out.println("Error in dumptofile()");
            //e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close the writer regardless of what happens...
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
