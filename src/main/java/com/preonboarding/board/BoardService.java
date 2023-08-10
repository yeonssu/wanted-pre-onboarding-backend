package com.preonboarding.board;

import com.preonboarding.global.exception.CustomException;
import com.preonboarding.global.exception.ExceptionCode;
import com.preonboarding.member.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    private final BoardRepository boardRepository;

    public BoardDto.Response createBoard(BoardDto.Post dto, MemberPrincipal memberPrincipal) {
        Board board = boardMapper.toEntity(dto);
        board.updateMember(memberPrincipal.getMember());
        boardRepository.save(board);
        return boardMapper.toResponse(board);
    }

    public Page<BoardDto.Response> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boardMapper.toPageResponse(boards);
    }

    public BoardDto.Response getBoard(Long id) {
        Board board = findVerifiedBoard(id);
        return boardMapper.toResponse(board);
    }

    private Board findVerifiedBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.BOARD_NOT_FOUND));
    }
}
