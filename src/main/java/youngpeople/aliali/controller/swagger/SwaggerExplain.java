package youngpeople.aliali.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class SwaggerExplain {
    //schoolcontroller
    @Operation(summary = "모든 학교 이름 조회", description = "저장되어 있는 모든 학교들의 이름을 조회한다.")
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SchoolGetAllExplain {
    }


    //recruitmentcontroller
    @Operation(
            summary = "모집 상세 조회하기",
            description = "동아리 모집의 상세한 내용을 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RecruitmentGetDetailExplain {
    }

    @Operation(
            summary = "모집 등록하기(스웨거에서 테스트 불가능)",
            description = "동아리의 새로운 모집을 등록한다. 동아리 관리자만 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RecruitmentRegisterExplain {
    }

    @Operation(
            summary = "모집 수정하기(스웨거테스트 불가능)",
            description = "동아리의 새로운 모집을 등록한다. 동아리 관리자만 가능하다. 아직 포스트맨에도 따로 없지만" +
                    "get을 put으로 바꿔서 그대로 테스트 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RecruitmentRegisterModifyExplain {
    }

    @Operation(
            summary = "모집 삭제하기",
            description = "동아리의 모집을 삭제한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RecruitmentDeleteExplain {
    }




    //membercontroller
    @Operation(
            summary = "회원 정보 조회",
            description = "마이페이지에서 회원정보를 조회한다. profile은 사진이 아니다." +
                    "profile은 integer데이터로 서버에서는 유저가 어떤 프로필 아이콘을 가지고 있는지 integer데이터로 가지고 있음"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MemberGetMyExplain {
    }

    @Operation(
            summary = "회원 프로필 수정",
            description = "회원 프로필 수정 API"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MemberPutMyProfileExplain {
    }

    @Operation(
            summary = "회원 닉네임 수정",
            description = "회원 닉네임 수정 API"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MemberPutMyNicknameExplain {
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "카카오 서버 탈퇴(프) → DB 상 논리 삭제(백) → 3개월(임시) 뒤 물리 삭제"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MemberDeleteMyExplain {
    }

    @Operation(
            summary = "학교 인증 요청하기",
            description = "DB에 저장된 학교 이메일과 일치 여부 확인부터 진행한다." +
                    "만약에 이메일이 일치하지 않는다면 오류가 전송된다." +
                    "일치하는 경우에는 이메일을 통해서 온 링크로 인증 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MemberAuthenticationSchoolRequestExplain {
    }

    @Operation(
            summary = "학교 인증 확인하기",
            description = "이메일을 통해서 학교 인증을 확인한다. 시간안에 확인하지 못했을 경우에는" +
                    "인증이 실패한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MemberAuthenticationSchoolCheckExplain {
    }



    //mainpageController
    @Operation(
            summary = "모든 동아리 조회",
            description = "모든 동아리(자신의학교동아리 + 연합동아리)를 조회한다. 단 (로그인하지 않았거나, ?) " +
                    "학교인증이 안되어 있을 경우에는 연합동아리만 조회가 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainAllClubsExplain {
    }

    @Operation(
            summary = "학교 동아리 조회",
            description = "학교인증이 되어있는 경우에 자신의 학교의 동아리를 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainSchoolClubExplain {
    }

    @Operation(
            summary = "연합 동아리 조회",
            description = "연합 동아리를 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainUnionClubExplain {
    }

    @Operation(
            summary = "전공이름과 단과대학이름 조회",
            description = "학교 인증이 된 회원의 경우에 단과대학 동아리나 과동아리를 검색하려는 경우가" +
                    "있을 수가 있다. 그 때 카테고리를 제공해야하므로 필요하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainSchoolCategoryExplain {
    }

    @Operation(
            summary = "모든 동아리 모집 최신순 조회",
            description = "모든 동아리의 모집에 대해서 최신순으로 조회한다." +
                    "여기서 모든 동아리란 마찬가지로 자신의 학교 동아리 + 연합동아리이다." +
                    "만약에 학교인증이 되지 않은경우 연합동아리만 제공한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainAllRecruitmentRecentExplain {
    }

    @Operation(
            summary = "모든 동아리 모집 인기순 조회",
            description = "모든 동아리의 모집에 대해서 인기순으로 조회한다." +
                    "여기서 모든 동아리란 마찬가지로 자신의 학교 동아리 + 연합동아리이다." +
                    "만약에 학교인증이 되지 않은경우 연합동아리만 제공한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainAllRecruitmentHotExplain {
    }

    @Operation(
            summary = "모든 동아리 모집 마감임박순 조회",
            description = "모든 동아리의 모집에 대해서 마감임박순으로 조회한다." +
                    "여기서 모든 동아리란 마찬가지로 자신의 학교 동아리 + 연합동아리이다." +
                    "만약에 학교인증이 되지 않은경우 연합동아리만 제공한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainAllRecruitmentDeadlineExplain {
    }

    @Operation(
            summary = "학교 동아리 모집 최신순 조회",
            description = "자신의 학교 동아리의 모집에 대해서 최신순으로 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainSchoolRecruitmentRecentExplain {
    }

    @Operation(
            summary = "학교 동아리 모집 인기순 조회",
            description = "자신의 학교 동아리의 모집에 대해서 인기순으로 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainSchoolRecruitmentHotExplain {
    }

    @Operation(
            summary = "학교 동아리 모집 마감임박순 조회",
            description = "자신의 학교 동아리의 모집에 대해서 마감임박순으로 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainSchoolRecruitmentDeadlineExplain {
    }

    @Operation(
            summary = "연합 동아리 모집 최신순 조회",
            description = "연합 동아리의 모집에 대해서 최신순으로 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainUnionRecruitmentRecentExplain {
    }

    @Operation(
            summary = "연합 동아리 모집 인기순 조회",
            description = "연합 동아리의 모집에 대해서 인기순으로 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainUnionRecruitmentHotExplain {
    }

    @Operation(
            summary = "연합 동아리 모집 마감임박순 조회",
            description = "연합 동아리의 모집에 대해서 마감임박순으로 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MainUnionRecruitmentDeadlineExplain {
    }



    // logincontroller
    @Operation(
            summary = "로그인하기",
            description = "로그인"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LoginKakaoAuthExplain {
    }

    @Operation(
            summary = "토큰 재발급하기",
            description = "리퀘스트 토큰 유효시 토큰을 재발급한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LoginReissueExplain {
    }



    //clubmembercontroller
    @Operation(
            summary = "동아리 회원 목록 조회하기",
            description = "해당 동아리에 소속된 회원들의 리스트를 조회합니다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubMemberGetExplain {
    }

    @Operation(
            summary = "동아리 회원들 정보 수정하기",
            description = "해당 동아리에 소속된 회원들의 권한과 동아리에서의 역할 이름을 수정합니다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubMemberPutExplain {
    }

    @Operation(
            summary = "동아리 회원들 강퇴하기",
            description = "동아리 회원들의 id리스트를 받아서 해당 회원들을 동아리 탈퇴시킵니다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubMemberDeleteExplain {
    }

    @Operation(
            summary = "동아리 탈퇴하기",
            description = "해당 동아리에서 자신이 직접 탈퇴합니다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubMemberDeleteMyExplain {
    }



    // clubcontroller
    @Operation(
            summary = "동아리 상세 조회하기",
            description = "동아리의 상세 정보를 조회한다. typeA와 typeB는 enum이 정해져있다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubGetDetailExplain {
    }

    @Operation(
            summary = "내 동아리 목록 조회하기",
            description = "마이페이지에서 내가 가입한 동아리들의 목록을 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubGetMyExplain {
    }

    @Operation(
            summary = "내 북마크 동아리 목록 조회하기",
            description = "마이페이지에서 내가 즐겨찾기에 등록한 동아리들의 목록을 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubGetBookmarkExplain {
    }

    @Operation(
            summary = "동아리 생성하기",
            description = "스웨거 테스트 불가능, 포스트맨에 테스트 있습니다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubRegisterExplain {
    }

    @Operation(
            summary = "동아리 수정하기",
            description = "스웨거 테스트 불가능, 포스트맨에 아직 따로 없는데 put으로 바꿔서 그대로 하면 됩니다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubPutExplain {
    }

    @Operation(
            summary = "동아리 삭제하기",
            description = "동아리 관리자만 삭제할 수 있다, 클럽회원이 모두 없을 시에만 삭제가 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ClubDeleteExplain {
    }



    // bookmarkcontroller
    @Operation(
            summary = "북마크 등록하기",
            description = "개별 북마크 등록"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BookmarkRegisterExplain {
    }

    @Operation(
            summary = "북마크 삭제하기",
            description = "북마크 물리삭제"
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BookmarkDeleteExplain {
    }



    // applycontroller
    @Operation(
            summary = "지원리스트 조회",
            description = "동아리에서 모집에 대한 지원자들을 조회한다. 동아리의 관리자들만 볼 수 있다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyClubListExplain {
    }

    @Operation(
            summary = "지원 상세 조회",
            description = "각 지원에 대해서, 상세한 내용을 조회한다. 동아리의 관리자들만 볼 수 있다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyClubDetailExplain {
    }

    @Operation(
            summary = "지원 등록",
            description = "어떤 동아리의 모집에 대하여 지원을 신청한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyRegisterExplain {
    }

    @Operation(
            summary = "지원 삭제(지원삭제가 불가능해져서 쓰지 않는 기능)",
            description = "지원삭제가 불가능해져서 쓰지 않는 기능이다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyDeleteExplain {
    }

    @Operation(
            summary = "지원한 멤버들 지원 승인하기",
            description = "어떤 동아리의 모집에 대해서 지원한 멤버들 중에서 요청한 멤버들에 대하여" +
                    "해당 멤버들의 id리스트를 받아서 지원을 승인한다. 동아리의 관리자만 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyClubApproveListExplain {
    }

    @Operation(
            summary = "지원한 멤버들 지원 거절하기",
            description = "어떤 동아리의 모집에 대해서 지원한 멤버들 중에서 요청한 멤버들에 대하여" +
                    "해당 멤버들의 id리스트를 받아서 지원을 거절한다. 동아리의 관리자만 가능하다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyClubRefuseListExplain {
    }

    @Operation(
            summary = "내가 한 지원 리스트 보기",
            description = "마이페이지에서 내가 지원했던 지원목록에 대해서 조회한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyMyListExplain {
    }

    @Operation(
            summary = "내 지원(동아리관리자가 승인한 지원) 지원 승인하기",
            description = "마이페이지에서 내가 지원했던 지원목록들 중에 동아리관리자가 승인해준 지원을 승인한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyMyApproveExplain {
    }

    @Operation(
            summary = "내 지원(동아리관리자가 승인한 지원) 지원 거절하기",
            description = "마이페이지에서 내가 지원했던 지원목록들 중에 동아리관리자가 승인해준 지원을 거절한다."
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApplyMyRefuseExplain {
    }
}
