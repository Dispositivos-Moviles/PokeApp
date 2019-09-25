package com.nickgonzalezs.todolist.model;

class Ability {
    private String name;
    private String url;

    public Ability(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
