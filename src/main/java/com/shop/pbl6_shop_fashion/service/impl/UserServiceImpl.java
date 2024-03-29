package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.dto.user.UserDto;
import com.shop.pbl6_shop_fashion.dto.user.UserMapper;
import com.shop.pbl6_shop_fashion.dto.user.UserMapperImpl;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.BaseException;
import com.shop.pbl6_shop_fashion.exception.RoleException;
import com.shop.pbl6_shop_fashion.exception.UniqueConstraintViolationException;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import com.shop.pbl6_shop_fashion.service.UserService;
import com.shop.pbl6_shop_fashion.util.ImageChecker;
import com.shop.pbl6_shop_fashion.util.ImgBBUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final ImageChecker imageChecker;


    /**
     * hasRole Admin
     * Lấy danh sách người dùng theo trang và kích thước trang đã cho.
     *
     * @param pageable Đối tượng Pageable để xác định trang và kích thước trang.
     * @return Đối tượng Page<UserDto> chứa danh sách người dùng theo trang.
     */
    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDto> userResponses = users.map(user -> userMapper.userToUserDTO(user));
        return userResponses;
    }


    /**
     * hasRole Admin or user.id=id (param)
     * Lấy thông tin người dùng dựa trên ID.
     *
     * @param id ID của người dùng cần lấy thông tin.
     * @return Đối tượng UserDto chứa thông tin của người dùng.
     * @throws UserNotFoundException Nếu không tìm thấy người dùng với ID đã cho.
     */
    @Override
    public UserDto getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User is not found: " + id));
        UserMapper userMapper = new UserMapperImpl();
        return userMapper.userToUserDTO(user);
    }

    /**
     * all user
     * Cập nhật thông tin người dùng dựa trên thông tin được cung cấp trong đối tượng UserDto.
     *
     * @param userResponse Đối tượng UserDto chứa thông tin cần cập nhật.
     * @return Đối tượng UserDto chứa thông tin người dùng đã được cập nhật.
     * @throws UserNotFoundException              Nếu không tìm thấy người dùng với ID đã cho.
     * @throws UniqueConstraintViolationException Nếu xảy ra vi phạm ràng buộc duy nhất (unique constraint) cho số điện thoại hoặc địa chỉ email.
     */
    @Override
    public UserDto updateInfoUser(int userId, UserDto userResponse) {
        User userDb = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found: " + userResponse.getId()));

        try {
            userDb = userMapper.userDTOToUser(userResponse, userDb);
            userRepository.save(userDb);
        } catch (DataIntegrityViolationException e) {
            // Handle the unique constraint violation
            throw new BaseException(e.getMessage());
        }

        return userMapper.userToUserDTO(userDb);
    }

    @Override
    public void updateAvatar(int userId, MultipartFile multipartFile) {
        if (!imageChecker.isImageFile(multipartFile)) {
            throw new IllegalArgumentException("Invalid File ");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        String imageUrl = ImgBBUtils.uploadImage(multipartFile);
        user.setUrlImage(imageUrl);
        userRepository.save(user);
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User is not found : " + id));
        Hibernate.initialize(user.getRoles());

        boolean roleExists = user.getRoles()
                .stream()
                .anyMatch(role -> role.getName() == roleType);

        if (roleExists) {
            throw new RoleException("Role already exists for the user: " + roleType.name());
        }

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RoleException("Role is not found: " + roleType.name()));
        user.getRoles().add(role);
        return user.getRoles();
    }

    @Override
    public Slice<UserDto> searchUsers(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null");
        }

        keyword = removeAccents(keyword).toLowerCase();
        Slice<User> users = userRepository.searchUsersByKeyword(keyword, pageable);
        return users.map(user -> userMapper.userToUserDTO(user));
    }

    @Override
    public User findById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found: " + userId));
    }


    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
}
