package hello.itemservice.web.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    //동시성 고려안했음.
    private static Map<Long,Member> store = new HashMap<>();

    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save : member={}",member);
        store.put(member.getId(),member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }


    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
