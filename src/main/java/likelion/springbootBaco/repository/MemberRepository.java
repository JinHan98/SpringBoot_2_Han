package likelion.springbootBaco.repository;

import likelion.springbootBaco.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
}







