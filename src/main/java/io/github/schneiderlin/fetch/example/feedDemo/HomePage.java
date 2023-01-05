package io.github.schneiderlin.fetch.example.feedDemo;

import io.vavr.collection.List;
import lombok.Data;


@Data
public class HomePage {
    // 人的基础信息
    private BasicData basicData;

    // 最热门 blog
    private MostLikeBlogData mostLikeBlog;
    // 最新 blog
    private BlogData mostRecentBlog;

    // 其他文章
    private List<BlogBasicData> otherBlogs;

    @Data
    public static class BasicData {
        private String id;
        private String name;
        private String description;
        private String gender;
        private String email;
    }

    @Data
    public static class MostLikeBlogData {
        private String id;
        private String title;
        private String preview;
        private Integer view;
        private Integer like;
        private List<String> reviews;
    }

    @Data
    public static class BlogData {
        private String id;
        private String title;
        private String preview;
        private Integer view;
        private Integer like;
    }

    @Data
    public static class BlogBasicData {
        private String id;
        private String title;
    }
}
