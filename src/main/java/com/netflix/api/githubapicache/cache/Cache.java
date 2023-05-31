package com.netflix.api.githubapicache.cache;

import com.netflix.api.githubapicache.github.RepoDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Cache<T> {
    private final List<T> cache;

    public Cache() {
        this.cache = new ArrayList<>();
    }

    public Cache(List<T> list) {
        this.cache = new ArrayList<>(list);
    }


    public void put(T value) {
        cache.add(value);
    }

    public List<T> getCache() {
        return cache;
    }

    public void clear() {
        cache.clear();
    }
}

