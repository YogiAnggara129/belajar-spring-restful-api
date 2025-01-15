package com.yogi_code.belajar_spring_restful_api.service;

import com.yogi_code.belajar_spring_restful_api.entity.Contact;
import com.yogi_code.belajar_spring_restful_api.entity.User;
import com.yogi_code.belajar_spring_restful_api.model.ContactResponse;
import com.yogi_code.belajar_spring_restful_api.model.CreateContactRequest;
import com.yogi_code.belajar_spring_restful_api.model.SearchContactRequest;
import com.yogi_code.belajar_spring_restful_api.model.UpdateContactRequest;
import com.yogi_code.belajar_spring_restful_api.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public ContactResponse create(User user, CreateContactRequest request) {
        validationService.validate(request);

        final Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);

        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional(readOnly = true)
    public ContactResponse get(User user, String id) {
        final Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
        return toContactResponse(contact);
    }

    @Transactional
    public ContactResponse update(User user, UpdateContactRequest request) {
        validationService.validate(request);

        final Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());

        contactRepository.save(contact);

        return toContactResponse(contact);
    }

    @Transactional
    public void delete(User user, String id) {
        final Contact contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));
       contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        final Specification<Contact> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstName"), "%" + request.getName() + "%"),
                        criteriaBuilder.like(root.get("lastName"), "%" + request.getName() + "%")
                ));
            }
            if (Objects.nonNull(request.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + request.getEmail() + "%"));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + request.getPhone() + "%"));
            }

            return query != null ? query.where(predicates.toArray(new Predicate[]{})).getRestriction() : null;
        };

        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        final Page<Contact> contacts = contactRepository.findAll(specification, pageable);
        final List<ContactResponse> contactResponses = contacts.getContent().stream().map(this::toContactResponse).toList();
        return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
    }

    private ContactResponse toContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
