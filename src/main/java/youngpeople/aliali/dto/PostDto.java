package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Comment;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostDto {
    @Data
    public static class PostReqDto{
        private String title;
        private String text;
        private Boolean fixed;
    }

    public static Post toEntity(PostReqDto postReqDto, Club club, Member member, PostType postType) {
        return new Post(postReqDto.title, postReqDto.text, postReqDto.fixed, postType, club, member);
    }

    public static void updateEntity(PostReqDto postReqDto, Post post, List<Image> images){
        post.setTitle(postReqDto.title);
        post.setText(postReqDto.text);
        post.setFixed(postReqDto.fixed);
        post.setImages(images);
    }

    @Data
    public static class FixedReqDto{
        private boolean fixed;
    }

    @Data
    @NoArgsConstructor
    public static class MainPagePostListDto{
        private String message;
        private List<PostSimpleContent> PostList = new ArrayList<>();
        public MainPagePostListDto(String message, List<Post> posts) {
            this.message = message;
            for(Post post : posts){
                PostList.add(new PostSimpleContent(post));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class ImageListDto{
        private String message;
        private List<String> imageList = new ArrayList<>();
        public ImageListDto(String message, List<Image> images) {
            this.message = message;
            for(Image image : images){
                imageList.add(image.getImageUri());
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class NoticePostListDto{
        private String message;
        private int totalPages;
        private List<PostSimpleContent> normalPostList = new ArrayList<>();
        private List<PostSimpleContent> fixedPostList = new ArrayList<>();
        public NoticePostListDto(String message, List<Post> normalPosts, List<Post> fixedPosts, int totalPages) {
            this.message = message;
            this.totalPages = totalPages;
            for(Post post : normalPosts){
                this.normalPostList.add(new PostSimpleContent(post));
            }
            for(Post post : fixedPosts){
                this.fixedPostList.add(new PostSimpleContent(post));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class GeneralPostListDto{
        private String message;
        private int totalPages;
        private List<PostSimpleContent> PostList = new ArrayList<>();
        public GeneralPostListDto(String message, List<Post> posts, int totalPages) {
            this.message = message;
            this.totalPages = totalPages;
            for(Post post : posts){
                PostList.add(new PostSimpleContent(post));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class PostSimpleContent {
        private Long postId;
        private Long memberId;
        private String NickName;
        private String title;
        private LocalDateTime createdDate;
        private String imageUri = "";
        public PostSimpleContent(Post post) {
            this.postId = post.getId();
            this.memberId = post.getMember().getId();
            this.NickName = post.getMember().getNickname();
            this.title = post.getTitle();
            this.createdDate = post.getCreatedDate();
            List<Image> images = post.getImages(); // 이거 접근지정 어케처리해야하지? 이렇게 디폴트로 선언해도 지역변수로 사용되고 메모리에서 날아가나요?
            if (images != null && !images.isEmpty()) {this.imageUri = images.get(0).getImageUri();}
        }
    }

    @Data
    @NoArgsConstructor
    public static class PostDetailDto{
        private String message;
        private String title;
        private String text;
        private List<String> images;
        public PostDetailDto(String message, Post post) {
            this.message = message;
            this.title = post.getTitle();
            this.text = post.getText();
            for (Image image : post.getImages()) {
                this.images.add(image.getImageUri());
            }
        }
    }
}
