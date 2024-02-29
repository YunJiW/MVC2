package hello.itemservice.web.login;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final LoginService loginService;
    private final SessionManager manager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm loginForm){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute("loginForm")LoginForm loginForm, BindingResult bindingResult,
                          HttpServletResponse response){

        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";
        }

        //세션관리자를 통해 세션을 생성하고 회원 데이터 보관
        manager.createSession(loginMember,response);


        //로그인 성공 처리
        return "redirect:/";
    }



//    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm")LoginForm loginForm, BindingResult bindingResult,
                        HttpServletResponse response){

        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지않습니다.");
            return "login/loginForm";
        }

        //쿠키에 시간정보가 없으면 세션 쿠키
        Cookie idcookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idcookie);

        //로그인 성공 처리
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        manager.expire(request);
        return "redirect:/";
    }


    //    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response,"memberId");

        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response,String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }




}
