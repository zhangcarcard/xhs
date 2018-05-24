package com.mofan.xhs.service;

import com.mofan.xhs.domain.Page;
import com.mofan.xhs.domain.UserDO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    Page<UserDO> pageBySelective(Page<UserDO> page);

    int countBySelective(UserDO record);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}
