package com.eku.eku_ocr_test.service;

import com.eku.eku_ocr_test.domain.Critic;
import com.eku.eku_ocr_test.domain.Grade;
import com.eku.eku_ocr_test.exceptions.NoSuchStudentException;
import com.eku.eku_ocr_test.form.CriticForm;
import com.eku.eku_ocr_test.repository.CriticRepository;
import com.eku.eku_ocr_test.repository.StudentRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class CriticServiceTest {

    @Autowired
    CriticService criticService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CriticRepository criticRepository;

    @Test
    void applyTest() {
        CriticForm criticForm = new CriticForm();
        criticForm.setEmail("ruldarm00@kyonggi.ac.kr");
        criticForm.setContent("ㅋㅋㅋㅋ 할많하않");
        criticForm.setGrade(Grade.A);
        criticForm.setLectureName("소프트웨어 공학");
        criticForm.setProfName("권기현");
        Critic critic = criticService.applyCritic(criticForm);

        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(critic.getContent()).isEqualTo(criticForm.getContent());
        soft.assertThat(critic.getLectureName()).isEqualTo(criticForm.getLectureName());
        soft.assertThat(critic.getProfName()).isEqualTo(criticForm.getProfName());
        soft.assertThat(critic.getGrade()).isEqualTo(criticForm.getGrade());
        soft.assertThat(critic.getWriter().getEmail()).isEqualTo(criticForm.getEmail());
        soft.assertAll();
    }

    @Test
    void applyTestNotValidWriter() {
        CriticForm criticForm = new CriticForm();
        criticForm.setEmail("notvalid@kyonggi.ac.kr");
        criticForm.setContent("ㅋㅋㅋㅋ 할많하않");
        criticForm.setGrade(Grade.A);
        criticForm.setLectureName("소프트웨어 공학");
        criticForm.setProfName("권기현");
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchStudentException.class, () -> criticService.applyCritic(criticForm));
    }

    @Test
    void removeCriticSuccess() {
        try {
            Critic target = criticRepository.findAll().stream().findFirst().orElseThrow();
            criticService.removeCritic(target.getCId());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void removeCriticFailNotExistsID() {
        assertThrows(EmptyResultDataAccessException.class, () -> criticService.removeCritic(Long.MAX_VALUE));
    }

    @Test
    void updateCriticSuccess() {
        try {
            Critic target = criticRepository.findAll().stream().findFirst().orElseThrow();
            CriticForm criticForm = new CriticForm();
            criticForm.setCriticId(target.getCId());
            criticForm.setContent("헉 너무 재밌었어요 ㅠㅠ");

            criticService.updateCritic(criticForm);
            Critic afterUpdate = criticRepository.findById(target.getCId()).orElseThrow();
            org.assertj.core.api.Assertions.assertThat(afterUpdate.getContent()).isEqualTo(criticForm.getContent());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void updateCriticFailNoId() {
        CriticForm criticForm = new CriticForm();
        criticForm.setContent("헉 너무 재밌었어요 ㅠㅠ");

        assertThrows(NoSuchElementException.class, () -> criticService.updateCritic(criticForm));
    }

    @Test
    void updateCriticFailNotValidId() {
        CriticForm criticForm = new CriticForm();
        criticForm.setCriticId(Long.MAX_VALUE);
        criticForm.setContent("헉 너무 재밌었어요 ㅠㅠ");

        assertThrows(NoSuchElementException.class, () -> criticService.updateCritic(criticForm));
    }
}
