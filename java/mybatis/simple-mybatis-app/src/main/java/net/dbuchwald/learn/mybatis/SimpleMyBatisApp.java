package net.dbuchwald.learn.mybatis;

import net.dbuchwald.learn.mybatis.model.Customer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * Hello world!
 *
 */
public class SimpleMyBatisApp 
{
    public static void main(String[] args) {
        String propertiesFileName = "mybatis/config/mybatis-config.xml";
        InputStream inputStream = SimpleMyBatisApp.class.getClassLoader().getResourceAsStream(propertiesFileName);

        if (inputStream == null) {
            System.err.println("Unable to load property file: " + propertiesFileName);
        } else {
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            SqlSession session = sqlSessionFactory.openSession();
            try {
                //Customer customer = session.selectOne("mybatis.mappers.CustomerMapper.selectCustomer", 1);
                List<Customer> customers = session.selectList("mybatis.mappers.CustomerMapper.selectAllCustomers");
                for (Customer customer: customers) {
                    System.out.println(customer.toString());
                }
                System.out.println("Size: " + customers.size());
            } finally {
                session.close();
            }
        }
    }
}
