package com.computer.mazhihuapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by computer on 2015/9/17.
 */
public class Latest {

    public static class stories implements Serializable{
        private int id;
        private String title;
        private String ga_prefix;
        private List<String> images;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String toString (){
            return "stories{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                    ", images=" + images +
                    ", type=" + type +
                    '}';
        }
    }

    public static class top_stories{
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String toString(){
            return "top_stories{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", image='" + image + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    private List<stories> stories;
    private List<top_stories> top_stories;
    private String date;

    public List<Latest.stories> getStories() {
        return stories;
    }

    public void setStories(List<Latest.stories> stories) {
        this.stories = stories;
    }

    public List<Latest.top_stories> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Latest.top_stories> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
