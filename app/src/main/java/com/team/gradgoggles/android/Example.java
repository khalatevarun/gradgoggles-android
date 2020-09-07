package com.team.gradgoggles.android;

import com.squareup.moshi.Json;

import java.util.List;

public class Example {

    @Json(name = "id")
    private Integer id;
    @Json(name = "scraps")
    private List<Scrap> scraps = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Scrap> getScraps() {
        return scraps;
    }

    public void setScraps(List<Scrap> scraps) {
        this.scraps = scraps;
    }

}