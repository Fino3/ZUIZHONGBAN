package com.example.listview;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

public class FeedBack {
    @JSONField(ordinal = 0)
    private long id;  //无需指定
    @JSONField(ordinal = 1)
    private String imageUrl=""; //图片url
    @JSONField(ordinal = 2)
    private String title="";   //标题
    @JSONField(ordinal = 3)
    private String desc="";  //描述
    @JSONField(ordinal = 4)
    private String account="";  //账号
    @JSONField(ordinal = 5)
    private String address=""; //地址，定位
    @JSONField(ordinal = 6)
    private String category="无"; //类别：安全隐患、卫生问题、秩序问题
    @JSONField(ordinal = 7)
    private int degree;  //级别：0-一般，  1-重要
    @JSONField(ordinal = 8)
    private Date time=new Date();  //时间: 2021-11-06T13:14:25.909+00:00
    @JSONField(ordinal = 9)
    private String process="已提交"; //当前状态："已提交"

    public FeedBack() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }


}
