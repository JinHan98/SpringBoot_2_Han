package likelion.springbootBaco.service;

import likelion.springbootBaco.domain.Address;
import likelion.springbootBaco.domain.Member;
import likelion.springbootBaco.dto.MemberDto;
import likelion.springbootBaco.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Test
    public void saveMember() throws Exception {
        //given
        String name = "hello";
        String city = "city";
        String state = "state";
        String street = "street";
        String zipcode = "11111";
        MemberDto.Create dto = new MemberDto.Create(name, city, state, street, zipcode);
        Address address = new Address(dto.getCity(), dto.getState(), dto.getStreet(), dto.getZipcode());
        Member member = Member.createMember(name, address);
        memberService.save(member);
        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        if (optionalMember.isPresent()) {
            Member findMember = optionalMember.get();
            Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        }
        //then
    }
}
