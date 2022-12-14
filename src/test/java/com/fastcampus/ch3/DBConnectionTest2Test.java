package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DBConnectionTest2Test {
    @Autowired
    DataSource ds;

    @Test
    public void insertUserTest() throws Exception{
        User user = new User("asdf13", "1234", "abc", "aaa@aaa.com", new Date(), "fb", new Date());
        deleteAll();
        int rowCnt = insertUser(user);
        assertTrue(rowCnt==1);
    }

    @Test
    public void selectUserTest() throws Exception{
        deleteAll();
        User user = new User("asdf13", "1234", "abc", "aaa@aaa.com", new Date(), "fb", new Date());
        int rowCnt = insertUser(user);
        User user2 = selectUser("asdf13");

        assertTrue(user.getId().equals("asdf13"));
    }

    @Test
    public void updateUserTest() throws Exception{
        User user = new User("asdf13", "1234", "abc", "aaa@aaa.com", new Date(), "fb", new Date());
        int rowCnt = updateUser(user);
        assertTrue(rowCnt==1);
    }

    @Test
    public void deleteUserTest() throws Exception {
        deleteAll();
        int rowCnt = deleteUser("asfd");

        assertTrue(rowCnt==0);

        User user = new User("asdf13", "1234", "abc", "aaa@aaa.com", new Date(), "fb", new Date());
        rowCnt = insertUser(user);
        assertTrue(rowCnt==1);

        rowCnt = deleteUser(user.getId());
        assertTrue(rowCnt==1);

        assertTrue(selectUser(user.getId())==null);
    }

    //??????????????? ?????? ????????? ????????? user_info???????????? update?????? ?????????
    public int updateUser(User user) throws Exception {
        Connection conn = ds.getConnection();

        String sql = "update user_info set pwd=?, email=? where id=?";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Injection??????, ????????????
        pstmt.setString(1, "1111");
        pstmt.setString(2, "bbb@bbbb");
        pstmt.setString(3, user.getId());

        return pstmt.executeUpdate(); // insert, delete, update ??? ??? ??????
    }

    public int deleteUser(String id) throws Exception {
        Connection conn = ds.getConnection();

        String sql = "delete from user_info where id=?";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Injection??????, ????????????
        pstmt.setString(1, id);
        return pstmt.executeUpdate(); // insert, delete, update ??? ??? ??????
    }


    public User selectUser(String id) throws Exception{
        Connection conn = ds.getConnection();

        String sql = "select * from user_info where id=?";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Injection??????, ????????????
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery(); // select ??????

        if(rs.next()){
            User user = new User();
            user.setId(rs.getString(1));
            user.setPwd(rs.getString(2));
            user.setName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setBirth(new Date(rs.getDate(5).getTime()));
            user.setSns(rs.getString(6));
            user.setReg_date(new Date(rs.getDate(7).getTime()));

            return user;
        }

        return null;
    }

    private void deleteAll() throws Exception {
        Connection conn = ds.getConnection();

        String sql = "delete from user_info";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Injection??????, ????????????
        pstmt.executeUpdate(); // insert, delete, update ??? ??? ??????
    }

    // ????????? ????????? user_info???????????? ???????????? ?????????
    public int insertUser(User user) throws Exception{
        Connection conn = ds.getConnection();

//        insert into user_info (id, pwd, name, email, birth, sns, reg_date)
//        values ('asdf', '1234', 'smith', 'aaa@aaa.com', '2022-12-03', 'facebook', now());

        String sql = "insert into user_info values (?, ?, ?, ?, ?, ?, now())";

        PreparedStatement pstmt = conn.prepareStatement(sql); //SQL Injection??????, ????????????
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getPwd());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
        pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
        pstmt.setString(6, user.getSns());

        int rowCnt = pstmt.executeUpdate(); // insert, delete, update ??? ??? ??????


        return rowCnt;
    }


    @Test
    public void springJdbcConnectionTest() throws Exception{
//        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
//        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // ????????????????????? ????????? ?????????.

        System.out.println("conn = " + conn);
        assertTrue(conn!=null); // ?????? ?????? ???????????? true???, ????????? ??????, ????????? ??????
    }

}