package likelion.springbootBaco.controller;

import likelion.springbootBaco.domain.Address;
import likelion.springbootBaco.domain.Member;
import likelion.springbootBaco.dto.MemberDto;
import likelion.springbootBaco.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // edited
    @GetMapping("new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberDto.Create());
        return "members/createMemberForm";
    }

    // edited
    @PostMapping("new")
    public String create(MemberDto.Create memberDto) {
        String name = memberDto.getName();
        String city = memberDto.getCity();
        String state = memberDto.getState();
        String street = memberDto.getStreet();
        String zipcode = memberDto.getZipcode();
        Address address = new Address(city, state, street, zipcode);
        Member member = Member.createMember(name, address);
        memberService.save(member);
        return "redirect:/";
    }

    @GetMapping("")
    public String findAll(Model model) {
        List<Member> memberList = memberService.findAll();
        model.addAttribute("memberList",memberList);
        return "members/memberList";
    }
}
