import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Vector;

/**
 * User: Kedar Biradar
 * Date: 5/2/14
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class StartSearch
{

    public void parseInput( String [] keywordArray)
    {
        int i,j,k,length;

        Stemmer stemmer=new Stemmer();
        ParseText parseText=new ParseText();
        secondaryIndex secondaryIndex=new secondaryIndex();
        processQuery pq=new processQuery();



        String currentKeyword,stemmedKeyword,temp;
        String [] retrievedList=null;

        Vector<String> tempPostingListAll=new Vector<String>();
        Vector<String> onlyDocIds;

        Vector<String> finalList=new Vector<String>();
        int noOfPostingLists=0;
        int totalSize=1;
    //    Vector<Vector<String>> finalPostingList= new Vector<Vector<String>>();
        char type='b';
        boolean specialtype=false;

        length=keywordArray.length;

        Vector<String> [] tempPostingList=new Vector[length];

        for(i=0;i<length;i++)
        {

            specialtype=false;
            currentKeyword=keywordArray[i];

            if(currentKeyword.indexOf(':')!=-1)
            {
                type=currentKeyword.charAt(0);
                specialtype=true;
                temp=currentKeyword.substring(currentKeyword.indexOf(':')+1,currentKeyword.length());
                currentKeyword=temp;
            }
            stemmer.add(currentKeyword.toCharArray(),currentKeyword.length());
            stemmedKeyword=stemmer.stem();

            retrievedList=secondaryIndex.searchPrimaryIndex(stemmedKeyword,"/media/kedar/New Volume/IRE/46gb/InvertedIndex_1.txt");
            if(retrievedList==null)
            {
                System.out.println("keyword not found");
                continue;
            }
            
            tempPostingList[noOfPostingLists]=new Vector<String>();
            totalSize+=retrievedList.length;

            int count1=0;
            if(specialtype==false)
            {
                count1=0;
                onlyDocIds=new Vector<String>();

                for(k=0;k<retrievedList.length && count1<10 ;k++)
                {
                    tempPostingList[noOfPostingLists].add(retrievedList[k]);
                    onlyDocIds.add(pq.extractDocId(retrievedList[k]));

                    if(i==0)
                        tempPostingListAll.add(pq.extractDocId(retrievedList[k]));

                    count1++;
                }

            }

            else
            {
                count1=0;
                onlyDocIds=new Vector<String>();

                for(k=0;k<retrievedList.length && count1<10 ; k++)
                {
                    if(retrievedList[k].indexOf(type)!=-1)
                    {
                        tempPostingList[noOfPostingLists].add(retrievedList[k]);
                        onlyDocIds.add(pq.extractDocId(retrievedList[k]));
                        count1++;

                        if(i==0)
                            tempPostingListAll.add(pq.extractDocId(retrievedList[k]));
                    }
                }

                if(tempPostingList[noOfPostingLists].size()==0)
                {
                    System.out.println("Keyword not found");
                    continue;
                }
            }

            tempPostingListAll.retainAll(onlyDocIds);

            noOfPostingLists++;
        }

        int m,l,n;
        String currentDocId,tempDocId;
        int [] count=new int[tempPostingListAll.size()];

        double idf;
        int tempPostListsize,tempPostListAllSize;

        int N=14041179;
        /*
        for(m=0;m<noOfPostingLists;m++)
            totalSize+=tempPostingList[m].size();
          */
        idf=Math.log10(N/totalSize);

        tempPostListAllSize=tempPostingListAll.size();

		l=0;
		if(length>1)
		{
			for(l=0;l<tempPostListAllSize && l < 10 ;l++)
			{
				currentDocId=tempPostingListAll.elementAt(l);
				for(m=0;m<noOfPostingLists;m++)
				{
					tempPostListsize=tempPostingList[m].size();
					for(n=0;n<tempPostListsize;n++)
					{
						tempDocId=pq.extractDocId(tempPostingList[m].elementAt(n));

						if(currentDocId.equalsIgnoreCase(tempDocId))
						{
						  count[l]+=pq.countTF(tempPostingList[m].elementAt(n)) ;
						}
					}
				}
				finalList.add(currentDocId+"b"+count[l]);
			}
		}
		else
		{
				finalList=tempPostingListAll;
		}

		int [] index=new int[noOfPostingLists];
		
               if(length>1 && l < 10)
               {
                for(m=0;m<noOfPostingLists;m++)
                {
                    tempPostListsize=tempPostingList[m].size();

                    for(n=0;n<tempPostListsize;n++)
                    {
                        finalList.add(tempPostingList[m].elementAt(n));
                        l++;
                        if(l>=10)
                            break;
                    }
                    if(l>=10)
                        break;
                }
               }

          pq.rank(finalList,idf);

    }

    public String [] takeInput()
    {
        //int t;
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        //StringBuilder temWord=new StringBuilder();
        String [] keywordArray=null;
        String inputLine;

        try
        {
                inputLine=reader.readLine();
                keywordArray=inputLine.split(" ");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return keywordArray;
    }

    public void startSearch()
    {
        String [] keywordArray;
        int k,t=0;

        try
        {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter no of query");
            t=Integer.parseInt(br.readLine());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        for(k=0;k<t;k++)
        {
            keywordArray=takeInput();
            long startTime=System.currentTimeMillis();
            parseInput(keywordArray);
            long stopTime=System.currentTimeMillis();

            System.out.println("Time required="+ (stopTime-startTime)/1000f) ;

        }
    }
}
