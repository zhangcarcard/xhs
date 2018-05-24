package com.mofan.xhs.repository;

import com.mofan.xhs.domain.Page;
import com.mofan.xhs.domain.UserDO;

import java.util.List;

public interface UserRepository {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    List<UserDO> selectAll();

    List<UserDO> pageBySelective(Page<UserDO> page);

    int countBySelective(UserDO record);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}