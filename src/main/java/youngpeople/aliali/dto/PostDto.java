package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.club.Club;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.enumerated.PostType;
import youngpeople.aliali.entity.member.Member;

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
}
