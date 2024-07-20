package youngpeople.aliali.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import youngpeople.aliali.entity.club.Comment;
import youngpeople.aliali.entity.club.Post;
import youngpeople.aliali.entity.member.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommentDto {
    @Data
    @NoArgsConstructor
    public static class CommentReqDto{
        private String text;
        private Boolean secret;
    }

    public static Comment toEntity(CommentReqDto commentReqDto, Post post, Member member){
        return new Comment(post, member, commentReqDto.text, commentReqDto.secret);
    }

    public static Comment toEntity(CommentReqDto commentReqDto, Post post, Member member, Comment parrentComment){
        return new Comment(post, member, commentReqDto.text, commentReqDto.secret, parrentComment);
    }

    @Data
    @NoArgsConstructor
    public static class NoticeCommentListDto{
        private String message;
        private List<ParentCommentContent> parentCommentContents = new ArrayList<>();
        public NoticeCommentListDto(String message, Post post, List<Comment> parentComments) {
            this.message = message;
            for(Comment parentComment : parentComments){
                if(parentComment.getActivated() || !(parentComment.getChildrenComments().isEmpty())){
                    parentCommentContents.add(new ParentCommentContent(post, parentComment));
                }
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class GeneralCommentListDto{
        private String message;
        private List<ParentCommentContent> parentCommentContents = new ArrayList<>();
        public GeneralCommentListDto(String message, Post post, List<Comment> parentComments, Set<Long> blockedCommentSet){
            this.message = message;
            for (Comment parentcomment : parentComments){
                if(parentcomment.getActivated() || !(parentcomment.getChildrenComments().isEmpty())
                || !(blockedCommentSet.contains(parentcomment.getId()))){ // 부모 댓글의 경우에는 대댓글이 없는 경우는 삭제된 경우 dto에 담지 않음
                    parentCommentContents.add(new ParentCommentContent(post, parentcomment, blockedCommentSet));
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
        public ParentCommentContent(Post post, Comment comment, Set<Long> blockedCommentSet){
            this.commentId = comment.getId();
            this.memberId = comment.getMember().getId();
            this.nickname = comment.getMember().getNickname();
            this.profile = comment.getMember().getProfile();
            this.text = comment.getText();
            this.createdDate = comment.getCreatedDate();
            this.secret = comment.getSecret();
            if(comment.getSecret() && post.getMember().equals(comment.getMember())){this.secret = false;}
            this.activated = comment.getActivated();
            if(blockedCommentSet.contains(comment.getId())){this.visualized = false;}
            for(Comment childComment : comment.getChildrenComments()){
                if(childComment.getActivated() && !(blockedCommentSet.contains(childComment.getId()))){ // 대댓글은 삭제됐거나 차단됐으면 dto에 추가 x
                    ChildCommentContent childCommentContent = new ChildCommentContent(post, childComment);
                    childCommentList.add(childCommentContent);
                }
            }
        }

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
                ChildCommentContent childCommentContent = new ChildCommentContent(post, childComment);
                childCommentList.add(childCommentContent);
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
