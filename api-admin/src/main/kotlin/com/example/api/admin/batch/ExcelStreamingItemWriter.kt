package com.example.api.admin.batch

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.batch.infrastructure.item.Chunk
import org.springframework.batch.infrastructure.item.ExecutionContext
import org.springframework.batch.infrastructure.item.ItemStreamWriter
import java.io.FileOutputStream

class ExcelStreamingItemWriter<T : Any>(
    private val outputFilePath: String,
    private val columnHeaders: List<String>,
    private val rowMapper: (T, Row) -> Unit
) : ItemStreamWriter<T> {

    private lateinit var workbook: SXSSFWorkbook
    private lateinit var fos: FileOutputStream
    private var currentRowNum = 0

    override fun open(executionContext: ExecutionContext) {
        executionContext.putString("outputFilePath", outputFilePath)
        // 保持 100 行在内存，多余的写入磁盘临时文件
        workbook = SXSSFWorkbook(100)
        val sheet = workbook.createSheet("Sheet1")

        // 生成表头
        val headerRow = sheet.createRow(currentRowNum++)
        columnHeaders.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        fos = FileOutputStream(outputFilePath)
    }

    override fun write(chunk: Chunk<out T>) {
        val sheet = workbook.getSheetAt(0)
        for (item in chunk.items) {
            val row = sheet.createRow(currentRowNum++)
            rowMapper(item, row)
        }
    }

    override fun close() {
        if (::workbook.isInitialized) {
            try {
                workbook.write(fos)
            } finally {
                workbook.close()
                if (::fos.isInitialized) fos.close()
            }
        }
    }
}