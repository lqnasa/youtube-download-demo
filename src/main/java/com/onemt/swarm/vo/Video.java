package com.onemt.swarm.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/11.
 * <p>
 * itag 17
 * url https://r6---sn-i3belnez.googlevideo.com/videoplayback?initcwndbps=1311250&sparams=clen,dur,ei,gir,id,initcwndbps,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,pl,requiressl,source,expire&requiressl=yes&clen=1021318&mime=video/3gpp&ipbits=0&expire=1515669004&pl=19&gir=yes&ip=47.52.28.90&key=yt6&mn=sn-i3belnez&signature=8384912DF2C1E387F6052F8DE67AE70F27548C0E.811C9872B0BB6FB61939FA64802947EB57E8C2DE&mm=31&ms=au&id=o-AMoki1Eyh1F_dsZWXcYoB7ijxBTDmqbANi9i3srfNCST&ei=rPFWWvG8FIzA4gLAnpzwDw&mv=m&mt=1515647297&itag=17&dur=104.582&lmt=1487352277371013&source=youtube
 * type video/3gpp; codecs="mp4v.20.3, mp4a.40.2"
 * quality small
 */
public class Video {

    private String url;
    private String type;
    private String quality;
    private String itag;
    private String clen;

    public Video() {
    }

    public Video(String url, String type, String quality, String itag, String clen) {
        this.url = url;
        this.type = type;
        this.quality = quality;
        this.itag = itag;
        this.clen = clen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getItag() {
        return itag;
    }

    public void setItag(String itag) {
        this.itag = itag;
    }

    public String getClen() {
        return clen;
    }

    public void setClen(String clen) {
        this.clen = clen;
    }
}
