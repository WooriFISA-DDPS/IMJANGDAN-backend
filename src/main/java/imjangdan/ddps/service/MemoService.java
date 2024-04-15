package imjangdan.ddps.service;

import jakarta.transaction.Transactional;
import imjangdan.ddps.common.exception.ResourceNotFoundException;
import imjangdan.ddps.dto.request.memo.MemoUpdateDto;
import imjangdan.ddps.dto.request.memo.MemoWriteDto;
import imjangdan.ddps.dto.request.memo.MemoSearchData;
import imjangdan.ddps.dto.response.memo.ResMemoDetailsDto;
import imjangdan.ddps.dto.response.memo.ResMemoListDto;
import imjangdan.ddps.dto.response.memo.ResMemoWriteDto;
import imjangdan.ddps.entity.Memo;
import imjangdan.ddps.entity.Member;
import imjangdan.ddps.repository.MemoRepository;
import imjangdan.ddps.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;

    // 페이징 리스트
    public Page<ResMemoListDto> getAllMemos(Pageable pageable) {
        Page<Memo> memos = memoRepository.findAllWithMemberAndComments(pageable);
        List<ResMemoListDto> list = memos.getContent().stream()
                .map(ResMemoListDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, memos.getTotalElements());
    }

    // 메모 검색, isEmpty() == ""
    public Page<ResMemoListDto> search(MemoSearchData searchData, Pageable pageable) {
        Page<Memo> result = null;
        if (!searchData.getTitle().isEmpty()) {
            result = memoRepository.findAllTitleContaining(searchData.getTitle(), pageable);
        } else if (!searchData.getContent().isEmpty()) {
            result = memoRepository.findAllContentContaining(searchData.getContent(), pageable);
        } else if (!searchData.getWriterName().isEmpty()) {
            result = memoRepository.findAllUsernameContaining(searchData.getWriterName(), pageable);
        }
        List<ResMemoListDto> list = result.getContent().stream()
                .map(ResMemoListDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    // 메모 등록
    public ResMemoWriteDto write(MemoWriteDto memoDTO, Member member) {

        Memo memo = MemoWriteDto.ofEntity(memoDTO);
        Member writerMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member Email", member.getEmail())
        );
        memo.setMappingMember(writerMember);
        Memo saveMemo = memoRepository.save(memo);
        return ResMemoWriteDto.fromEntity(saveMemo, writerMember.getUsername());
    }

    // 메모 상세보기
    public ResMemoDetailsDto detail(Long memoId) {
       Memo findMemo = memoRepository.findByIdWithMemberAndCommentsAndFiles(memoId).orElseThrow(
               () -> new ResourceNotFoundException("Memo", "Memo Id", String.valueOf(memoId))
       );
       return ResMemoDetailsDto.fromEntity(findMemo);
    }

    // 메모 수정
    public ResMemoDetailsDto update(Long memoId, MemoUpdateDto memoDTO) {
        Memo updateMemo = memoRepository.findByIdWithMemberAndCommentsAndFiles(memoId).orElseThrow(
                () -> new ResourceNotFoundException("Memo", "Memo Id", String.valueOf(memoId))
        );
        updateMemo.update(memoDTO.getTitle(), memoDTO.getContent(), memoDTO.getLatitude(), memoDTO.getLongitude(), memoDTO.getCategory());
        return ResMemoDetailsDto.fromEntity(updateMemo);
    }

    // 메모 삭제
    public void delete(Long memoId) {
        memoRepository.deleteById(memoId);
    }
}
