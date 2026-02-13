package com.example.data

import com.example.data.admin.mapper.AdminMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AdminApplicationTests {


    @Autowired
    lateinit var adminMapper: AdminMapper

    @Test
    fun contextLoads() {
//        val adminEntity = AdminEntity()
//        adminEntity.password = "123456"
//        adminEntity.username = UUID.randomUUID().toString()
//        adminEntity.disabledStatus = AdminDisabledStatusEnum.DISABLED_FALSE
//        adminMapper.insert(adminEntity)

//        val admin = adminMapper.selectById(5L)
//
//        adminMapper.deleteById(admin.id)
       // println(admin)
    }

}
