package com.adrabazha.gypsy.board.repository;

import com.adrabazha.gypsy.board.domain.sql.Organization;
import com.adrabazha.gypsy.board.domain.sql.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findUsersByUsernameContainsOrFullNameContains(@NotNull String username, @NotNull String fullName, Pageable userLimit);

    List<User> findUsersByUsernameIn(List<@NotNull String> usernames);

    List<User> findUsersByOrganizationsContains(Organization organization);
}
