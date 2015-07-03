package com.application.caching.service;

import java.util.List;

import com.application.caching.annotation.InvalidateMethodCache;
import com.application.caching.annotation.MethodCache;
import com.application.caching.model.Person;

public interface PersonService {

  @InvalidateMethodCache
  public void addPerson(Person p);

  @InvalidateMethodCache
  public void updatePerson(Person p);

  @MethodCache
  public List<Person> listPersons();

  public Person getPersonById(int id);

  @InvalidateMethodCache
  public void removePerson(int id);

}
