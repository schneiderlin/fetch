package io.github.schneiderlin.fetch.example.feedDemo.database;

import io.github.schneiderlin.fetch.BlockedRequest;
import io.github.schneiderlin.fetch.IORef;
import io.github.schneiderlin.fetch.example.feedDemo.request.*;
import io.github.schneiderlin.fetch.io.IO;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Resolver {
    private Map<String, BlogTable> getBlogByIds(List<String> blogIds) {
        if (blogIds.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getBlogByIds: " + blogIds);
        return blogIds.toMap(Function.identity(), blogId -> {
            BlogTable blogTable = new BlogTable();
            blogTable.setId(blogId);
            blogTable.setUid("user1");
            blogTable.setTitle("title");
            blogTable.setPreview("preview");
            blogTable.setPublishTime(LocalDateTime.now());
            return blogTable;
        });
    }

    private Map<String, List<String>> getBlogIdsByUIds(List<String> uids) {
        if (uids.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getBlogIdsByUIds: " + uids);

        return uids.toMap(Function.identity(), uid ->
                List.of("blog1", "blog2", "blog3", "blog4"));
    }

    private Map<String, Integer> getBlogLikeByIds(List<String> ids) {
        if (ids.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getBlogLikeByIds: " + ids);
        return ids.toMap(Function.identity(), id -> new Random().nextInt());
    }

    private Map<String, Integer> getBlogViewByIds(List<String> ids) {
        if (ids.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getBlogViewByIds: " + ids);
        return ids.toMap(Function.identity(), id -> new Random().nextInt());
    }

    private Map<String, String> getMostLikeBlogIdByUIds(List<String> uids) {
        if (uids.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getMostLikeBlogIdByUIds: " + uids);
        return uids.toMap(Function.identity(), uid -> "blog1");
    }

    private Map<String, String> getMostRecentBlogIdByUIds(List<String> uids) {
        if (uids.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getMostRecentBlogIdByUIds: " + uids);
        return uids.toMap(Function.identity(), uid -> "blog2");
    }

    private Map<String, List<ReviewTable>> getReviewsByBlogIds(List<String> blogIds) {
        if (blogIds.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getReviewsByBlogIds: " + blogIds);
        return blogIds.toMap(Function.identity(), blogId -> {
            ReviewTable review1 = new ReviewTable();
            review1.setUserId("user2");
            review1.setBlogId(blogId);
            review1.setReview("垃圾 blog");
            ReviewTable review2 = new ReviewTable();
            review2.setUserId("user3");
            review2.setBlogId(blogId);
            review2.setReview("好好好");
            return List.of(review1, review2);
        });
    }

    private Map<String, UserTable> getUserByIds(List<String> uids) {
        if (uids.isEmpty()) {
            return HashMap.empty();
        }

        System.out.println("getUserByIds: " + uids);
        return uids.toMap(Function.identity(), uid -> {
            UserTable userTable = new UserTable();
            userTable.setId(uid);
            userTable.setName("name");
            userTable.setDescription("description");
            userTable.setGender("gender");
            userTable.setEmail("email");
            return userTable;
        });
    }

    public IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        // batch 所有的同类请求
//        List<String> blogByIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "BlogById"))
//                .map(request -> (String) request.request.getId());
//        List<String> blogIdsByUIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "BlogIdsByUId"))
//                .map(request -> (String) request.request.getId());
//        List<String> blogLikeByIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "BlogLikeById"))
//                .map(request -> (String) request.request.getId());
//        List<String> blogViewByIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "BlogViewById"))
//                .map(request -> (String) request.request.getId());
//        List<String> mostLikeBlogIdByUIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "MostLikeBlogIdByUId"))
//                .map(request -> (String) request.request.getId());
//        List<String> mostRecentBlogIdByUIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "MostRecentBlogIdByUId"))
//                .map(request -> (String) request.request.getId());
//        List<String> reviewsByBlogIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "ReviewsByBlogId"))
//                .map(request -> (String) request.request.getId());
//        List<String> userByIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getTag(), "UserById"))
//                .map(request -> (String) request.request.getId());
//
//        // 实际数据库查询
//        Map<String, BlogTable> blogByIdsMap = getBlogByIds(blogByIds);
//        Map<String, List<String>> blogIdsByUIdsMap = getBlogIdsByUIds(blogIdsByUIds);
//        Map<String, Integer> blogLikeByIdsMap = getBlogLikeByIds(blogLikeByIds);
//        Map<String, Integer> blogViewByIdsMap = getBlogViewByIds(blogViewByIds);
//        Map<String, String> mostLikeBlogIdByUIdsMap = getMostLikeBlogIdByUIds(mostLikeBlogIdByUIds);
//        Map<String, String> mostRecentBlogIdByUIdsMap = getMostRecentBlogIdByUIds(mostRecentBlogIdByUIds);
//        Map<String, List<ReviewTable>> reviewsByBlogIdsMap = getReviewsByBlogIds(reviewsByBlogIds);
//        Map<String, UserTable> userByIdsMap = getUserByIds(userByIds);
//
//        return IO
//                .sequence(blockedRequests.map(request -> {
//                    if (request.request.getClass() == BlogById.class) {
//                        return IORef.writeIORef(request.result, blogByIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == BlogIdsByUId.class) {
//                        return IORef.writeIORef(request.result, blogIdsByUIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == BlogLikeById.class) {
//                        return IORef.writeIORef(request.result, blogLikeByIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == BlogViewById.class) {
//                        return IORef.writeIORef(request.result, blogViewByIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == MostLikeBlogIdByUId.class) {
//                        return IORef.writeIORef(request.result, mostLikeBlogIdByUIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == MostRecentBlogIdByUId.class) {
//                        return IORef.writeIORef(request.result, mostRecentBlogIdByUIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == ReviewsByBlogId.class) {
//                        return IORef.writeIORef(request.result, reviewsByBlogIdsMap.get((String) request.request.getId()).get());
//                    } else if (request.request.getClass() == UserById.class) {
//                        return IORef.writeIORef(request.result, userByIdsMap.get((String) request.request.getId()).get());
//                    }
//
//                    throw new RuntimeException("no resolver");
//                }))
//                .andThen(IO.noop());
        return null;
    }
}
