package com.eku.EKU.service;

import com.eku.EKU.repository.MappingKeyRepository;
import com.eku.EKU.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class SignUpServiceTest {

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MappingKeyRepository mappingKeyRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ApplicationContext applicationContext;

    /*@Test
    void enrollKeyMemberTest() {
        Student student = Student.builder()
                .studNo((long) 201713883)
                .email("ruldarm00@kyonggi.ac.kr")
                .name("신현호")
                .authenticated(false)
                .department("컴퓨터공학부")
                .build();
        try {
            String secretKey = Base64.getEncoder().encodeToString(KeyGen.generateKey(KeyGen.AES_192).getEncoded());
            signUpService.enrollClient(student, secretKey);

            String authKey = mappingKeyRepository.findMappingKeyByStudent(student)
                    .orElseThrow()
                    .getAuthKey();

            assertThat(authKey).isEqualTo(secretKey);
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }*/

    /*@Test
    void emailAuthTest() {
        try {
            Student student = Student.builder()
                    .studNo((long) 201713883)
                    .email("ruldarm00@kyonggi.ac.kr")
                    .name("신현호")
                    .authenticated(false)
                    .department("컴퓨터공학부")
                    .build();
            Student target = studentRepository.findById(student.getStudNo())
                    .orElseThrow();
            String authKey = mappingKeyRepository.findMappingKeyByStudent(target)
                    .orElseThrow()
                    .getAuthKey();

            assertThat(signUpService.authEmail(authKey)).isTrue();
            //assertThat(studentRepository.findById(student.getStudNo()).orElseThrow().isAuthenticated()).isTrue();
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail();
        }
    }*/

    /*@Test
    public void whenFileUploaded_thenVerifyStatus() throws Exception{
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes(StandardCharsets.UTF_8));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }*/

    /*@Test
    void enrollStudent(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setStudNo((long)201713883);
        signUpForm.setDepartment("컴퓨터공학부");
        signUpForm.setEmail("ruldarm00@kyonggi.ac.kr");
        signUpForm.setName("신현호");
        signUpForm.setPassword("qlalfqjsgh");

        try {
            Student student = signUpService.enrollClient(signUpForm).get();
            assertThat(student.getStudNo()).isEqualTo(201713883);
        } catch (Exception e) {
            fail();
        }
    }*/

    @Test
    void duplTest() {
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println(format);
    }
}