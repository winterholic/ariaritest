package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Member;

import java.util.ArrayList;
import java.util.List;

public class PostDto {
    @Data
    @NoArgsConstructor
    public static class PostReqDto{
        private String title;
        private String text;
        private Boolean fixed;
        private PostType type;
    }

    public static class PostListDto{
        private String message;
        private List<PostBriefContent> posts = new ArrayList<>();
    }

    public static class PostBriefContent{
        private Long postId;
        private Long memberId;
        private String memberName;
        private String title;
        private String text;
        private Boolean fixed;
        private PostType type;
    }

    public static Post toEntity(PostDto.PostReqDto PostReqDto, Club club, Member member) {
        return new Post(PostReqDto.title, PostReqDto.text, PostReqDto.fixed, PostReqDto.type, club, member);
    }
}
