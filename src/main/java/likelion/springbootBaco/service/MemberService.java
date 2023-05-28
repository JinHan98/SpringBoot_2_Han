package likelion.springbootBaco.service;

import likelion.springbootBaco.domain.Member;
import likelion.springbootBaco.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MemberService {

    public void save(Member member);

    public Member findById(Long id);

    public List<Member> findAll();
}
