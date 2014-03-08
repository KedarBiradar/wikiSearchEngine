import java.io.*;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.Vector;

/**
 * User: Kedar Biradar
 * Date: 28/1/14
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */

public class MergeSortedFiles
{


    PriorityQueue<PQueueNode> queue = new PriorityQueue<PQueueNode>();
    TreeMap<Integer, Boolean> fileAvailable = new TreeMap<Integer, Boolean>();
    BufferedReader [] readers;
    char [][] lines,terms;
    boolean [] readLine;
    static int fileNumber;


    String sortByTF(String inputLine)
    {
        TreeMap<Integer , Vector<String>> sortedMap=new TreeMap<Integer, Vector<String>>(Collections.reverseOrder());
        StringBuilder outputLine=new StringBuilder();
        String keyword,postingList;
        String []docIds;
        int termCount;
        Vector<String> tempList,tempList2;

        processQuery pq= new processQuery();


        keyword=inputLine.substring(0,inputLine.indexOf(":"));
        postingList=inputLine.substring(inputLine.indexOf(":")+1,inputLine.length());
        docIds=postingList.split(",");

        outputLine.append(keyword+":");
        for(String id:docIds)
        {
            termCount=pq.countTF(id);
            tempList=sortedMap.get(termCount);

            if(tempList==null)
            {
                tempList=new Vector<String>();
            }
            tempList.add(id);

            sortedMap.put(termCount,tempList);
        }

        for(int w:sortedMap.keySet())
        {
            tempList2=sortedMap.get(w);
            for(String id:tempList2)
            {
                outputLine.append(id+",");
            }
        }

        return outputLine.toString();
    }


    public void MergeFiles(int NoOfFiles)  //throws IOException
    {

        fileNumber=NoOfFiles;
        String fileName="/media/kedar/STUDY/Evaluate/Temp_File";
        //String secondaryIndexFileName="/media/kedar/New Volume/IRE/100mb/Temp_File";

        BufferedWriter writerPrimaryIndexFile = null;
      //  BufferedWriter writeSecondaryIndexFile=null;

        long offset=0;
        StringBuilder line=null;
        try
        {
            File file = new File("/media/kedar/STUDY/Evaluate/InvertedIndex.txt");
      //      File file2 = new File("/media/kedar/New Volume/IRE/100mb/SecondaryIndexFile.txt");

            writerPrimaryIndexFile = new BufferedWriter(new FileWriter(file));
     //       writeSecondaryIndexFile= new BufferedWriter(new FileWriter(file2));

            readers = new BufferedReader[fileNumber+1];

            lines = new char[fileNumber+1][];
            terms = new char[fileNumber+1][];
            readLine = new boolean[fileNumber+1];

            for(int i =0; i < fileNumber; i++)
            {
                readers[i] = new BufferedReader(new FileReader(fileName+i));
                lines[i] = readers[i].readLine().toCharArray();

                int j = 0, length = lines[i].length;

                terms[i] = new char[length];

                for(j=0; j < length; j++)
                {
                    if(lines[i][j] == ':')
                        break;
                    terms[i][j] = lines[i][j];
                }

                PQueueNode node = new PQueueNode(terms[i], j, i);

                queue.add(node);


            }

            fileAvailable = new TreeMap<Integer, Boolean>();
            for(int k = 0; k < fileNumber; k++)
            {
                fileAvailable.put(k, true);
            }

            Boolean noFileAvailable = false;
            String newLine;
            while(true)
            {

                 line = RemoveLines();

                if(line == null)
                    break;

                newLine=sortByTF(line.toString());

                writerPrimaryIndexFile.write(newLine.toCharArray());
                writerPrimaryIndexFile.write('\n');


                  /*
                keyword=line.substring(0,line.indexOf(":"));
                System.out.println("keyword="+keyword+" offset="+offset);
                writeSecondaryIndexFile.write(keyword+" "+offset+"\n");

                offset+=line.length()+1;
                */

                // Add lines...

                AddLines();

            }

            while(!queue.isEmpty())
            {
                String line1;
                PQueueNode head = queue.remove();
                int index = head.getIndex();

                newLine=sortByTF(lines[index].toString());

                writerPrimaryIndexFile.write(newLine);
                writerPrimaryIndexFile.write('\n');


                /*
                line1=lines[index].toString();
                keyword=line.substring(0,line1.indexOf(":"));

                writeSecondaryIndexFile.write(keyword+" "+offset+"\n");
                offset+=line1.length()+1;
                */

            }


            writerPrimaryIndexFile.flush();
          //  writeSecondaryIndexFile.flush();

        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
        finally
        {
                                            // *****************

            try
            {

                writerPrimaryIndexFile.close();
            //    writeSecondaryIndexFile.close();
                for(int i =0; i < fileNumber; i++)
            {
                readers[i].close();
               // File fileToDelete = new File(fileName+i);
              //  fileToDelete.delete();
            }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public StringBuilder RemoveLines()
    {

        StringBuilder buffer=new StringBuilder();
        if(queue.isEmpty())
            return null;

        PQueueNode head = queue.remove();
        int index = head.getIndex();

        readLine[index] = true;

        if(queue.isEmpty())
            return new StringBuilder(new String(lines[index]));

        buffer.setLength(0);

        int length=0;
        for(;length < lines[index].length; length++)
        {
            if(lines[index][length] == ':')
                break;
        }

        buffer.append(lines[index], 0, length);
        buffer.append(':');

        char[] currentTerm = new char[length];
        for(int i =0 ; i < length; i++)
        {
            currentTerm[i] = lines[index][i];
        }


        // ****		For now don't add doc ids.. *****************
        length++;
        while(length < lines[index].length)
        {
            buffer.append(lines[index][length++]);
        }


        while(!queue.isEmpty() && IsEqual(queue.peek().getWord().toCharArray(), currentTerm))
        {
            head = queue.remove();
            int newIndex = head.getIndex();
            readLine[newIndex] = true;

            int i=0;

            while(i < lines[newIndex].length && lines[newIndex][i++]!=':');

            //i++;

            while(i < lines[newIndex].length)
            {
                buffer.append(lines[newIndex][i++]);
            }


        }

        return buffer;
    }

    public void AddLines() throws IOException
    {
        for(int index = 0; index < fileNumber; index++)
        {
            if(readLine[index] == false || fileAvailable.get(index) == false)
                continue;

            String line = readers[index].readLine();

            if(line == null)
            {
                fileAvailable.put(index, false);
            }
            else
            {
                lines[index] = line.toCharArray();
                readLine[index] = false;

                int i = 0;
                while(lines[index][i++] != ':');

                PQueueNode node = new PQueueNode(lines[index], i-1, index);

                queue.add(node);
            }

        }
    }

    public boolean IsEqual(char[] first, char[] second)
    {
        int firstLength = first.length;
        int secondLength = second.length;

        int i = 0;
        while( i < firstLength && i < secondLength)
        {
            int diff = first[i] - second[i];
            i++;
            if(diff == 0)
                continue;
            else
                return false;
        }

        return firstLength == secondLength;

    }
}
