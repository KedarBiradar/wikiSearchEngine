/**
 * Created with IntelliJ IDEA.
 * User: kedar
 * Date: 12/1/14
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContributorData
{
    String username=null;
    String contributorid=null;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getContributorid()
    {
        return contributorid;
    }

    public void setContributorid(String contributorid)
    {
        this.contributorid = contributorid;
    }

    @Override
    public String toString()
    {
        return "\nUsername"+username+"\nContributor ID="+contributorid+"\n";
    }
}