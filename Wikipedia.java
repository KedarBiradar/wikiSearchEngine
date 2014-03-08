//package wikiMini;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: kedar
 * Date: 12/1/14
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Wikipedia
{
    public static void main(String [] args)
    {

       // long startTime=0;
        String fileName=null;
        Parser p1=new Parser();
        ParseText pt=new ParseText();
        secondaryIndex si=new secondaryIndex();
        StartSearch startSearch=new StartSearch();
        MergeSortedFiles msi=new MergeSortedFiles();



        long startTime = System.currentTimeMillis();

        pt.buildStopwordDict("StopwordList.txt");


        //args[1].concat("/InvertedIndex.txt");


        //p1.parseXML("/media/kedar/STUDY/Evaluate/evaluate.xml","/media/kedar/STUDY/Evaluate/InvertedIndex.txt");

	
        long stopTime = System.currentTimeMillis();
        System.out.println("parsing Time required="+ (stopTime-startTime)/1000f);



	
		 long startTime1 = System.currentTimeMillis();

		
		System.out.println("Started secondary index");
		
			// msi.MergeFiles(13);	
			//si.buildSecondaryIndex("/media/kedar/STUDY/Evaluate/InvertedIndex.txt");
			
		 long stopTime1 = System.currentTimeMillis();
        System.out.println("Time required for merging="+ (stopTime-startTime)/1000f);


        
           startSearch.startSearch();

    }
}
