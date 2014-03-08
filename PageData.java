/**
 * Created with IntelliJ IDEA.
 * User: kedar
 * Date: 12/1/14
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class PageData
{
    String title=null;
    String pageid=null;
    String text=null;

    RevisionData revData=null;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title.toLowerCase();
    }

    public String getPageid()
    {
        return pageid;
    }

    public void setPageid(String pageid)
    {
        this.pageid = pageid.toLowerCase();
        pageid=pageid.replaceAll("\\r|\\n","");
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text.toLowerCase();
    }
    /*

        public RevisionData getRevData()
        {
            if(revData==null)
                revData=new RevisionData();

            return revData;
        }

        public void setRevData(RevisionData revData)
        {
            this.revData = revData;
        }
      */
    @Override
    public String toString()
    {
        //    return super.toString();    //To change body of overridden methods use File | Settings | File Templates.
        return "Page Title="+title+"\n Page ID="+pageid+"\n"+"Text="+text;

    }
}