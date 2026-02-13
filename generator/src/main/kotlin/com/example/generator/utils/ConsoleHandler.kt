package com.example.generator.utils

object ConsoleHandler {
    fun prompt(message: String, default: String): String {
        print("\n$message [默认: $default]: ")
        return readLine()?.trim()?.takeIf { it.isNotEmpty() } ?: default
    }

    fun getTableNames(args: Array<String>): List<String> {
        if (args.isNotEmpty()) return args.flatMap { it.split(",") }.map { it.trim() }.filter { it.isNotEmpty() }

        println("\n【步骤1】请输入要生成的表名 (逗号分隔，q退出)")
        while (true) {
            print("表名: ")
            val input = readLine() ?: ""
            if (input.lowercase() in listOf("q", "exit")) System.exit(0)
            val tables = input.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            if (tables.isNotEmpty()) return tables
            println("❌ 表名不能为空")
        }
    }
}
