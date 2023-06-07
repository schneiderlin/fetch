package io.github.schneiderlin.fetch.blogDemo;

import io.github.schneiderlin.fetch.blogDemo.model.PostContent;
import io.github.schneiderlin.fetch.blogDemo.model.PostInfo;
import io.vavr.collection.List;

import java.time.LocalDateTime;
import java.util.Map;

public class Database {
    private static final List<PostInfo> postInfos = List.of(
            new PostInfo(1, LocalDateTime.now(), "topic"),
            new PostInfo(2, LocalDateTime.now(), "topic"),
            new PostInfo(3, LocalDateTime.now(), "topic")
    );
    private static final List<PostContent> postContents = List.of(
            new PostContent(1, "content"),
            new PostContent(2, "content"),
            new PostContent(3, "content")
    );
    private static final Map<Long, Integer> viewCount = Map.of();

    public static List<Long> getPostIds() {
        System.out.println("call getPostIds");
        return List.of(1L, 2L, 3L);
    }

    public static PostContent getContentById(long id) {
        System.out.println("call getContentById: " + id);
        return postContents.find(content -> content.id == id).get();
    }

    public static List<PostContent> getContentByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.empty();
        }
        System.out.println("call getContentByIds: " + ids);
        return postContents.filter(postContent -> ids.contains(postContent.id));
    }

    public static PostInfo getInfoById(long id) {
        System.out.println("call getInfoById: " + id);
        return postInfos.find(info -> info.id == id).get();
    }

    public static List<PostInfo> getInfoByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.empty();
        }
        System.out.println("call getInfoByIds: " + ids);
        return postInfos.filter(info -> ids.contains(info.id));
    }
}
