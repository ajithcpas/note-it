package com.noteit.auth;

import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<Authorities, Users>
{
}
