package org.example.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.example.jpa.dto.DescriptionDto;
import org.example.jpa.po.SpringBootJpaExamplePO;
import org.example.jpa.repository.SpringBootJpaExampleRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class Application {

    private final SpringBootJpaExampleRepository repository;

    private final Session session;

    @Autowired
    public Application(SpringBootJpaExampleRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.session = (Session) entityManager;
    }

    @Transactional
    public List<SpringBootJpaExamplePO> getBySpecification() {
        return this.repository.findAll((root, query, cb) -> {
            Predicate predicate = cb.and(
                    cb.equal(root.<Integer>get("deleted"), 0),
                    cb.equal(root.<Long>get("id"), 0),
                    cb.like(root.get("description"), "test%")
            );
            return query.where(predicate).getRestriction();
        });
    }

    @Transactional
    public void sql() {
        DescriptionDto dto = this.repository.getDescription();
        System.out.println(dto.getDescription());
        dto = this.repository.getDescription();
        System.out.println(dto.getDescription());
    }

    @Transactional
    public void getOneAfterChange() {
        SpringBootJpaExamplePO po = this.repository.getReferenceById(1L);
        System.out.println("Old description: " + po.getDescription());
        po.setDescription("vvv");
        po = this.repository.findFirstByDescriptionLike("helloworld");
        System.out.println("New description: " + po.getDescription());
    }

    @Transactional
    public void queryList() {
        Query<DescriptionDto> query = this.session.createQuery("select id, description from SpringBootJpaExamplePO", DescriptionDto.class);
        List<DescriptionDto> result = query.getResultList();
        System.out.println(result);
        System.out.println(this.session.get(SpringBootJpaExamplePO.class, 1L));
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Application app = context.getBean(Application.class);
        app.queryList();
    }

}
