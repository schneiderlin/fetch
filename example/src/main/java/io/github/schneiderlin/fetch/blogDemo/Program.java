package io.github.schneiderlin.fetch.blogDemo;

import com.google.gson.Gson;
import io.github.schneiderlin.fetch.BlockedRequest;
import io.github.schneiderlin.fetch.Fetch;
import io.github.schneiderlin.fetch.IORef;
import io.github.schneiderlin.fetch.blogDemo.model.PostContent;
import io.github.schneiderlin.fetch.blogDemo.model.PostInfo;
import io.github.schneiderlin.fetch.blogDemo.request.FetchPostContent;
import io.github.schneiderlin.fetch.blogDemo.request.FetchPostInfo;
import io.github.schneiderlin.fetch.blogDemo.request.FetchPosts;
import io.github.schneiderlin.fetch.io.IO;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.util.stream.Collectors;

import static io.github.schneiderlin.fetch.Fetch.*;

public class Program {

    public static void main(String[] args) {
        Fetch<String> blog = appCombine(Program::renderPage, leftPane(), mainPane());
        IO<String> blogString = runFetch(Program::resolver, blog);
        String s = blogString.performIO();
        System.out.println(s);
    }

    private static final Gson gson = new Gson();

    private static IO<Void> resolver(List<BlockedRequest<Object, Object>> blockedRequests) {
        // batch 所有的同类请求
//        List<Long> getInfoPIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getClass().getCanonicalName(), FetchPostInfo.class.getCanonicalName()))
//                .map(request -> {
//                    String json = gson.toJson(request.request);
//                    FetchPostInfo fetchPostInfo = gson.fromJson(json, FetchPostInfo.class);
//                    return fetchPostInfo.postId;
//                });
//
//        List<Long> getContentPIds = blockedRequests
//                .filter(request -> Objects.equals(request.request.getClass().getCanonicalName(), FetchPostContent.class.getCanonicalName()))
//                .map(request -> {
//                    String json = gson.toJson(request.request);
//                    FetchPostContent fetchPostContent = gson.fromJson(json, FetchPostContent.class);
//                    return fetchPostContent.postId;
//                });
//
//        // 实际数据库查询
//        Map<Long, PostInfo> postInfos = Database.getInfoByIds(getInfoPIds)
//                .toMap(info -> new Tuple2<>(info.id, info));
//        Map<Long, PostContent> postContents = Database.getContentByIds(getContentPIds)
//                .toMap(content -> new Tuple2<>(content.id, content));
//
//        return IO
//                .sequence(blockedRequests.map(request -> {
//                    if (request.request.getClass() == FetchPosts.class) {
//                        return IORef.writeIORef(request.result, Database.getPostIds());
//                    } else if (request.request.getClass() == FetchPostInfo.class) {
//                        String json = gson.toJson(request.request);
//                        FetchPostInfo fetchPostInfo = gson.fromJson(json, FetchPostInfo.class);
//                        return IORef.writeIORef(request.result, postInfos.get(fetchPostInfo.postId).get());
//                    } else if (request.request.getClass() == FetchPostContent.class) {
//                        String json = gson.toJson(request.request);
//                        FetchPostContent fetchPostContent = gson.fromJson(json, FetchPostContent.class);
//                        return IORef.writeIORef(request.result, postContents.get(fetchPostContent.postId).get());
//                    }
//
//                    throw new RuntimeException("no resolver");
//                }))
//                .andThen(IO.noop());
        return null;
    }

    // 业务逻辑
    private static String renderPosts(List<Tuple2<PostInfo, PostContent>> posts) {
        return posts
                .map(tuple -> {
                    return tuple._1.topic + tuple._2.content;
                })
                .collect(Collectors.joining("\n"));
    }

    private static String renderSidePane(String popularPosts, String topics) {
        return popularPosts + topics;
    }

    private static String renderPage(String leftPane, String mainPane) {
        return leftPane + mainPane;
    }

    private static Fetch<String> mainPane() {
        return getAllPostsInfo()
                .flatMap(posts -> {
                    var ordered = posts.sortBy(info -> info.date).take(5);
                    var content = mapM(ordered, post -> getPostContent(post.id));
                    return content.map(c -> renderPosts(ordered.zip(c)));
                });
    }

    private static Fetch<String> leftPane() {
        return appCombine(Program::renderSidePane, popularPosts(), topics());
    }

    private static Fetch<String> popularPosts() {
        return unit("popular posts");
    }

    private static Fetch<String> topics() {
        return unit("topics");
    }

    private static Fetch<List<Long>> getPostIds() {
        return new FetchPosts().toFetch();
    }

    private static Fetch<List<PostInfo>> getAllPostsInfo() {
        return getPostIds().flatMap(postIds -> mapM(postIds, Program::getPostInfo));
    }

    private static Fetch<PostContent> getPostContent(long postId) {
        return new FetchPostContent(postId).toFetch();
    }

    private static Fetch<PostInfo> getPostInfo(long postId) {
        return new FetchPostInfo(postId).toFetch();
    }
}
