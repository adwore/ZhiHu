package com.computer.mazhihuapp.model;

import java.util.List;

/**
 * Created by computer on 2015/9/22.
 */
public class Content {
    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private List<String> css;
    private int id;
    private String ga_prefix;
    private int type;
    private List<RecommendersEntiny> recommenders;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<RecommendersEntiny> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<RecommendersEntiny> recommenders) {
        this.recommenders = recommenders;
    }

    public static class RecommendersEntiny{
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
