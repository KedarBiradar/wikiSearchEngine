/**
 * Created with IntelliJ IDEA.
 * User: kedar
 * Date: 12/1/14
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class RevisionData
{
    String revisionid=null;
    String timeStamp=null;
    ContributorData conData=null;
    String minor;
    String comment;
    String text;

    public String getRevisionid()
    {
        return revisionid;
    }

    public void setRevisionid(String revisionid)
    {
        this.revisionid = revisionid;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public ContributorData getConData()
    {
        if(conData==null)
            conData=new ContributorData();

        return conData;
    }

    public void setConData(ContributorData conData)
    {
        this.conData = conData;
    }

    public String getMinor()
    {
        return minor;
    }

    public void setMinor(String minor)
    {
        this.minor = minor;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Revision ID="+revisionid+"\n TimeStamp="+timeStamp+"\n"+conData+"\n Minor="+minor+"\n Comment="+comment+"\n Text="+text;
    }
}