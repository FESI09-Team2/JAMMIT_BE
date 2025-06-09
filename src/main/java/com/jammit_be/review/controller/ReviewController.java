package com.jammit_be.review.controller;

import com.jammit_be.common.dto.CommonResponse;
import com.jammit_be.common.dto.response.PageResponse;
import com.jammit_be.review.dto.request.CreateReviewRequest;
import com.jammit_be.review.dto.response.ReviewResponse;
import com.jammit_be.review.dto.response.ReviewStatisticsResponse;
import com.jammit_be.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/jammit/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(
            summary = "리뷰 생성 API", 
            description = "사용자가 모임의 다른 참가자에 대한 리뷰를 생성합니다. 인증된 사용자만 호출 가능합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "리뷰 생성 정보",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateReviewRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "기본 예시",
                                            summary = "기본 리뷰 생성 예시",
                                            value = "{\n" +
                                                    "  \"revieweeId\": 2,\n" +
                                                    "  \"gatheringId\": 1,\n" +
                                                    "  \"content\": \"함께 연주하기 좋은 사람이었습니다. 다음에도 함께 하고 싶습니다.\",\n" +
                                                    "  \"isPracticeHelped\": true,\n" +
                                                    "  \"isGoodWithMusic\": true,\n" +
                                                    "  \"isGoodWithOthers\": true,\n" +
                                                    "  \"isSharesPracticeResources\": false,\n" +
                                                    "  \"isManagingWell\": true,\n" +
                                                    "  \"isHelpful\": true,\n" +
                                                    "  \"isGoodLearner\": false,\n" +
                                                    "  \"isKeepingPromises\": true\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200", 
                            description = "리뷰 생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommonResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "리뷰 생성 실패 - 요청 데이터 오류"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
                    @ApiResponse(responseCode = "403", description = "권한 없음"),
                    @ApiResponse(responseCode = "404", description = "사용자 또는 모임을 찾을 수 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 해당 모임에서 해당 사용자에 대한 리뷰가 존재함")
            }
    )
    public CommonResponse<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        var response = reviewService.createReview(request);
        return new CommonResponse<ReviewResponse>().success(response);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(
            summary = "리뷰 삭제 API", 
            description = "사용자가 작성한 리뷰를 삭제합니다. 본인이 작성한 리뷰만 삭제할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
                    @ApiResponse(responseCode = "403", description = "리뷰를 삭제할 권한이 없음"),
                    @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없음")
            }
    )
    public CommonResponse<Void> deleteReview(
            @Parameter(description = "삭제할 리뷰 ID", example = "1", required = true)
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return CommonResponse.ok();
    }

    @GetMapping("/written")
    @Operation(
            summary = "작성한 리뷰 목록 조회 API", 
            description = "현재 로그인한 사용자가 작성한 리뷰 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", 
                            description = "리뷰 목록 조회 성공",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    public CommonResponse<List<ReviewResponse>> getWrittenReviews() {
        var response = reviewService.getReviewsByReviewer();
        return new CommonResponse<List<ReviewResponse>>().success(response);
    }

    @GetMapping("/received")
    @Operation(
            summary = "받은 리뷰 목록 조회 API", 
            description = "현재 로그인한 사용자가 받은 리뷰 목록을 페이지네이션으로 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", 
                            description = "리뷰 목록 조회 성공",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    public CommonResponse<PageResponse<ReviewResponse>> getReceivedReviews(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") 
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "8") 
            @RequestParam(required = false, defaultValue = "8") int pageSize) {
        var response = reviewService.getReviewsByRevieweeWithPagination(page, pageSize);
        return new CommonResponse<PageResponse<ReviewResponse>>().success(response);
    }

    @GetMapping("/received/statistics")
    @Operation(
            summary = "받은 리뷰 평가항목별 통계 정보 조회 API", 
            description = "현재 로그인한 사용자가 받은 리뷰의 평가항목별 통계 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", 
                            description = "통계 정보 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewStatisticsResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
            }
    )
    public CommonResponse<ReviewStatisticsResponse> getReviewStatistics() {
        var response = reviewService.getReviewStatistics();
        return new CommonResponse<ReviewStatisticsResponse>().success(response);
    }

    @GetMapping("/gathering/{gatheringId}")
    @Operation(
            summary = "모임의 리뷰 목록 조회 API", 
            description = "특정 모임에 대한 모든 리뷰 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", 
                            description = "리뷰 목록 조회 성공",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
                    @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음")
            }
    )
    public CommonResponse<List<ReviewResponse>> getGatheringReviews(
            @Parameter(description = "조회할 모임 ID", example = "1", required = true)
            @PathVariable Long gatheringId) {
        var response = reviewService.getReviewsByGathering(gatheringId);
        return new CommonResponse<List<ReviewResponse>>().success(response);
    }
} 