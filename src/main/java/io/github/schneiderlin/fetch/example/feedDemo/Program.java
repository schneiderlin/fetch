package io.github.schneiderlin.fetch.example.feedDemo;

import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.example.feedDemo.database.BlogTable;
import io.github.schneiderlin.fetch.example.feedDemo.database.Resolver;
import io.github.schneiderlin.fetch.example.feedDemo.database.ReviewTable;
import io.github.schneiderlin.fetch.example.feedDemo.database.UserTable;
import io.github.schneiderlin.fetch.example.feedDemo.request.*;
import io.vavr.collection.List;

public class Program {
    public static void main(String[] args) {
        HomePage homePage = new Program().home("user1");
        System.out.println(homePage);
    }

    public HomePage home(String userId) {
        return runProgram(fetchHome(userId));
    }

    public List<HomePage> homes(List<String> userIds) {
        Fetch<List<HomePage>> program = Fetch.mapM(userIds, this::fetchHome);
        return runProgram(program);
    }

    public <A> A runProgram(Fetch<A> program) {
        return Fetch.runFetch(new Resolver()::resolver, program).performIO();
    }

    public Fetch<HomePage> fetchHome(String userId) {
        return Fetch.appCombine(
                // 然后把查询结果组合起来
                (basicData, mostLikeBlog, mostRecentBlog, otherBlogs) -> {
                    HomePage homePage = new HomePage();
                    homePage.setBasicData(basicData);
                    homePage.setMostLikeBlog(mostLikeBlog);
                    homePage.setMostRecentBlog(mostRecentBlog);
                    homePage.setOtherBlogs(otherBlogs);
                    return homePage;
                },
                // 先查这四个
                fetchBasicData(userId),
                fetchMostLikeBlog(userId),
                fetchMostRecentBlog(userId),
                fetchOtherBlogs(userId)
        );
    }

    public Fetch<HomePage.BasicData> fetchBasicData(String userId) {
        // 根据主键查 user 表
        return userById(userId)
                .map(userTable -> {
                    // 查到 user 表数据之后组装
                    HomePage.BasicData basicData = new HomePage.BasicData();
                    basicData.setId(userTable.getId());
                    basicData.setName(userTable.getName());
                    basicData.setDescription(userTable.getDescription());
                    basicData.setGender(userTable.getGender());
                    basicData.setEmail(userTable.getEmail());
                    return basicData;
                });
    }

    public Fetch<UserTable> userById(String uid) {
        return new UserById(uid).toFetch();
    }

    public Fetch<String> mostLikeBlogIdByUId(String uid) {
        return new MostLikeBlogIdByUId(uid).toFetch();
    }

    public Fetch<HomePage.MostLikeBlogData> fetchMostLikeBlog(String userId) {
        // 查询最多点赞 blog id
        return mostLikeBlogIdByUId(userId)
                .flatMap(mostLikeBlogId -> Fetch.appCombine(
                        (blogTable, likeCount, viewCount, reviews) -> {
                            HomePage.MostLikeBlogData data = new HomePage.MostLikeBlogData();
                            data.setId(blogTable.getId());
                            data.setTitle(blogTable.getTitle());
                            data.setPreview(blogTable.getPreview());
                            data.setView(viewCount);
                            data.setLike(likeCount);
                            data.setReviews(reviews.map(ReviewTable::getReview));
                            return data;
                        },
                        blogById(mostLikeBlogId),
                        blogLikeById(mostLikeBlogId),
                        blogViewById(mostLikeBlogId),
                        reviewsByBlog(mostLikeBlogId)
                ));
    }

    public Fetch<Integer> blogLikeById(String blogId) {
        return new BlogLikeById(blogId).toFetch();
    }

    public Fetch<Integer> blogViewById(String blogId) {
        return new BlogViewById(blogId).toFetch();
    }

    public Fetch<List<ReviewTable>> reviewsByBlog(String blogId) {
        return new ReviewsByBlogId(blogId).toFetch();
    }

    public Fetch<String> mostRecentBlogIdByUId(String userId) {
        return new MostRecentBlogIdByUId(userId).toFetch();
    }

    public Fetch<HomePage.BlogData> fetchMostRecentBlog(String userId) {
        return mostRecentBlogIdByUId(userId)
                .flatMap(mostRecentBlogId -> Fetch.appCombine(
                        (blogTable, likeCount, viewCount) -> {
                            HomePage.BlogData blogData = new HomePage.BlogData();
                            blogData.setId(blogTable.getId());
                            blogData.setTitle(blogTable.getTitle());
                            blogData.setPreview(blogTable.getPreview());
                            blogData.setView(viewCount);
                            blogData.setLike(likeCount);
                            return blogData;
                        },
                        blogById(mostRecentBlogId),
                        blogLikeById(mostRecentBlogId),
                        blogViewById(mostRecentBlogId)
                ));
    }

    public Fetch<List<HomePage.BlogBasicData>> fetchOtherBlogs(String userId) {
        return blogsByUId(userId)
                .map(blogTables -> blogTables
                        .map(blogTable -> {
                            HomePage.BlogBasicData data = new HomePage.BlogBasicData();
                            data.setId(blogTable.getId());
                            data.setTitle(blogTable.getTitle());
                            return data;
                        }));
    }

    public Fetch<List<BlogTable>> blogsByUId(String userId) {
        return blogIdsByUId(userId)
                .flatMap(blogIds -> Fetch.mapM(blogIds, this::blogById));
    }

    public Fetch<List<String>> blogIdsByUId(String userId) {
        return new BlogIdsByUId(userId).toFetch();
    }

    public Fetch<BlogTable> blogById(String blogId) {
        return new BlogById(blogId).toFetch();
    }
}
