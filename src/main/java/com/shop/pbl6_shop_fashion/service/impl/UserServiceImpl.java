package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.dto.UserResponse;
import com.shop.pbl6_shop_fashion.dto.mapper.UserMapper;
import com.shop.pbl6_shop_fashion.dto.mapper.impl.UserMapperImpl;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.BaseException;
import com.shop.pbl6_shop_fashion.exception.RoleException;
import com.shop.pbl6_shop_fashion.exception.UniqueConstraintViolationException;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import com.shop.pbl6_shop_fashion.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    /**
     * hasRole Admin
     * Lấy danh sách người dùng theo trang và kích thước trang đã cho.
     *
     * @param pageable Đối tượng Pageable để xác định trang và kích thước trang.
     * @return Đối tượng Page<UserResponse> chứa danh sách người dùng theo trang.
     */
    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> userResponses = users.map(user -> userMapper.userToUserResponse(user));
        return userResponses;
    }


    /**
     * hasRole Admin or user.id=id (param)
     * Lấy thông tin người dùng dựa trên ID.
     *
     * @param id ID của người dùng cần lấy thông tin.
     * @return Đối tượng UserResponse chứa thông tin của người dùng.
     * @throws UserNotFoundException Nếu không tìm thấy người dùng với ID đã cho.
     */
    @Override
    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User is not found: " + id));
        UserMapper userMapper = new UserMapperImpl();
        return userMapper.userToUserResponse(user);
    }


    /**
     * all user
     * Cập nhật thông tin người dùng dựa trên thông tin được cung cấp trong đối tượng UserResponse.
     *
     * @param userResponse Đối tượng UserResponse chứa thông tin cần cập nhật.
     * @return Đối tượng UserResponse chứa thông tin người dùng đã được cập nhật.
     * @throws UserNotFoundException              Nếu không tìm thấy người dùng với ID đã cho.
     * @throws UniqueConstraintViolationException Nếu xảy ra vi phạm ràng buộc duy nhất (unique constraint) cho số điện thoại hoặc địa chỉ email.
     */
    @Override
    public UserResponse updateUser(UserResponse userResponse) {
        User userDb = userRepository.findById(userResponse.getId())
                .orElseThrow(() -> new UserNotFoundException("User is not found : " + userResponse.getId()));

        UserMapper userMapper = new UserMapperImpl();
        try {
            userDb = userMapper.userResponseToUser(userResponse, userDb);
            userRepository.save(userDb);
        } catch (DataIntegrityViolationException e) {
            // Handle the unique constraint violation
            throw new BaseException(e.getMessage());
        }

        return userMapper.userToUserResponse(userDb);
    }


    /**
     * hasRole Admin
     * Khóa hoặc mở khóa tài khoản người dùng dựa trên trạng thái hiện tại.
     *
     * @param id ID của người dùng cần bị khóa hoặc mở khóa.
     * @return Trạng thái khóa mới của người dùng (true nếu bị khóa, false nếu không bị khóa).
     * @throws UserNotFoundException Nếu không tìm thấy người dùng với ID đã cho.
     */
    @Override
    public boolean lockUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User is not found with id: " + id));
        if (!user.getRole().equals(RoleType.ADMIN)) {
            boolean isUserLocked = user.isLocked(); // Lấy giá trị trạng thái hiện tại
            user.setLocked(!isUserLocked); // Đảo ngược giá trị trạng thái
            userRepository.save(user); // Lưu thay đổi vào cơ sở dữ liệu
            return user.isLocked();
        }
        return false;
    }

    /**
     * hasRole Admin
     * Cập nhật quyền (permission) cho người dùng dựa trên loại quyền đã cho.
     *
     * @param id       ID của người dùng cần cập nhật quyền.
     * @param roleType Loại quyền (RoleType) cần được cập nhật cho người dùng.
     * @return Danh sách quyền sau khi cập nhật.
     * @throws UserNotFoundException Nếu không tìm thấy người dùng với ID đã cho.
     * @throws RoleException         Nếu xảy ra lỗi khi cập nhật quyền, ví dụ: quyền đã tồn tại hoặc quyền không tồn tại.
     */
    @Override
    @Transactional
    public List<Role> updatePermissionUser(int id, RoleType roleType) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User is not found : " + id));
        Hibernate.initialize(user.getRoles());

        // Kiểm tra xem quyền đã tồn tại cho người dùng hay chưa
        boolean roleExists = user.getRoles().stream()
                .anyMatch(role -> role.getName() == roleType);

        if (roleExists) {
            throw new RoleException("Role already exists for the user: " + roleType.name());
        } else {
            Optional<Role> role = roleRepository.findByName(roleType);
            if (role.isPresent()) {
                user.getRoles().add(role.get());
                return user.getRoles();
            } else {
                throw new RoleException("Role is not found: " + roleType.name());
            }
        }
    }

    @Override
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        if (keyword==null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be");
        }

        keyword = removeAccents(keyword).toLowerCase();
        System.out.println("keto: " + keyword);
        Page<User> users = userRepository.searchUsersByKeyword(keyword, pageable);
        Page<UserResponse> userResponses = users.map(user -> userMapper.userToUserResponse(user));
        return userResponses;
    }
    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
