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
    @NoArgsConstructor
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
    @NoArgsConstructor
    public static class PostListDto{
        private String message;
        private List<PostSimpleContent> normalPostList = new ArrayList<>();
        private List<PostSimpleContent> fixedPostList = new ArrayList<>();
        public PostListDto(String message, List<Post> normalPosts, List<Post> fixedPosts) {
            this.message = message;
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
            List<Image> images = post.getImages(); // 이거 접근지정 어케처리해야하지?
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

//    @Data
//    @NoArgsConstructor
//    public static class CommentListDto{
//
//    }
//
    @Data
    @NoArgsConstructor
    public static class ParentCommentContent{
        private Long commentId;
        private Long memberId;
        private String nickname;
        private Integer profile;
        private String text;
        private LocalDateTime createdDate;
        private Boolean secret;
        private List<ChildCommentContent> childCommentList;
        public ParentCommentContent(Post post, Comment comment){
            this.commentId = comment.getId();
            this.memberId = comment.getMember().getId();
            this.nickname = comment.getMember().getNickname();
            this.profile = comment.getMember().getProfile();
            this.text = comment.getText();
            this.createdDate = comment.getCreatedDate();
            this.secret = comment.getSecret();
            if(comment.getSecret() && post.getMember().equals(comment.getMember())){this.secret = false;}
            for(Comment childComment : comment.getChildrenComments()){

            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class ChildCommentContent{
        private Long commentId;
        private Long memberId;
        private String nickname;
        private Integer profile;
        private String text;
        private LocalDateTime createdDate;
        private Boolean secret;
        public ChildCommentContent(Post post, Comment comment){
            this.commentId = comment.getId();
            this.memberId = comment.getMember().getId();
            this.nickname = comment.getMember().getNickname();
            this.profile = comment.getMember().getProfile();
            this.text = comment.getText();
            this.createdDate = comment.getCreatedDate();
            this.secret = comment.getSecret();
            if(comment.getSecret() && //글 작성자 or 부모댓글 작성자에게만 비밀댓글해제
                    (post.getMember().equals(comment.getMember()) || comment.getMember().equals(comment.getParentComment().getMember()))){
                this.secret = false;
            }
        }
    }


}
