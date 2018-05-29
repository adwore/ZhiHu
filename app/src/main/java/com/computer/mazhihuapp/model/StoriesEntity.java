package com.computer.mazhihuapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by computer on 2015/9/25.
 */
public class StoriesEntity implements Serializable {


    /**
     * images : ["http://pic3.zhimg.com/956663ab7cf64103a5a08d567db3f1c6_t.jpg"]
     * type : 0
     * id : 7121745
     * title : 前《永恒之塔》技术总监谈如何使用Unity实现次世代效果
     */

    private int type;
    private int id;
    private String title;
    private List<String> images;

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getImages() {
        return images;
    }
}
