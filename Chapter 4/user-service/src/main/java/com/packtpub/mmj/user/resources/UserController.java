package com.packtpub.mmj.user.resources;

import com.packtpub.mmj.user.domain.model.entity.Entity;
import com.packtpub.mmj.user.domain.model.entity.User;
import com.packtpub.mmj.user.domain.service.UserService;
import com.packtpub.mmj.user.domain.valueobject.UserVO;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sourabh Sharma
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {

    /**
     *
     */
    protected static final Logger logger = Logger.getLogger(UserController.class.getName());

    /**
     *
     */
    protected UserService userService;

    /**
     *
     * @param userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Fetch users with the specified name. A partial case-insensitive match is
     * supported. So <code>http://.../user/rest</code> will find any users with
     * upper or lower case 'rest' in their name.
     *
     * @param name
     * @return A non-null, non-empty collection of users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<User>> findByName(@RequestParam("name") String name) {
        logger.info(String.format("user-service findByName() invoked:{} for {} ", userService.getClass().getName(), name));
        name = name.trim().toLowerCase();
        Collection<User> users;
        try {
            users = userService.findByName(name);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception raised findByName REST Call", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return users.size() > 0 ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Fetch users with the given id. <code>http://.../v1/users/{id}</code> will
     * return user with given id.
     *
     * @param id
     * @return A non-null, non-empty collection of users.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Entity> findById(@PathVariable("id") String id) {
        logger.info(String.format("user-service findById() invoked:{} for {} ", userService.getClass().getName(), id));
        id = id.trim();
        Entity user;
        try {
            user = userService.findById(id);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception raised findById REST Call {0}", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return user != null ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Add user with the specified information.
     *
     * @param userVO
     * @return A non-null user.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> add(@RequestBody UserVO userVO) {
        logger.info(String.format("user-service add() invoked: %s for %s", userService.getClass().getName(), userVO.getName()));
        System.out.println(userVO);
        User user = new User(null, null, null, null, null);
        BeanUtils.copyProperties(userVO, user);
        try {
            userService.add(user);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception raised add Booking REST Call {0}", ex);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
