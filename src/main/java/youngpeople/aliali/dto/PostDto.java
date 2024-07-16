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

    @Data
    @NoArgsConstructor
    public static class CommentReqDto{
        private String text;
        private Boolean secret;
    }

    public static Comment toEntity(CommentReqDto commentReqDto, Post post, Member member){
        return new Comment(post, member, commentReqDto.text, commentReqDto.secret);
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
        private List<PostSimpleContent> normalPostList = new ArrayList<>();
        private List<PostSimpleContent> fixedPostList = new ArrayList<>();
        public NoticePostListDto(String message, List<Post> normalPosts, List<Post> fixedPosts) {
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
    public static class GeneralPostListDto{
        private String message;
        private List<PostSimpleContent> PostList = new ArrayList<>();
        public GeneralPostListDto(String message, List<Post> posts) {
            this.message = message;
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

    @Data
    @NoArgsConstructor
    public static class CommentListDto{
        private String message;
        private List<ParentCommentContent> parentCommentContents = new ArrayList<>();
        public CommentListDto(String message, Post post){
            this.message = message;
            for (Comment parentcomment : post.getComments()){
                if(parentcomment.getActivated() || (!parentcomment.getChildrenComments().isEmpty())){ // 부모 댓글의 경우에는 대댓글이 없는 경우는 삭제된 경우 dto에 담지 않음
                    parentCommentContents.add(new ParentCommentContent(post, parentcomment));
                }
            }
        }
    }

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
        private Boolean activated;
        private Boolean visualized = true;
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
            this.activated = comment.getActivated();
            for(Comment childComment : comment.getChildrenComments()){
                if(childComment.getActivated()){ // 대댓글은 삭제됐으면 dto에 추가 x
                    ChildCommentContent childCommentContent = new ChildCommentContent(post, childComment);
                    childCommentList.add(childCommentContent);
                }
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

// post에 대한 이슈부분들 정리
// 1. 우선 메인에 보이는 post목록은 비회원도 볼 수 있으므로 따로 api를 구성
// 2. 게시판에 들어가서 보이는 post목록들은 비회원은 못보도록 제공함 차단회원을 걸러내서 post로 제공
// 3 - 1. 클럽 활동들에 대해서 사진으로 나온 부분은 비회원도 볼 수 있도록 함
// 3 - 2. 따라서 공지사항 이외의 글들에서 이미지를 모두 가져와서 제공함
// 4 - 1. post의 세부 내용을 제공할 때, 댓글과 같이 주기 vs 따로 api 제공하기에 대한 고민
// 4 - 2. 댓글이 많을 경우에 유저 입장에서 조금이라도 빠른 피드백을 위해서는 후자가 맞음
// 4 - 3. 책임분리에 대한 원리에 따라서도 후자를 따라가는 것이 좋음
// 5 - 1. 댓글을 제공할 때, 차단에 따라서 구현 과정에서 분기점이 많이 나타나고 있음
// 5 - 2. (나중에 고려)dto 중심으로 return을 제공하는 서비스의 구조를 개선하면 좀 더 좋을 것 같음.
// 6 - 1. 댓글 리포지토리 구현할 때 activated검사를 필수조건에서 제외
// 6 - 2. 삭제된 댓글 여부도 부모댓글인 경우에 알려야하기 때문
// 7 - 1. 차단된 댓글을 어떻게 제공을 막을 것인지에 대한 고민
// 7 - 2. 1안 dto에 애초에 담지 않는 방식, 그렇다면 currentuser까지 고려해야하는데 이거는 구조적인 문제가 있다고 생각
// 7 - 3. 2안 dto에서 제거하는 방법, 서버에서 처리하기 때문에 담았다가 지우는 것이 그렇게 비효율적이라고 보이지 않음,
// 7 - 4. 애초에 담지 않을 때에도 비교가 이루어지기 때문에 그에 준하는 작업소요가 소요될 것으로 우려
// 7 - 5. 2안으로 진행하고 나중에 구조개선 후에 지우는 작업을 컨트롤러에서 해주면 좋을것 같다는 생각이 듬
// 8. 댓글 수정기능 이거 구현하는 것이 그렇게 어려운 부분은 아닌데, 일단 어느정도 에브리타임의 기획을 따라가고 있으므로 배제하였음
