package cz.codecamp.repository;

import cz.codecamp.model.Location;
import cz.codecamp.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jakubbares on 11/17/16.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmailLogin(String emailLogin);
    User findByUserName(String userName);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE Users u SET u.cityFrom = :cityFrom, u.pctAvgPriceMax = :pctAvgPriceMax,u.flyDurationMinutesMax = :flyDurationMinutesMax,u.dateFrom = :dateFrom,u.dateTo = :dateTo WHERE u.user_name = :userName")
//    User  updateUserSettings(@Param("cityFrom") Location cityFrom, @Param("userName") String userName);

//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE User u SET u.cities_to = :citiesTo WHERE u.user_name = :userName")
//    void updateCitiesTo(@Param("citiesTo") List<Location> citiesTo, @Param("userName") String userName);
}


