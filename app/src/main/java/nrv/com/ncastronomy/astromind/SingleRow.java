package nrv.com.ncastronomy.astromind;

/**
 * Created by NRV on 8/2/2015.
 */
class SingleRow{
    int typeofpost;//0-message 1-story
    String id;
    String title;
    int likes;
    String description;
    String imagelink;
    int iconimglink;
    String pubdate;
    String link;
    public SingleRow(String title, String description,String img,int iconimg,String pid,int type,String dat_pub,int lks,String lnk) {

        this.title = title;
        this.description = description;
        this.imagelink=img;
        this.iconimglink=iconimg;
        this.id=pid;
        this.typeofpost=type;
        this.pubdate=dat_pub.split("T")[0];
        this.likes=lks;
        this.link=lnk;
    }
    public String getLike(){
        if (this.likes!=0) {
            return "Likes:-" + this.likes;
        }
        else {
            return "Do the first like";
        }
    }
}
