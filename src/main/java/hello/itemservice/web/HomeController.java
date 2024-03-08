package hello.itemservice.web;


import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.argumentresolver.Login;
import hello.itemservice.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager manager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    //    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);

        //로그인 된 사용자 전용 홈
        return "loginHome";

    }

    //    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {


        Member member = (Member) manager.getSession(request);
        if (member == null) {
            return "home";
        }


        model.addAttribute("member", member);

        //로그인 된 사용자 전용 홈
        return "loginHome";

    }

    //    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {


        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        model.addAttribute("member", loginMember);

        //로그인 된 사용자 전용 홈
        return "loginHome";
    }

    //    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)
                                    Member loginMember,
                                    Model model) {

        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);

        //로그인 된 사용자 전용 홈
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(
            @Login Member loginMember,
            Model model) {

        if (loginMember == null) {
            return "home";
        }
        model.addAttribute("member", loginMember);

        //로그인 된 사용자 전용 홈
        return "loginHome";
    }


}
