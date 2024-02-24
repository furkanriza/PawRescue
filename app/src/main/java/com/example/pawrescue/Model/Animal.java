package com.example.pawrescue.Model;

public class Animal {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String posterId;
    private String postedByNameAndSurname;
    private String rescuerId;
    private String rescuedByNameAndSurname;

    public Animal() {
    }

    public Animal(String name, String description, String imageUrl, String posterId, String postedByNameAndSurname) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.posterId = posterId;
        this.postedByNameAndSurname = postedByNameAndSurname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getPostedByNameAndSurname() {
        return postedByNameAndSurname;
    }

    public void setPostedByNameAndSurname(String postedByNameAndSurname) {
        this.postedByNameAndSurname = postedByNameAndSurname;
    }

    public String getRescuerId() {
        return rescuerId;
    }

    public void setRescuerId(String rescuerId) {
        this.rescuerId = rescuerId;
    }

    public String getRescuedByNameAndSurname() {
        return rescuedByNameAndSurname;
    }

    public void setRescuedByNameAndSurname(String rescuedByNameAndSurname) {
        this.rescuedByNameAndSurname = rescuedByNameAndSurname;
    }
}
