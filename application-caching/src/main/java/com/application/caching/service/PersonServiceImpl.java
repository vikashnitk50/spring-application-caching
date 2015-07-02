package com.application.caching.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.application.caching.dao.PersonDAO;
import com.application.caching.model.Person;

public class PersonServiceImpl implements PersonService {

  private PersonDAO personDAO;

  public void setPersonDAO(PersonDAO personDAO) {
    this.personDAO = personDAO;
  }

  @Override
  @Transactional
  public void addPerson(Person p) {
    this.personDAO.addPerson(p);
  }

  @Override
  @Transactional
  public void updatePerson(Person p) {
    this.personDAO.updatePerson(p);
  }

  @Override
  @Transactional
  public List<Person> listPersons() {
    return this.personDAO.listPersons();
  }

  @Override
  @Transactional
  public Person getPersonById(int id) {
    return this.personDAO.getPersonById(id);
  }

  @Override
  @Transactional
  public void removePerson(int id) {
    this.personDAO.removePerson(id);
  }

}
