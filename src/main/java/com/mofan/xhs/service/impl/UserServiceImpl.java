package com.mofan.xhs.service.impl;

import com.mofan.xhs.domain.Page;
import com.mofan.xhs.domain.UserDO;
import com.mofan.xhs.repository.UserRepository;
import com.mofan.xhs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userRepository.deleteByPrimaryKey(id);
    }

    @Transactional
    @Override
    public int insert(UserDO record) {
        return userRepository.insert(record);
    }

    @Transactional
    @Override
    public int insertSelective(UserDO record) {
        return userRepository.insertSelective(record);
    }

    @Override
    public UserDO selectByPrimaryKey(Integer id) {
        return userRepository.selectByPrimaryKey(id);
    }

    @Override
    public Page<UserDO> pageBySelective(Page<UserDO> page) {
        List<UserDO> list = userRepository.pageBySelective(page);
        int count = userRepository.countBySelective(page.getCondition());

        page.setResults(list);
        page.setTotal(count);
        return page;
    }

    @Override
    public int countBySelective(UserDO record) {
        return userRepository.countBySelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserDO record) {
        return userRepository.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserDO record) {
        return userRepository.updateByPrimaryKey(record);
    }
}
