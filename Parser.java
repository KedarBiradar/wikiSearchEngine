//package wikiMini;

/**
 * Created with IntelliJ IDEA.
 * User: kedar
 * Date: 12/1/14
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 *
 */

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Stack;

public class Parser
{

    static int pagecount,fileNo=0;

    public static void parseXML(String xmlfileName,String indexFile)
    {

        MergeSortedFiles msi=new MergeSortedFiles();
        try
        {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser my_parser = factory.newSAXParser();
            final createIndex ci=new createIndex();
            final String indexFileName=indexFile;


            File titleFile=new File("/media/kedar/STUDY/Evaluate/TitleFile.txt");
            FileWriter outputFile=new FileWriter(titleFile);
            final BufferedWriter writer=new BufferedWriter(outputFile);



            DefaultHandler handler = new DefaultHandler()
            {
                StringBuffer tagContent=new StringBuffer();
                Stack <String> tagStack=new Stack<String>();


                String tagName=new String();
                String parentTagName=new String();
                int totalPageCount=0;
                StringBuilder titleInfo=new StringBuilder();


                PageData pagedata=null;
                ParseText parsetext=new ParseText();





                public void startElement(String uri, String localName,String qName,Attributes attributes) throws SAXException
                {
                    tagStack.push(qName);

                    if(qName.equalsIgnoreCase("page"))
                        pagedata=new PageData();

                }

                public void endElement(String uri, String localName,String qName) throws SAXException
                {


                    if(!tagStack.isEmpty())
                    {
                        tagName=tagStack.pop();

                        if(!tagStack.isEmpty())
                            parentTagName=tagStack.peek();
                    }
                    if(tagName.equalsIgnoreCase("id"))
                    {
                        if(parentTagName.equalsIgnoreCase("page"))
                        {

                            pagedata.setPageid(tagContent.toString());
                        }

                          /*
                           else if(parentTagName.equalsIgnoreCase("revision"))
                           {
                              pagedata.getRevData().setRevisionid(tagContent.toString());
                           }
                           else
                           {
                                pagedata.getRevData().getConData().setContributorid(tagContent.toString());
                           }
                           */
                    }

                    if(tagName.equalsIgnoreCase("title"))
                    {
                        pagedata.setTitle(new String(tagContent));
                    }

                    if(tagName.equalsIgnoreCase("text"))
                    {
                        pagedata.setText(new String(tagContent));
                    }

                        /*
                        else if(tagName.equalsIgnoreCase("timestamp"))
                        {
                             pagedata.getRevData().setTimeStamp(tagContent.toString());
                        }

                        else if(tagName.equalsIgnoreCase("username"))
                        {
                            pagedata.getRevData().getConData().setUsername(tagContent.toString());
                        }

                        else if(tagName.equalsIgnoreCase("minor"))
                        {
                            pagedata.getRevData().setMinor(tagContent.toString());
                        }

                        else if(tagName.equalsIgnoreCase("comment"))
                        {
                            pagedata.getRevData().setComment(tagContent.toString());
                        }

                        else if(tagName.equalsIgnoreCase("text"))
                        {
                            pagedata.getRevData().setText(tagContent.toString());
                        }

                        */

                    tagContent.delete(0,tagContent.length());

                    if(tagName.equalsIgnoreCase("page"))
                    {
                        //titleInfo.append(pagedata.pageid.trim()+":"+pagedata.title.trim()+"\n");


                        try
                        {
                            writer.write(pagedata.pageid.trim()+"#"+pagedata.title.trim()+"\n");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        parsetext.makeWord(pagedata.title,pagedata.pageid,ci,"title");
                        parsetext.makeWord(pagedata.text,pagedata.pageid,ci,"body");
                        pagecount++;
                        totalPageCount++;

			
			    //System.out.println(totalPageCount);
                        if(pagecount>1500)
                        {
                            pagecount=0;
                            ci.dumpToFile(fileNo);
                           ci.wordTree.clear();
                            fileNo++;
                            System.out.println("Dumped fileno=:"+fileNo);

                            /*
                            try
                            {
                                writer.write(titleInfo.toString());
                                titleInfo.setLength(0);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            */
                        }
                       // System.out.println("Processing page: "+totalPageCount);

                    }

                    if(tagName.equalsIgnoreCase("file"))
                    {
                        pagecount=0;
                        ci.dumpToFile(fileNo);
                        ci.wordTree.clear();
                        fileNo++;
                        System.out.println("Total numbers of temporary files="+fileNo);
                        System.out.println("total page count="+totalPageCount);
                      //  msi.MergeFiles(fileNo);
                    }
                   // System.out.println("Total numbers of temporary files="+fileNo);
                  // msi.MergeFiles(fileNo);
                }

                public void characters(char ch[], int start, int length) throws SAXException
                {
                    String tmp=new String(ch,start,length);
                    tagContent.append(tmp);
                }
            };

            my_parser.parse(xmlfileName, handler);

            writer.close();


	
		 long startTime3 = System.currentTimeMillis();
	
            msi.MergeFiles(fileNo);

		 long stopTime3 = System.currentTimeMillis();
        System.out.println("Time required for merging="+ (stopTime3-startTime3)/1000f);


        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
    }
}
