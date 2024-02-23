package hello.itemservice.web.member;

import hello.itemservice.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;


    @GetMapping("/add")
    public String save(@Valid @ModelAttribute("member") Member member,
                       BindingResult result){

        if(result.hasErrors()){
            return "members/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }
}
