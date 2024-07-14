package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import youngpeople.aliali.entity.Image;
import youngpeople.aliali.entity.club.Club;
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
        private PostType type;
    }

    public static Post toEntity(PostDto.PostReqDto PostReqDto, Club club, Member member) {
        return new Post(PostReqDto.title, PostReqDto.text, PostReqDto.fixed, PostReqDto.type, club, member);
    }

    @Data
    @NoArgsConstructor
    public static class PostListDto{
        private String message;
        private List<PostBriefContent> normalPostList = new ArrayList<>();
        private List<PostBriefContent> fixedPostList = new ArrayList<>();
        public PostListDto(String message, List<Post> normalPosts, List<Post> fixedPosts) {
            this.message = message;
            for(Post post : normalPosts){
                this.normalPostList.add(new PostBriefContent(post));
            }
            for(Post post : fixedPosts){
                this.fixedPostList.add(new PostBriefContent(post));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class PostBriefContent{
        private Long postId;
        private Long memberId;
        private String NickName;
        private String title;
        private LocalDateTime createdDate;
        private String imageUri = "";
        public PostBriefContent(Post post) {
            this.postId = post.getId();
            this.memberId = post.getMember().getId();
            this.NickName = post.getMember().getNickname();
            this.title = post.getTitle();
            this.createdDate = post.getCreatedDate();
            List<Image> images = post.getImages(); // 이거 접근지정 어케처리해야하지?
            if (images != null && !images.isEmpty()) {this.imageUri = images.get(0).getImageUri();}
        }
    }


}
