package vn.spring.WOW.strategieseditor.core;


public class ShowLink
{

    private String linkTo;
    private String comment;

    public ShowLink(String s, String s1)
    {
        linkTo = s;
        comment = s1;
    }

    public String toString()
    {
        return linkTo;
    }

    public String getLinkTo()
    {
        return linkTo;
    }

    public String getComment()
    {
        return comment;
    }
}
