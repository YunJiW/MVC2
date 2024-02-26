package hello.itemservice.web;


import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

//    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId",required = false)Long memberId, Model model){
        if(memberId == null){
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member",loginMember);
        
        //로그인 된 사용자 전용 홈
        return "loginHome";

    }


    @PostMapping("/logout")
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
