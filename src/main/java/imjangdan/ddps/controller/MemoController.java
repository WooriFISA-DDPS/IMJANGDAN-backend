package imjangdan.ddps.controller;

import imjangdan.ddps.dto.request.memo.MemoUpdateDto;
import imjangdan.ddps.dto.request.memo.MemoWriteDto;
import imjangdan.ddps.dto.request.memo.MemoSearchData;
import imjangdan.ddps.dto.response.memo.ResMemoDetailsDto;
import imjangdan.ddps.dto.response.memo.ResMemoListDto;
import imjangdan.ddps.dto.response.memo.ResMemoWriteDto;
import imjangdan.ddps.entity.Member;
import imjangdan.ddps.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
@Slf4j
public class MemoController {

    private final MemoService memoService;

    // 페이징 목록
    @GetMapping("/list")
    public ResponseEntity<Page<ResMemoListDto>> memoList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResMemoListDto> listDTO = memoService.getAllMemos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }

    // 페이징 검색 , Get 요청 @RequestBody 사용할 수 없음
    @GetMapping("/search")
    public ResponseEntity<Page<ResMemoListDto>> search(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String writerName) {
        MemoSearchData memoSearchData = MemoSearchData.createdSearchData(title, content, writerName);
        Page<ResMemoListDto> searchMemo = memoService.search(memoSearchData, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(searchMemo);
    }

    @PostMapping("/write")
    public ResponseEntity<ResMemoWriteDto> write(
            @RequestBody MemoWriteDto memoDTO,
            @AuthenticationPrincipal Member member) {
        Thread currentThread = Thread.currentThread();
        log.info("현재 실행 중인 스레드: " + currentThread.getName());
        ResMemoWriteDto saveMemoDTO = memoService.write(memoDTO, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveMemoDTO);
    }

    @GetMapping("/{memoId}")
    public ResponseEntity<ResMemoDetailsDto> detail(@PathVariable("memoId") Long memoId) {
        ResMemoDetailsDto findMemoDTO = memoService.detail(memoId);
        return ResponseEntity.status(HttpStatus.OK).body(findMemoDTO);
    }

    // 상세보기 -> 수정
    @PatchMapping("/{memoId}/update")
    public ResponseEntity<ResMemoDetailsDto> update(
            @PathVariable Long memoId,
            @RequestBody MemoUpdateDto memoDTO) {
        ResMemoDetailsDto updateMemoDTO = memoService.update(memoId, memoDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updateMemoDTO);
    }

    // 상세보기 -> 삭제
    @DeleteMapping("/{memoId}/delete")
    public ResponseEntity<Long> delete(@PathVariable Long memoId) {
        memoService.delete(memoId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
