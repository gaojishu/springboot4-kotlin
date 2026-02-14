package com.example.generator.controller

import com.example.generator.config.GeneratorProps
import com.example.generator.core.GeneratorEngine
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import com.example.generator.dto.req.GeneratorReq
import com.example.generator.dto.res.ApiResult

@Controller
class IndexController {

    @GetMapping("/form")
    fun form(model: Model): String {
        val props = GeneratorProps(System.getProperty("user.dir"))

        model.addAttribute("config", props)
        return "form" // 对应 templates/
    }

    @PostMapping("/execute")
    @ResponseBody // 返回简单字符串即可
    fun execute(@RequestBody params: GeneratorReq): ApiResult<Unit> {

        val props = GeneratorProps(System.getProperty("user.dir"))

        GeneratorEngine(props,params).execute()

        return ApiResult.ok<Unit>().message("生成成功")
    }
}