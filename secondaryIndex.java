import java.io.*;

import static java.util.Arrays.sort;


public class secondaryIndex
{


/*
    public void buildSecondaryIndex(String primaryIndexFileName)
    {
        try
        {

            RandomAccessFile reader=new RandomAccessFile(primaryIndexFileName,"r");

            System.out.println("builded secondary index file");
            File secondaryIndexFile=new File("/media/kedar/New Volume/IRE/46gb/SecondaryIndexFile.txt");
            FileWriter outputFile=new FileWriter(secondaryIndexFile);
            BufferedWriter writer=new BufferedWriter(outputFile);
            String line;

            String []temp={"",""};
            long lengt;

            line=reader.readLine();

            temp=line.split(":");
            writer.write(temp[0]+" 0\n");
            lengt=line.length()+1;

            line=reader.readLine();
            while(line!=null)
            {

                temp=line.split(":");
                writer.write(temp[0]+" "+lengt+"\n");
                lengt+=line.length()+1;

                line=reader.readLine();
            }

            writer.close();
            outputFile.close();

            reader.close();




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

*/


    public void buildSecondaryIndex(String primaryIndexFileName)
    {
        try
        {

            RandomAccessFile reader=new RandomAccessFile(primaryIndexFileName,"r");

            System.out.println("builded secondary index file");
            File secondaryIndexFile=new File("/media/kedar/STUDY/Evaluate/SecondaryIndexFile.txt");
            FileWriter outputFile=new FileWriter(secondaryIndexFile);
            BufferedWriter writer=new BufferedWriter(outputFile);
            String line;

            String temp;
            long lengt;

            line=reader.readLine();

            temp=line.substring(0,line.indexOf(":"));
            writer.write(temp+" 0\n");
            lengt=line.length()+1;

            line=reader.readLine();
            while(line!=null)
            {

                /*temp=line.split(":");
                writer.write(temp[0]+" "+lengt+"\n");*/

                temp=line.substring(0,line.indexOf(":"));
                writer.write(temp+" "+lengt+"\n");

                lengt+=line.length()+1;
                line=reader.readLine();
            }

            writer.close();
            outputFile.close();

            reader.close();




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    public long searchSecondaryIndex(String keyword,RandomAccessFile secondaryIndexFile,long start,long end)
    {

        long mid,offset;

        char inputChar;
        StringBuilder inputLine=new StringBuilder();
        String [] tempKeyword={"",""};

        mid=(start+end)/2;
        try
        {
            secondaryIndexFile.seek(mid);
            offset=mid;

            if(mid<secondaryIndexFile.length())
                inputChar=(char)secondaryIndexFile.readByte();
            else
                return -1;

            while(inputChar!='\n')
            {
                offset--;

                if(offset==0)
                {
                    secondaryIndexFile.seek(offset);
                    inputLine.append(secondaryIndexFile.readLine());

                    tempKeyword=inputLine.toString().split(" ");

                    if(tempKeyword[0].equalsIgnoreCase(keyword))
                        return Long.parseLong(tempKeyword[1].toString());
                }

                if(offset<0)
                    return -1;

                secondaryIndexFile.seek(offset);
                inputChar=(char)secondaryIndexFile.readByte();
            }
            inputLine.append(secondaryIndexFile.readLine());

            tempKeyword=inputLine.toString().split(" ");

            if(tempKeyword[0].equalsIgnoreCase(keyword))
                return Long.parseLong(tempKeyword[1].toString());



            if(((mid-1<start) && tempKeyword[0].compareToIgnoreCase(keyword)<0) || ((mid+1>end) && tempKeyword[0].compareToIgnoreCase(keyword)<0))
            {
                return -1;
            }




            if(tempKeyword[0].compareToIgnoreCase(keyword)>0)
            {
                return searchSecondaryIndex(keyword, secondaryIndexFile, start, mid-1);
            }

            else
            {
                return searchSecondaryIndex(keyword, secondaryIndexFile, mid+1, end);
            }



        }
        catch(Exception e)
        {
            System.out.println("Error in secondary search "+e);

        }

        return -1;
    }

    public String[] searchPrimaryIndex(String keyword, String fileName)
    {
        long offset=0;
        String tempLine;
        int [] docIDs=null;
        int i,lent;
        String [] tempArray;
        String [] idArray=null;
        RandomAccessFile secondaryFile;

        //search in secondaryIndexFile For Offset of keyword
        try
        {
            secondaryFile=new RandomAccessFile("/media/kedar/New Volume/IRE/46gb/SecondaryIndexFile_1.txt","r");
            offset=searchSecondaryIndex(keyword,secondaryFile,0,secondaryFile.length());
            secondaryFile.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        if(offset==-1)
        {

            //System.out.println("");
            return null;
        }

        //keyword found
        try
        {
            RandomAccessFile inputFile=new RandomAccessFile(fileName,"r");

            inputFile.seek(offset);

            tempLine=inputFile.readLine();

            tempArray=tempLine.split(":");

            idArray=tempArray[1].split(",");

           // lent=idArray.length;
//            sort(idArray);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //return docIDs;
        return idArray;
    }


}
