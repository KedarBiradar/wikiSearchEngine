import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import static java.lang.Math.round;

/**
 * User: Kedar Biradar
 * Date: 31/1/14
 * Time: 6:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class processQuery
{

    public void printTitle(Vector<String> resultList)
    {
        String fileName="/media/kedar/New Volume/IRE/46gb/TitleFile.txt";
        int i,length;
        //String docId,line;
        long offset;
        RandomAccessFile secondaryFile;
        //docId=null;

        //search in secondaryIndexFile For Offset of keyword
        try
        {
            secondaryFile=new RandomAccessFile(fileName,"r");
            for( String docId:resultList)
            {
                System.out.println(docId+" "+searchTitle(docId, secondaryFile, 0, secondaryFile.length()));
            }
            secondaryFile.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }



    public String searchTitle(String word,RandomAccessFile raf,long start,long end)
    {

        int docId=Integer.parseInt(word);
        long mid;
        String res;
        try {
            while(start<=end)
            {
                mid=(start+end)/2;
               // System.out.println(start+"       "+end+"          "+mid);
                raf.seek(mid);
                raf.readLine();
                if((res=raf.readLine())!=null)
                {
                    //System.out.println(res);
                    String[] arr=res.split("#");

                    int comparison=Integer.parseInt(arr[0]);
                    if(comparison==docId)
                    {
                        return arr[1];
                    }
                    else if(comparison<docId)
                    {
                        start=mid+1;//indexReader.getFilePointer();
                    }
                    else
                    {
                        end=mid-1;
                    }
                }
                else
                {
                    end=mid-1;
                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return "Title not found";

    }

    public void rank(Vector<String> searchResult,double idf)
    {
        TreeMap<Double,Vector<String>> weightedList= new TreeMap<Double, Vector<String>>(Collections.reverseOrder());
        int i,lengthOfSearchResult;
        double weight=0,IDF;

        lengthOfSearchResult=searchResult.size();
        String docId;

        IDF=idf;

        for(i=0;i<lengthOfSearchResult;i++)
        {
            docId=extractDocId(searchResult.elementAt(i));
            weight=countTF(searchResult.elementAt(i))*IDF;
            Vector<String>docList=weightedList.get(weight);
            if(docList==null)
            {
                docList=new Vector<String>();
            }


                docList.add(docId);

            weightedList.put(weight,docList);

        }

        Vector<String> tempList,totaldocIds;

        totaldocIds=new Vector<String>();
        for(Double keys:weightedList.keySet())
        {

            tempList=weightedList.get(keys);

            for(String keyword:tempList)
            {

                totaldocIds.add(keyword);
                /*
                System.out.print(keyword+"  ");
                printTitle(keyword);
                */
            }
        }
        printTitle(totaldocIds);
    }



    public void newRank(Vector<Vector<String>> searchResult)
    {
        int i,j,k,searchResultLength,currentListLength;
        double weight;


        Vector<String> currentList=new Vector<String>();

        HashMap<String,Double> weightedList=new HashMap<String, Double>();
        String docID;


        searchResultLength=searchResult.size();

        double [] IDF= new double[searchResultLength];


        for(i=0;i<searchResultLength;i++)
        {
            IDF[i]=countIDF(searchResult.elementAt(i));
        }

        for(i=0;i<searchResultLength;i++)
        {
            currentList=searchResult.elementAt(i);
            currentListLength=currentList.size();

            for(j=0;j<currentListLength;j++)
            {
                docID=extractDocId(currentList.elementAt(j));

                weight=countTF(currentList.elementAt(j))*IDF[i];

                if(weightedList.containsKey(docID))
                {
                    weightedList.put(docID,weightedList.get(docID)+weight);
                }
                else
                    weightedList.put(docID,weight);
            }
        }

        String tempString;
        double tempWeight;
        Vector<String> docList;

        TreeMap<Double,Vector<String>> finalWeightList=new TreeMap<Double, Vector<String>>(Collections.reverseOrder());
        {
            for(String docIds:weightedList.keySet())
            {
                tempWeight=weightedList.get(docIds);

                docList=finalWeightList.get(tempWeight);
                if(docList==null)
                {
                    docList=new Vector<String>();
                }
                docList.add(docIds);
                finalWeightList.put(tempWeight,docList);

            }
        }

        k=1;

        for(Double W:finalWeightList.keySet())
        {
            docList=finalWeightList.get(W);

            for(String key:docList)
            {
                System.out.print(key+",");
                k++;

                if(k>10)
                    break;
            }
            System.out.println(" Weight="+W);

            if(k>10)
                break;
        }

    }


    public String extractDocId(String docId)
    {
        String tempId;
        int [] index=new int[5];
        int i,endIndex;

        index[0]=docId.indexOf("t");
        index[1]=docId.indexOf("b");
        index[2]=docId.indexOf("i");
        index[3]=docId.indexOf("c");
        index[4]=docId.indexOf("l");


        endIndex=docId.length();

        for(i=0;i<5;i++)
            if(endIndex>index[i] && index[i]!=-1)
                endIndex=index[i];

        //endIndex--;

        tempId=docId.substring(0,endIndex);


        return tempId;

    }

    public int countTF(String searchResult)
    {
        int finalCount=0;
        int length,tempKeywordLength;
        String tempKeyword;

        int [] index=new int[5];
        int i,startIndex;

            tempKeyword=searchResult;
            tempKeywordLength=tempKeyword.length();


                index[0]=tempKeyword.indexOf("t");
                index[1]=tempKeyword.indexOf("b");
                index[2]=tempKeyword.indexOf("i");
                index[3]=tempKeyword.indexOf("c");
                index[4]=tempKeyword.indexOf("l");


                startIndex=index[0];

                for(i=1;i<5;i++)
                    if(startIndex<index[i])
                        startIndex=index[i];

                startIndex++;

                finalCount=Integer.parseInt(tempKeyword.substring(startIndex,tempKeywordLength));

        return finalCount;
    }

    public double countIDF(Vector<String> searchResult)
    {
        int N=103417;                        //No of documents(pages)

        int x;
        x=N/searchResult.size();
        double temp=Math.log10(x);

        String num=String.format("%.2f",temp);

        return Double.parseDouble(num);

    }



    public int countTotal(Vector<String> searchResult,char keywordType)
    {
        int finalCount=0;
        int length,tempKeywordLength;
        String tempKeyword;
        StringBuilder tempCount=new StringBuilder();

        length=searchResult.size();
        int [] index=new int[5];
        int i,startIndex;


        for(int k=0;k<length;k++)
        {

            tempKeyword=searchResult.elementAt(k);
            tempKeywordLength=tempKeyword.length();



            if(tempKeyword.indexOf(keywordType)!=-1)
            {

                index[0]=tempKeyword.indexOf("t");
                index[1]=tempKeyword.indexOf("b");
                index[2]=tempKeyword.indexOf("i");
                index[3]=tempKeyword.indexOf("c");
                index[4]=tempKeyword.indexOf("l");

                startIndex=index[0];

                for(i=1;i<5;i++)
                    if(startIndex<index[i])
                        startIndex=index[i];

                startIndex++;

                finalCount+=Integer.parseInt(tempKeyword.substring(startIndex,tempKeywordLength));

            }
        }

        return finalCount;
    }
}
