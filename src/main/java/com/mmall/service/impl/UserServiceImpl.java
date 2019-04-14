package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.*;
import com.mmall.vo.UserListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service("iUserService")
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // 密码登录MD5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        // 校验用户名
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        // 校验邮箱
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("Email已存在");
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        // MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        if (sendConfirmMail(user.getEmail(), user.getUsername()) == false) {
            return ServerResponse.createByErrorMessage("邮件发送失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功, 请前往邮箱点击验证链接");
    }

    /**
     * 函数用于发送验证邮件
     *
     * @param mailAddress 待验证邮箱
     * @param userName    用户名
     * @return 邮件发送是否成功
     */
    private boolean sendConfirmMail(String mailAddress, String userName) {
        String plaintext = userName + "," + mailAddress + "," + DateTimeUtil.getDate();
        String ciphertext = null;
        try {
            //加密用户名和邮箱
            ciphertext = SymCrypUtil.encrypt(plaintext, SymCrypUtil.getKey());
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        String url = "http://localhost/user/checkVerifyLink.do?arg=" + ciphertext;
        //拼接内容
        String mailContent = "<p><span>欢迎使用mmall，请在30分钟内访问下面的链接以完成邮箱验证</span></p>"
                + "<p><a href=\"" + url + "\">" + url + "</a></p>";
        //发送邮件
        if (MailUtil.sendMail("邮箱验证", mailContent, mailAddress)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 函数用于验证邮箱验证链接
     *
     * @param link 携带数据参数的request
     */
    @Override
    public ServerResponse<String> checkVerifyLink(String link) {
        if (link == null) {
            return ServerResponse.createByErrorMessage("验证错误");
        }
        String plaintext = null;
        //解密数据
        try {
            plaintext = SymCrypUtil.decrypt(link, SymCrypUtil.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("验证错误");
        }
        try {
            String[] result = plaintext.split(",");
            String username;
            String email;
            String registerTime;
            //如果链接格式不正确
            if (result.length != 3) {
                return ServerResponse.createByErrorMessage("验证错误");
            }
            username = result[0];
            email = result[1];
            registerTime = result[2];
            //判断是否过期
            int time = DateTimeUtil.minutesBetweenDate(registerTime, DateTimeUtil.getDate());
            if (time > 30) {
                return ServerResponse.createByErrorMessage("连接已过期");
            }
            //判断数据是否正确
            int userCount = userMapper.selectCountByNameAndMail(username, email);
            if (userCount != 1) {
                return ServerResponse.createByErrorMessage("用户信息有误");
            }
            //更新用户权限，暂不判断
            if (userMapper.updateValidByUserName(username, Const.UserAccountStatuEnum.VERIFIED.getCode()) == 0) {
                return ServerResponse.createByErrorMessage("验证错误");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ServerResponse.createByErrorMessage("验证错误");
        }
        return ServerResponse.createBySuccessMessage("验证成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            // 校验用户名
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            // 校验邮箱
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("Email已存在");
                }
            }
            if (!Const.USERNAME.equals(type) && !Const.EMAIL.equals(type)) {
                return ServerResponse.createByErrorMessage("参数错误");
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 说明问题及问题答案是这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            RedisShardedPoolUtil.setEx(Const.TOKEN_PREFIX, forgetToken, 60 * 60 * 12);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = RedisShardedPoolUtil.get(Const.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        // username是不能被更新的
        // email也要进行校验，校验新的email是否已经存在，并且存在的email如果相同的话，不能是当前用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已存在，请更换email再尝试更新");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    // backend

    /**
     * 校验用户是否为管理员
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse<PageInfo> getUserList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectList();

        List<UserListVo> userListVoList = Lists.newArrayList();
        for (User userItem : userList) {
            UserListVo userListVo = assembleUserListVo(userItem);
            userListVoList.add(userListVo);
        }
        PageInfo pageResult = new PageInfo(userList);
        pageResult.setList(userListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public int getUserCount() {
        return userMapper.selectUserCount();
    }

    private UserListVo assembleUserListVo(User user) {
        UserListVo userListVo = new UserListVo();
        userListVo.setId(user.getId());
        userListVo.setUsername(user.getUsername());
        userListVo.setEmail(user.getEmail());
        userListVo.setPhone(user.getPhone());
        userListVo.setCreateTime(user.getCreateTime());
        return userListVo;
    }
}
