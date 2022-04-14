package com.eku.EKU.controller;

import com.eku.EKU.form.FreeBoardForm;
import com.eku.EKU.service.FreeBoardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게시판 접근에 응답하는 컨트롤러
 */
@RestController
public class FreeBoardController {
    private final FreeBoardService boardService;


    public FreeBoardController(FreeBoardService boardService) {
        this.boardService = boardService;
    }
    /**
     * 게시물 삽입
     */
    @PostMapping("/insertBoard")
    public boolean insertBoard(@RequestBody FreeBoardForm form){
        return false;
    }
}